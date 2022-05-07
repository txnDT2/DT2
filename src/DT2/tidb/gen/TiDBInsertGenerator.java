package DT2.tidb.gen;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import DT2.Randomly;
import DT2.common.query.ExpectedErrors;
import DT2.common.query.SQLQueryAdapter;
import DT2.mysql.gen.MySQLExpressionGenerator;
import DT2.tidb.TiDBErrors;
import DT2.tidb.TiDBExpressionGenerator;
import DT2.tidb.TiDBProvider.TiDBGlobalState;
import DT2.tidb.TiDBSchema.TiDBColumn;
import DT2.tidb.TiDBSchema.TiDBTable;
import DT2.tidb.visitor.TiDBVisitor;

public class TiDBInsertGenerator {

    private final TiDBGlobalState globalState;
    private final ExpectedErrors errors = new ExpectedErrors();
    private TiDBExpressionGenerator gen;

    public TiDBInsertGenerator(TiDBGlobalState globalState) {
        this.globalState = globalState;
        TiDBErrors.addInsertErrors(errors);
    }

    public static SQLQueryAdapter getQuery(TiDBGlobalState globalState) throws SQLException {
        return new TiDBInsertGenerator(globalState).get();
    }

    private SQLQueryAdapter get() {
        TiDBTable table = globalState.getSchema().getRandomTable(t -> !t.isView());
        gen = new TiDBExpressionGenerator(globalState).setColumns(table.getColumns());
        StringBuilder sb = new StringBuilder();
        boolean isInsert = Randomly.getBoolean();
        sb.append("INSERT");
//        if (isInsert) {
//            sb.append("INSERT");
//        } else {
//            sb.append("REPLACE");
//        }
//        if (Randomly.getBooleanWithRatherLowProbability()) {
//            sb.append(" ");
//            sb.append(Randomly.fromOptions("LOW_PRIORITY", "HIGH_PRIORITY", "DELAYED"));
//        }
        if (isInsert && Randomly.getBoolean()) {
            sb.append(" IGNORE ");
        }
        sb.append(" INTO ");
        sb.append(table.getName());
        if (Randomly.getBoolean()) {
            sb.append(" VALUES ");
            List<TiDBColumn> columns = table.getColumns();
            insertColumns(sb, columns);
        } else {
            List<TiDBColumn> columnSubset = table.getRandomNonEmptyColumnSubset();
            sb.append("(");
            sb.append(columnSubset.stream().map(c -> c.getName()).collect(Collectors.joining(", ")));
            sb.append(") VALUES ");
            insertColumns(sb, columnSubset);
        }
//        if (isInsert && Randomly.getBoolean()) {
//            sb.append(" ON DUPLICATE KEY UPDATE ");
//            sb.append(table.getRandomColumn().getName());
//            sb.append("=");
//            sb.append(TiDBVisitor.asString(gen.generateExpression()));
//        }
//        errors.add("Illegal mix of collations");
        return new SQLQueryAdapter(sb.toString(), errors);
    }

    private void insertColumns(StringBuilder sb, List<TiDBColumn> columns) {
//        for (int nrRows = 0; nrRows < Randomly.smallNumber() + 1; nrRows++) {
//            if (nrRows != 0) {
//                sb.append(", ");
//            }
            sb.append("(");
            for (int nrColumn = 0; nrColumn < columns.size(); nrColumn++) {
                if (nrColumn != 0) {
                    sb.append(", ");
                }
                String columnName = columns.get(nrColumn).getName();
                String columnType = TiDBTableGenerator.columnType.get(columnName);
                sb.append(MySQLExpressionGenerator.generateConstant(columnType));
//                insertValue(sb);
            }
            sb.append(")");
//        }
    }

    private void insertValue(StringBuilder sb) {
        sb.append(gen.generateConstant()); // TODO: try to insert valid data
    }

}