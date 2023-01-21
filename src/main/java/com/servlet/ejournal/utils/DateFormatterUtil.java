package com.servlet.ejournal.utils;

import com.servlet.ejournal.exceptions.UtilException;
import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class DateFormatterUtil {
    // Suppress constructor
    private DateFormatterUtil() {
    }

    /**
     * Method to acquire {@link Timestamp} from date-string from View (without time)
     *
     * @param date {@link String} in format YYYY-MM-DD
     * @return {@link Timestamp} for provided string (only date, no time)
     * @throws UtilException if provided string is in wrong formatting
     */
    public static Timestamp getTimestamp(String date) throws UtilException {
        try {
            List<Integer> splitDate = Arrays
                    .stream(date.split("-"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            return new Timestamp(
                    splitDate.get(0) - 1900,
                    splitDate.get(1) - 1,
                    splitDate.get(2),
                    0, 0, 0, 0);
        } catch (Exception e) {
            log.error("Can't convert date-string to Timestamp!", e);
            throw new UtilException("Can't convert date-string to Timestamp!", e);
        }
    }
}
