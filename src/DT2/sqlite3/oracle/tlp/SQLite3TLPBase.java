package DT2.sqlite3.oracle.tlp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import DT2.Randomly;
import DT2.common.gen.ExpressionGenerator;
import DT2.common.oracle.TernaryLogicPartitioningOracleBase;
import DT2.common.oracle.TestOracle;
import DT2.sqlite3.SQLite3Errors;
import DT2.sqlite3.SQLite3GlobalState;
import DT2.sqlite3.ast.SQLite3Expression;
import DT2.sqlite3.ast.SQLite3Expression.Join;
import DT2.sqlite3.ast.SQLite3Expression.SQLite3ColumnName;
import DT2.sqlite3.ast.SQLite3Select;
import DT2.sqlite3.gen.SQLite3Common;
import DT2.sqlite3.gen.SQLite3ExpressionGenerator;
import DT2.sqlite3.schema.SQLite3Schema;
import DT2.sqlite3.schema.SQLite3Schema.SQLite3Column;
import DT2.sqlite3.schema.SQLite3Schema.SQLite3Table;
import DT2.sqlite3.schema.SQLite3Schema.SQLite3Tables;

public class SQLite3TLPBase extends TernaryLogicPartitioningOracleBase<SQLite3Expression, SQLite3GlobalState>
        implements TestOracle {

    SQLite3Schema s;
    SQLite3Tables targetTables;
    SQLite3ExpressionGenerator gen;
    SQLite3Select select;

    public SQLite3TLPBase(SQLite3GlobalState state) {
        super(state);
        SQLite3Errors.addExpectedExpressionErrors(errors);
        SQLite3Errors.addQueryErrors(errors);
    }

    @Override
    public void check() throws SQLException {
        s = state.getSchema();
        targetTables = s.getRandomTableNonEmptyTables();
        gen = new SQLite3ExpressionGenerator(state).setColumns(targetTables.getColumns());
        initializeTernaryPredicateVariants();
        select = new SQLite3Select();
        select.setFetchColumns(generateFetchColumns());
        List<SQLite3Table> tables = targetTables.getTables();
        List<Join> joinStatements = gen.getRandomJoinClauses(tables);
        List<SQLite3Expression> tableRefs = SQLite3Common.getTableRefs(tables, s);
        select.setJoinClauses(joinStatements.stream().collect(Collectors.toList()));
        select.setFromTables(tableRefs);
        select.setWhereClause(null);
    }

    List<SQLite3Expression> generateFetchColumns() {
        List<SQLite3Expression> columns = new ArrayList<>();
        if (Randomly.getBoolean()) {
            columns.add(new SQLite3ColumnName(SQLite3Column.createDummy("*"), null));
        } else {
            columns = Randomly.nonEmptySubset(targetTables.getColumns()).stream()
                    .map(c -> new SQLite3ColumnName(c, null)).collect(Collectors.toList());
        }
        return columns;
    }

    @Override
    protected ExpressionGenerator<SQLite3Expression> getGen() {
        return gen;
    }

}
