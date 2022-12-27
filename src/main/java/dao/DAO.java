package dao;

import java.util.Collection;
import java.util.Optional;

/**
 * DAO interface with basic CRUD operations for database tables.
 * @param <T> One of {@link entities}, for which CRUD operations will be implemented.
 */
public interface DAO<T> {
    /**
     * Get method to retrieve specified entity from {@link entities} package from database table
     * @param id Field by which will be committed search in database table.
     * @return {@link Optional} result of such entry in database table.
     */
    Optional<T> get(long id);

    /**
     * Get method to retrieve all {@link entities} from database table
     * @return {@link Collection} of database entries
     */
    Collection<T> getAll();

    /**
     * Update method to update specific database entry
     * @param entity Specified entity from {@link entities} package, from which data will be obtained
     *               and then updated.
     * @return Number of affected rows
     */
    long update(T entity);

    /**
     * Delete method to remove specific database entry
     * @param id ID of entity to be removed from table
     * @return ID of deleted entity
     */
    long delete(long id);

    /**
     * Create method to add entry to database table
     * @param entity Specified entity from {@link entities} package to be saved in database table
     * @return Generated entity's ID
     */
    long save(T entity);

}
