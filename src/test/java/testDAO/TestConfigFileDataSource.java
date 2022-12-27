package testDAO;

import dao.DataSource;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

@Log4j2
class TestConfigFileDataSource {
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
    void testWrongProperties() {
        InputStream input = null;
        OutputStream out = null;
        try {
            Properties normalProps = new Properties();
            input = Files.newInputStream(afterTestPath.toPath());
            normalProps.load(input);
            String resetName = normalProps.getProperty("dataSource.user");

            out = Files.newOutputStream(afterTestPath.toPath());
            normalProps.setProperty("dataSource.user", "test_data");
            normalProps.store(out, null);
            out.close();

            afterTestPath.renameTo(testPath);
            Assertions.assertThrows(NoClassDefFoundError.class, DataSource::getConnection);
            testPath.renameTo(afterTestPath);

            out = Files.newOutputStream(afterTestPath.toPath());
            normalProps.load(input);
            normalProps.setProperty("dataSource.user", resetName);
            normalProps.store(out, null);
        } catch (Exception e) {
            log.error("Can't locate database config file");
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignored) {}
        }
    }
}
