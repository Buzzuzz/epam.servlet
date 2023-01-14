package model.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * DAO interface with basic CRUD operations for database tables.
 *
 * @param <T> One of {@link model.entities entities}, for which CRUD operations will be implemented.
 */
public interface DAO<T> {
    /**
     * Get method to retrieve specified entity from {@link model.entities entities} package from database table
     *
     * @param id  Field by which will be committed search in database table.
     * @param con {@link Connection} on which operation to be done
     * @return {@link Optional} result of such entry in database table.
     */
    Optional<T> get(Connection con, long id);

    /**
     * Get method to retrieve all {@link model.entities entities} from database table
     *
     * @param con {@link Connection} on which operation to be done
     * @return {@link Collection} of database entries
     */
    Collection<T> getAll(Connection con, int limit, int offset, String sorting, Map<String, String> filters);

    /**
     * Update method to update specific database entry
     *
     * @param entity Specified entity from {@link model.entities entities} package, from which data will be obtained
     *               and then updated.
     * @param con    {@link Connection} on which operation to be done
     * @return Number of affected rows
     */
    long update(Connection con, T entity);

    /**
     * Delete method to remove specific database entry
     *
     * @param id  ID of entity to be removed from table
     * @param con {@link Connection} on which operation to be done
     * @return ID of deleted entity
     */
    long delete(Connection con, long id);

    /**
     * Create method to add entry to database table
     *
     * @param entity Specified entity from {@link model.entities entities} package to be saved in database table
     * @param con    {@link Connection} on which operation to be done
     * @return Generated entity's ID
     */
    long save(Connection con, T entity);

}
