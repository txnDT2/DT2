package DT2.arangodb.query;

import java.util.Collections;

import com.arangodb.ArangoCollection;

import DT2.GlobalState;
import DT2.Main;
import DT2.arangodb.ArangoDBConnection;
import DT2.arangodb.ArangoDBQueryAdapter;
import DT2.arangodb.ArangoDBSchema;
import DT2.common.query.ExpectedErrors;

public class ArangoDBCreateIndexQuery extends ArangoDBQueryAdapter {

    private final ArangoDBSchema.ArangoDBColumn column;

    public ArangoDBCreateIndexQuery(ArangoDBSchema.ArangoDBColumn column) {
        this.column = column;
    }

    @Override
    public boolean couldAffectSchema() {
        return false;
    }

    @Override
    public <G extends GlobalState<?, ?, ArangoDBConnection>> boolean execute(G globalState, String... fills)
            throws Exception {
        try {
            ArangoCollection collection = globalState.getConnection().getDatabase()
                    .collection(column.getTable().getName());
            collection.ensureHashIndex(Collections.singletonList(column.getName()), null);
            Main.nrSuccessfulActions.addAndGet(1);
            return true;
        } catch (Exception e) {
            Main.nrUnsuccessfulActions.addAndGet(1);
            throw e;
        }
    }

    @Override
    public ExpectedErrors getExpectedErrors() {
        return new ExpectedErrors();
    }

    @Override
    public String getLogString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("db.").append(column.getTable().getName())
                .append(".ensureIndex({type: \"hash\", fields: [ \"").append(column.getName()).append("\" ]});");
        return stringBuilder.toString();
    }
}
