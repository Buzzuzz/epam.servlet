package model.dao;

import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class created for initial tests setup.
 * Mainly contains methods to change database in <a href="file:src/main/resources/db.properties">properties file</a>
 * to epam_test (test database, method {@link TestSetup#setup()}) and epam (main database, method {@link TestSetup#cleanup()})
 */
@Log4j2
public class TestSetup {
    private static final Properties URLS = new Properties();
    private static final Properties DB = new Properties();
    private static final String URLS_PATH = "src/main/resources/urls.properties";
    private static final String DB_PATH = "src/main/resources/db.properties";
    private static final Lock LOCK = new ReentrantLock();

    // Suppress constructor
    private TestSetup() {
    }

    static {
        try (FileInputStream urlsInput = new FileInputStream(URLS_PATH);
             FileInputStream dbInput = new FileInputStream(DB_PATH)) {
            URLS.load(urlsInput);
            DB.load(dbInput);
        } catch (IOException e) {
            log.error("Can't locate properties files");
        }
    }

    /**
     * Method to change database to test case.
     * Run before all tests.
     */
    public static void setup() {
        try (FileOutputStream out = new FileOutputStream(DB_PATH)){
            LOCK.lock();
            DB.setProperty("jdbcUrl", URLS.getProperty("test.url"));
            DB.store(out, null);
        } catch (IOException e) {
            log.error("Can't locate database properties file");
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Method to change database to main case.
     * Run after all tests.
     */
    public static void cleanup() {
        try (FileOutputStream out = new FileOutputStream(DB_PATH)){
            LOCK.lock();
            DB.setProperty("jdbcUrl", URLS.getProperty("main.url"));
            DB.store(out, null);
        } catch (IOException e) {
            log.error("Can't locate database properties file");
        } finally {
            LOCK.unlock();
        }
    }
}
