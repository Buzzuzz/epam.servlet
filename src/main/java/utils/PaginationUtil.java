package utils;

import constants.AttributeConstants;
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
import java.util.StringJoiner;
import java.util.stream.IntStream;

import static constants.AttributeConstants.*;
import static model.dao.DataSource.*;

@Log4j2
public class PaginationUtil {

    // Suppress constructor
    private PaginationUtil() {
    }

    public static int getRecordsCount(Connection con, String countable, String table) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String temp = SQLQueries.COUNT_RECORDS;
            temp = temp.replaceFirst("\\?", countable);
            temp = temp.replaceFirst("\\?", table);
            statement = con.prepareStatement(temp);

            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (Exception e) {
            log.error("Can't count records in table " + table + " by " + countable + " field", e);
            throw new DAOException("Can't count records in table " + table + " by " + countable + " field", e);
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

    public static String getEntityPaginationQuery(String table, Map<String, String> filters) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("select * from %s ", table));
        if (!filters.isEmpty()) {
            sb.append("where ");
            filters.forEach((k, v) -> sb.append(String.format("%s = %s, ", k, v)));
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        sb.append(" order by ? limit ? offset ?");

        return sb.toString();
    }
}
