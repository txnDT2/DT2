package DT2.citus;

import java.sql.SQLException;

import DT2.postgres.PostgresGlobalState;

public class CitusGlobalState extends PostgresGlobalState {

    private boolean repartition;

    public void setRepartition(boolean repartition) {
        this.repartition = repartition;
    }

    public boolean getRepartition() {
        return repartition;
    }

    @Override
    public CitusSchema readSchema() throws SQLException {
        return CitusSchema.fromConnection(getConnection(), getDatabaseName());
    }

}
