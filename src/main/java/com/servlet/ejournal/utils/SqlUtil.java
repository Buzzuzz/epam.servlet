package com.servlet.ejournal.utils;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.constants.SQLQueries;
import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.exceptions.UtilException;
import com.servlet.ejournal.model.dao.DataSource;
import com.servlet.ejournal.constants.RegexConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Log4j2
public class SqlUtil {

    // Suppress constructor
    private SqlUtil() {
    }

    public static int getRecordsCount(Connection con, String countable, String table, Map<String, String[]> filters) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            StringBuilder sb = new StringBuilder(SQLQueries.COUNT_RECORDS);
            sb.replace(sb.indexOf("?"), sb.indexOf("?") + 1, countable)
                    .replace(sb.indexOf("?"), sb.indexOf("?") + 1, table).append(" ")
                    .append(buildFilters(filters));

            statement = con.prepareStatement(sb.toString());
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (Exception e) {
            String message = String.format("Can't count records in table %s, by %s field", table, countable);
            log.error(message, e);
            throw new DAOException(message, e);
        } finally {
            DataSource.closeAll(resultSet, statement);
        }
    }

    public static int[] getPages(int limit, int recordsCount) {
        int maxPages = recordsCount % limit == 0 ? recordsCount / limit : recordsCount / limit + 1;
        return IntStream.range(1, maxPages + 1).toArray();
    }

    public static int getLimit(HttpServletRequest req) {
        return req.getParameter(AttributeConstants.DISPLAY_RECORDS_NUMBER) == null ?
                AttributeConstants.DEFAULT_LIMIT :
                Integer.parseInt(req.getParameter(AttributeConstants.DISPLAY_RECORDS_NUMBER));
    }

    public static int getCurrentPage(HttpServletRequest req) {
        return req.getParameter(AttributeConstants.CURRENT_PAGE) == null ?
                AttributeConstants.DEFAULT_PAGE :
                Integer.parseInt(req.getParameter(AttributeConstants.CURRENT_PAGE));
    }

    public static int getOffset(int limit, int currentPage) {
        if (currentPage < 1) {
            return AttributeConstants.DEFAULT_OFFSET;
        }
        return limit * (currentPage - 1);
    }

    public static String getSortingType(HttpServletRequest req, Class c) throws UtilException {
        try {
            String className = c.getSimpleName().toUpperCase();
            Class<AttributeConstants> ac = AttributeConstants.class;
            Field sortingField = ac.getDeclaredField(String.format("DEFAULT_%s_SORTING", className));
            return req.getParameter(AttributeConstants.SORTING_TYPE) == null ?
                    sortingField.get(ac).toString() :
                    req.getParameter(AttributeConstants.SORTING_TYPE);
        } catch (Exception e) {
            String message = String.format("Can't get default sorting for %s in class %s",
                    c.getName(), AttributeConstants.class);
            log.error(message, e);
            throw new UtilException(message, e);
        }
    }

    public static String getFilter(HttpServletRequest req, String filterName) {
        if (req.getParameter(filterName) != null) {
            return req.getParameter(filterName).equals(AttributeConstants.NONE_ATTR) ? AttributeConstants.NONE_ATTR : req.getParameter(filterName);
        }
        return AttributeConstants.NONE_ATTR;
    }

    public static Map<String, String[]> getFilters(HttpServletRequest req, String... filterNames) {
        Map<String, String[]> resultFiltersMap = new HashMap<>();

        Arrays.stream(filterNames).forEach(filter -> {
            String currentFilter = getFilter(req, filter);
            if (!currentFilter.equals(AttributeConstants.NONE_ATTR)) {
                resultFiltersMap.put(filter, new String[]{currentFilter});
            }
        });

        return resultFiltersMap;
    }

    public static void getEndDateFilter(String endDateFilter, Map<String, String[]> filters) {
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());

        switch (endDateFilter) {
            case AttributeConstants.COURSE_NOT_STARTED:
                filters.put(AttributeConstants.QUERY, new String[]{String.format("%s > %s", SQLQueries.START_DATE_MILLIS, currentDate.getTime())});
                break;
            case AttributeConstants.COURSE_IN_PROGRESS:
                filters.put(AttributeConstants.QUERY, new String[]{String.format("%s < %s and %s > %s", SQLQueries.START_DATE_MILLIS, currentDate.getTime(), SQLQueries.END_DATE_MILLIS, currentDate.getTime())});
                break;
            case AttributeConstants.COURSE_ENDED:
                filters.put(AttributeConstants.QUERY, new String[]{String.format("%s < %s", SQLQueries.END_DATE_MILLIS, currentDate.getTime())});
                break;
            default:
                // no filtration by date
                break;
        }
    }

    public static void getMyCourseFilter(HttpServletRequest req, long id, Map<String, String[]> filters) {
        String userFilterString = String.format(AttributeConstants.FULL_COLUMN_NAME, AttributeConstants.USER_COURSE_TABLE, AttributeConstants.USER_ID);
        String switchPosition = getFilter(req, AttributeConstants.SWITCH);
        if (!switchPosition.equals(AttributeConstants.NONE_ATTR)) {
            filters.put(userFilterString, new String[]{String.valueOf(id)});
        } else {
            filters.put(AttributeConstants.FINAL_MARK, new String[]{"-1"});
        }
    }

    public static String getEntityPaginationQuery(String table, Map<String, String[]> filters) {
        return String.format("%s %s ", SQLQueries.SELECT_EVERYTHING_FROM_PART, table) + buildFilters(filters) + SQLQueries.PAGINATION_LIMIT_OFFSET_QUERY_PART;
    }

    // TODO : refactor filter building
    private static String buildFilters(Map<String, String[]> filters) {
        String builded = "";
        if (filters != null && !filters.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("where ");

            for (Map.Entry<String, String[]> entry : filters.entrySet()) {
                if (entry.getValue() == null) continue;
                for (String stringValue : entry.getValue()) {
                    if (entry.getKey().equals(AttributeConstants.QUERY)) {
                        sb.append(String.format("%s and ", stringValue));
                    } else {
                        String filter = stringValue.matches(RegexConstants.IS_A_NUMBER) ?
                                String.format(String.format("%s = %s and ", entry.getKey(), stringValue)) :
                                String.format("%s = '%s' and ", entry.getKey(), stringValue);
                        sb.append(filter);
                    }
                    log.info(sb.toString());
                }
            }
            sb.delete(sb.lastIndexOf("and"), sb.length());
            builded = sb.toString();
        }
        return builded;
    }
}
