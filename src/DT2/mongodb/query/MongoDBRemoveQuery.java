package DT2.mongodb.query;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.result.DeleteResult;

import DT2.GlobalState;
import DT2.Main;
import DT2.common.query.ExpectedErrors;
import DT2.mongodb.MongoDBConnection;
import DT2.mongodb.MongoDBQueryAdapter;
import DT2.mongodb.MongoDBSchema;

public class MongoDBRemoveQuery extends MongoDBQueryAdapter {

    private final String objectId;
    private final MongoDBSchema.MongoDBTable table;

    public MongoDBRemoveQuery(MongoDBSchema.MongoDBTable table, String objectId) {
        this.objectId = objectId;
        this.table = table;
    }

    @Override
    public boolean couldAffectSchema() {
        return true;
    }

    @Override
    public <G extends GlobalState<?, ?, MongoDBConnection>> boolean execute(G globalState, String... fills)
            throws Exception {
        try {
            DeleteResult result = globalState.getConnection().getDatabase().getCollection(table.getName())
                    .deleteOne(new Document("_id", new ObjectId(objectId)));
            if (result.wasAcknowledged()) {
                Main.nrSuccessfulActions.addAndGet(1);
            } else {
                Main.nrUnsuccessfulActions.addAndGet(1);
            }
            return result.wasAcknowledged();
        } catch (Exception e) {
            Main.nrUnsuccessfulActions.addAndGet(1);
            return false;
        }
    }

    @Override
    public ExpectedErrors getExpectedErrors() {
        return new ExpectedErrors();
    }

    @Override
    public String getLogString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("db.").append(table.getName()).append(".remove({'_id': '").append(objectId).append("'})");
        return stringBuilder.toString();
    }
}
