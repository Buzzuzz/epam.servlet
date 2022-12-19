package testDAO;

import model.dao.DataSource;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

@Log4j2
class TestDataSource {
    private static Connection con = null;
    @Test
    void testSuccessfulGetConnection() {
        con = DataSource.getConnection();
        Assertions.assertNotNull(con);
    }

    @Test
    void testSuccessfulCloseConnection() throws SQLException {
        try {
            con = DataSource.getConnection();
            Assertions.assertFalse(con.isClosed());
            con.close();
            Assertions.assertTrue(con.isClosed());
        } catch (SQLException e) {
            log.error("Can't close connection");
            throw new SQLException("Can't close connection", e);
        }
    }
}
