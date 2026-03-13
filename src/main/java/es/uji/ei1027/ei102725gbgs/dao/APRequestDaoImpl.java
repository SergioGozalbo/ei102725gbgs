package es.uji.ei1027.ei102725gbgs.dao;

import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.ei102725gbgs.model.APRequest;
import es.uji.ei1027.ei102725gbgs.utils.database.Dao;

/**
 * Data Access Object implementation for {@link APRequest} entities.
 * <p>
 * Provides JDBC-based persistence operations for personal assistance requests
 * stored in the database. The primary key type is {@link Integer}.
 * </p>
 */
@Repository
public class APRequestDaoImpl implements Dao<APRequest, Integer> {

    /** JDBC template used to execute SQL statements against the data source. */
    private JdbcTemplate jdbcTemplate;

    /**
     * Injects the data source and initialises the internal {@link JdbcTemplate}.
     *
     * @param dataSource the data source to use; must not be {@code null}
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource));
    }

    /**
     * {@inheritDoc}
     *
     * @param id the request identifier to look up
     * @return the matching {@link APRequest}, or {@code null} if not found
     */
    @Override
    public APRequest getByID(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'getByID'");
    }

    /**
     * {@inheritDoc}
     *
     * @return a list of all {@link APRequest} records
     */
    @Override
    public List<APRequest> getAll() {
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    /**
     * {@inheritDoc}
     *
     * @param entity the {@link APRequest} to persist
     */
    @Override
    public void save(APRequest entity) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    /**
     * {@inheritDoc}
     *
     * @param entity the {@link APRequest} containing updated values
     */
    @Override
    public void update(APRequest entity) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    /**
     * {@inheritDoc}
     *
     * @param id the identifier of the request to update
     */
    @Override
    public void updateByID(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'updateByID'");
    }

    /**
     * {@inheritDoc}
     *
     * @param entity the {@link APRequest} to remove
     */
    @Override
    public void delete(APRequest entity) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    /**
     * {@inheritDoc}
     *
     * @param id the identifier of the request to remove
     */
    @Override
    public void deleteByID(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteByID'");
    }
}
