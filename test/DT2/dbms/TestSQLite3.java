package DT2.dbms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import DT2.Main;

public class TestSQLite3 {

    @Test
    public void testSqlite() {
        // run with one thread due to multithreading issues, see https://github.com/sqlancer/sqlancer/pull/45
        assertEquals(0, Main.executeMain(new String[] { "--random-seed", "0", "--timeout-seconds", TestConfig.SECONDS,
                "--num-threads", "1", "--num-queries", "0", "sqlite3" }));
    }

}
