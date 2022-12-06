package testDAO;

import dao.DAOException;
import dao.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TestNoConfigFileDataSource {
    static File testPath = new File("src/main/resources/config/db.properties");
    static File afterTestPath = new File("src/main/resources/config/db_test.properties");

    @BeforeAll
    static void setUp() {
        testPath.renameTo(afterTestPath);
    }

    @AfterAll
    static void cleanUp() {
        afterTestPath.renameTo(testPath);
    }

    @Test
    void TestNoConfigFile() {
        Assertions.assertThrows(ExceptionInInitializerError.class, DataSource::getConnection);
    }

    @Test
    void TestWrongProperties() {
        Assertions.assertThrows(NoClassDefFoundError.class, DataSource::getConnection);
    }
}
