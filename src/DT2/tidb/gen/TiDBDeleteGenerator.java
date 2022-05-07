package DT2.tidb.gen;

import java.sql.SQLException;

import DT2.Randomly;
import DT2.common.query.ExpectedErrors;
import DT2.common.query.SQLQueryAdapter;
import DT2.tidb.TiDBExpressionGenerator;
import DT2.tidb.TiDBProvider.TiDBGlobalState;
import DT2.tidb.TiDBSchema.TiDBTable;
import DT2.tidb.visitor.TiDBVisitor;

public final class TiDBDeleteGenerator {

    private TiDBDeleteGenerator() {
    }

    public static SQLQueryAdapter getQuery(TiDBGlobalState globalState) throws SQLException {
        ExpectedErrors errors = new ExpectedErrors();
        TiDBTable table = globalState.getSchema().getRandomTable(t -> !t.isView());
        TiDBExpressionGenerator gen = new TiDBExpressionGenerator(globalState).setColumns(table.getColumns());
        StringBuilder sb = new StringBuilder("DELETE ");
        if (Randomly.getBooleanWithSmallProbability()) {
            sb.append("LOW_PRIORITY ");
        }
        if (Randomly.getBooleanWithSmallProbability()) {
            sb.append("QUICK ");
        }
        if (Randomly.getBooleanWithSmallProbability()) {
            sb.append("IGNORE ");
        }
        sb.append("FROM ");
        sb.append(table.getName());
        if (Randomly.getBoolean()) {
            sb.append(" WHERE ");
            sb.append(TiDBVisitor.asString(gen.generateExpression()));
//            errors.add("Truncated incorrect");
//            errors.add("Data truncation");
//            errors.add("Truncated incorrect FLOAT value");
        }
//        if (Randomly.getBoolean()) {
//            sb.append(" ORDER BY ");
//            TiDBErrors.addExpressionErrors(errors);
//            sb.append(gen.generateOrderBys().stream().map(o -> TiDBVisitor.asString(o))
//                    .collect(Collectors.joining(", ")));
//        }
//        if (Randomly.getBoolean()) {
//            sb.append(" LIMIT ");
//            sb.append(Randomly.getNotCachedInteger(0, Integer.MAX_VALUE));
//        }
//        errors.add("Bad Number");
//        errors.add("Truncated incorrect"); // https://github.com/pingcap/tidb/issues/24292
//        errors.add("is not valid for CHARACTER SET");
//        errors.add("Division by 0");
//        errors.add("error parsing regexp");
        return new SQLQueryAdapter(sb.toString(), errors);

    }

}