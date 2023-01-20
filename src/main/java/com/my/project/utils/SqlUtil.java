package com.my.project.utils;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.SQLQueries;
import com.my.project.exceptions.DAOException;
import com.my.project.exceptions.UtilException;
import com.my.project.model.dao.DataSource;
import com.my.project.constants.RegexConstants;
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

import static com.my.project.constants.AttributeConstants.*;
import static com.my.project.constants.AttributeConstants.QUERY;
import static com.my.project.constants.SQLQueries.*;

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

    public static String getEntityPaginationQuery(String table, Map<String, String[]> filters) {
        return String.format("%s %s ", SELECT_EVERYTHING_FROM_PART, table) + buildFilters(filters) + PAGINATION_LIMIT_OFFSET_QUERY_PART;
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
