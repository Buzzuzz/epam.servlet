package utils;

import constants.AttributeConstants;
import constants.RegexConstants;
import constants.SQLQueries;
import exceptions.DAOException;
import exceptions.UtilException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.stream.IntStream;

import static constants.AttributeConstants.*;
import static model.dao.DataSource.*;

@Log4j2
public class PaginationUtil {

    // Suppress constructor
    private PaginationUtil() {
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
            closeAll(resultSet, statement);
        }
    }

    public static int[] getPages(int limit, int recordsCount) {
        int maxPages = recordsCount % limit == 0 ? recordsCount / limit : recordsCount / limit + 1;
        return IntStream.range(1, maxPages + 1).toArray();
    }

    public static int getLimit(HttpServletRequest req) {
        return req.getParameter(DISPLAY_RECORDS_NUMBER) == null ?
                DEFAULT_LIMIT :
                Integer.parseInt(req.getParameter(DISPLAY_RECORDS_NUMBER));
    }

    public static int getCurrentPage(HttpServletRequest req) {
        return req.getParameter(CURRENT_PAGE) == null ?
                DEFAULT_PAGE :
                Integer.parseInt(req.getParameter(CURRENT_PAGE));
    }

    public static int getOffset(int limit, int currentPage) {
        if (currentPage < 1) {
            return DEFAULT_OFFSET;
        }
        return limit * (currentPage - 1);
    }

    public static String getSortingType(HttpServletRequest req, Class c) throws UtilException {
        try {
            String className = c.getSimpleName().toUpperCase();
            Class<AttributeConstants> ac = AttributeConstants.class;
            Field sortingField = ac.getDeclaredField(String.format("DEFAULT_%s_SORTING", className));
            return req.getParameter(SORTING_TYPE) == null ?
                    sortingField.get(ac).toString() :
                    req.getParameter(SORTING_TYPE);
        } catch (Exception e) {
            String message = String.format("Can't get default sorting for %s in class %s",
                    c.getName(), AttributeConstants.class);
            log.error(message, e);
            throw new UtilException(message, e);
        }
    }

    public static String getFilter(HttpServletRequest req, String filterName) {
        if (req.getParameter(filterName) != null) {
            return req.getParameter(filterName).equals(NONE_ATTR) ? NONE_ATTR : req.getParameter(filterName);
        }
        return NONE_ATTR;
    }

    public static String getEntityPaginationQuery(String table, Map<String, String[]> filters) {
        return String.format("select * from %s ", table) +
                buildFilters(filters) +
                "order by ? limit ? offset ?";
    }

    private static String buildFilters(Map<String, String[]> filters) {
        String builded = "";
        if (filters != null && !filters.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("where ");

            for (Map.Entry<String, String[]> entry : filters.entrySet()) {
                if (entry.getValue() == null) continue;
                for (String stringValue : entry.getValue()) {
                    String filter = stringValue.matches(RegexConstants.IS_A_NUMBER) ?
                            String.format(String.format("%s = %s and ", entry.getKey(), stringValue)) :
                            String.format("%s = '%s' and ", entry.getKey(), stringValue);
                    sb.append(filter);
                }
            }
            sb.delete(sb.lastIndexOf("and"), sb.length());
            builded = sb.toString();
        }
        return builded;
    }
}
