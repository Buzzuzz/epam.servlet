package testDAO.testConfig;

import dao.DataSource;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

@Log4j2
class TestConfigFileDataSource {
    static File testPath = new File("src/main/resources/db.properties");
    static File afterTestPath = new File("src/main/resources/db_test.properties");

    @Test
    void TestNoConfigFile() {
        testPath.renameTo(afterTestPath);
        Assertions.assertThrows(ExceptionInInitializerError.class, DataSource::getConnection);
        afterTestPath.renameTo(testPath);
    }

}
