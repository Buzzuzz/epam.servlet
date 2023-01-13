package utils;

import constants.SQLQueries;
import exceptions.DAOException;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static model.dao.DataSource.*;

@Log4j2
public class CountRecordsUtil {

    // Suppress constructor
    private CountRecordsUtil() {
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
}
