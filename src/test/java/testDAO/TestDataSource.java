package testDAO;

import dao.DataSource;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;

@Log4j2
class TestDataSource {
    @Test
    void TestSuccessfulGetConnection() {
        DataSource.getConnection();
        Assertions.assertNotNull(DataSource.getConnection());
    }
}
