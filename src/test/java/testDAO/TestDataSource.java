package testDAO;

import dao.DataSource;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

@Log4j2
class TestDataSource {
    @Test
    void TestSuccessfulGetConnection() {
        Connection con = DataSource.getConnection();
        Assertions.assertNotNull(DataSource.getConnection());
        try {
            con.close();
        } catch (SQLException e) {
            log.error("Can't close connection");
        }
    }
}
