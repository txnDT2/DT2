package DT2.duckdb.gen;

import DT2.Randomly;
import DT2.common.query.ExpectedErrors;
import DT2.common.query.SQLQueryAdapter;
import DT2.duckdb.DuckDBErrors;
import DT2.duckdb.DuckDBProvider.DuckDBGlobalState;
import DT2.duckdb.DuckDBToStringVisitor;

public final class DuckDBViewGenerator {

    private DuckDBViewGenerator() {
    }

    public static SQLQueryAdapter generate(DuckDBGlobalState globalState) {
        int nrColumns = Randomly.smallNumber() + 1;
        StringBuilder sb = new StringBuilder("CREATE ");
        sb.append("VIEW ");
        sb.append(globalState.getSchema().getFreeViewName());
        sb.append("(");
        for (int i = 0; i < nrColumns; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("c");
            sb.append(i);
        }
        sb.append(") AS ");
        sb.append(DuckDBToStringVisitor.asString(DuckDBRandomQuerySynthesizer.generateSelect(globalState, nrColumns)));
        ExpectedErrors errors = new ExpectedErrors();
        DuckDBErrors.addExpressionErrors(errors);
        DuckDBErrors.addGroupByErrors(errors);
        return new SQLQueryAdapter(sb.toString(), errors, true);
    }

}
