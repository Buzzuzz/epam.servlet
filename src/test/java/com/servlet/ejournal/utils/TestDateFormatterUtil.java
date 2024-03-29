package com.servlet.ejournal.utils;

import com.servlet.ejournal.exceptions.UtilException;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static com.servlet.ejournal.utils.DateFormatterUtil.getTimestamp;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestDateFormatterUtil {
    @Test
    void testWrongDateString() {
        assertThrows(UtilException.class, () -> getTimestamp("02.01.2003"));
    }

    @Test
    void testGetActualTimeStamp() throws UtilException {
        Timestamp result = getTimestamp("2003-01-02");
        assertEquals(2003, result.toLocalDateTime().getYear());
        assertEquals(1, result.toLocalDateTime().getMonthValue());
        assertEquals(2, result.toLocalDateTime().getDayOfMonth());
    }
}
