package com.servlet.ejournal.utils;

import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.constants.RegexConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.constants.SQLQueries.*;

@Log4j2
public class SqlUtil {

    // Suppress constructor
    private SqlUtil() {
    }

    public static int getRecordsCount(Connection con, String countable, String table, Map<String, String[]> filters) throws DAOException {
        String query = COUNT_RECORDS
                .replaceFirst("\\?", countable)
                .replaceFirst("\\?", table)
                .concat(" ")
                .concat(buildFilters(filters));

        try (PreparedStatement statement = con.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            String message = String.format("Can't count records in table %s, by %s field", table, countable);
            log.error(message, e);
            throw new DAOException(message, e);
        }
    }

    public static int[] getPages(int limit, int recordsCount) {
        if (recordsCount == 0 || limit == 0) return new int[]{1};
        int maxPages = recordsCount % limit == 0 ? recordsCount / limit : recordsCount / limit + 1;
        return IntStream.range(1, maxPages + 1).toArray();
    }

    public static int getLimit(HttpServletRequest req) {
        try {
            return req.getParameter(DISPLAY_RECORDS_NUMBER) == null ?
                    DEFAULT_LIMIT :
                    Integer.parseInt(req.getParameter(DISPLAY_RECORDS_NUMBER));
        } catch (NumberFormatException e) {
            return DEFAULT_LIMIT;
        }
    }

    public static int getCurrentPage(HttpServletRequest req) {
        try {
            return req.getParameter(CURRENT_PAGE) == null ?
                    DEFAULT_PAGE :
                    Integer.parseInt(req.getParameter(CURRENT_PAGE));
        } catch (NumberFormatException e) {
            return DEFAULT_PAGE;
        }
    }

    public static int getOffset(int limit, int currentPage) {
        return currentPage < 1 ? DEFAULT_OFFSET : limit * (currentPage - 1);
    }

    public static String getSortingType(HttpServletRequest req, String defaultSorting) {
        return req.getParameter(SORTING_TYPE) == null ? defaultSorting : req.getParameter(SORTING_TYPE);
    }

    public static String getFilter(HttpServletRequest req, String filterName) {
        return req.getParameter(filterName) == null ? NONE_ATTR : req.getParameter(filterName);
    }

    public static Map<String, String[]> getFilters(HttpServletRequest req, String... filterNames) {
        Map<String, String[]> resultFiltersMap = new HashMap<>();

        Arrays.stream(filterNames).forEach(filter -> {
            String currentFilter = getFilter(req, filter);
            if (!currentFilter.equals(NONE_ATTR)) {
                resultFiltersMap.put(filter, new String[]{currentFilter});
            }
        });

        return resultFiltersMap;
    }

    public static void getEndDateFilter(String endDateFilter, Map<String, String[]> filters) {
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());

        switch (endDateFilter) {
            case COURSE_NOT_STARTED:
                filters.put(QUERY, new String[]{String.format("%s > %s", START_DATE_MILLIS, currentDate.getTime())});
                break;
            case COURSE_IN_PROGRESS:
                filters.put(QUERY, new String[]{String.format("%s < %s and %s > %s", START_DATE_MILLIS, currentDate.getTime(), END_DATE_MILLIS, currentDate.getTime())});
                break;
            case COURSE_ENDED:
                filters.put(QUERY, new String[]{String.format("%s < %s", END_DATE_MILLIS, currentDate.getTime())});
                break;
            default:
                // no filtration by date
                break;
        }
    }

    public static void getMyCourseFilter(HttpServletRequest req, long id, Map<String, String[]> filters) {
        String userFilterString = String.format(FULL_COLUMN_NAME, USER_COURSE_TABLE, USER_ID);
        String switchPosition = getFilter(req, SWITCH);
        if (!switchPosition.equals(NONE_ATTR)) {
            filters.put(userFilterString, new String[]{String.valueOf(id)});
        } else {
            filters.put(FINAL_MARK, new String[]{"-1"});
        }
    }

    public static String getAllEntitiesQuery(String table, int limit, int offset, String sorting, Map<String, String[]> filters) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %s ", SELECT_EVERYTHING_FROM_PART, table))
                .append(buildFilters(filters))
                .append(PAGINATION_LIMIT_OFFSET_QUERY_PART);

        sb.replace(sb.indexOf("?"), sb.indexOf("?") + 1, sorting)
                .replace(sb.indexOf("?"), sb.indexOf("?") + 1, String.valueOf(limit))
                .replace(sb.indexOf("?"), sb.indexOf("?") + 1, String.valueOf(offset));

        return sb.toString();
    }

    // TODO : refactor
    private static String buildFilters(Map<String, String[]> filters) {
        StringBuilder sb = new StringBuilder();
        if (filters != null && !filters.isEmpty()) {
            sb.append("where ");
            filters.forEach((filter, values) -> {
                if (values != null) {
                    for (String concreteValue : values) {
                        if (filter.equals(QUERY)) {
                            sb.append(String.format("%s and ", concreteValue));
                        } else {
                            sb.append(String.format("%s = %s and ", filter,
                                    isValidNumber(concreteValue) ? concreteValue : String.format("'%s'", concreteValue)));
                        }
                    }
                }
            });
            sb.delete(sb.lastIndexOf("and"), sb.length());
        }
        return sb.toString();
    }

    private static boolean isValidNumber(String value) {
        return value.matches(RegexConstants.IS_A_NUMBER);
    }
}
