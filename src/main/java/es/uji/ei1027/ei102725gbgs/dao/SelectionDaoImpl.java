package es.uji.ei1027.ei102725gbgs.dao;

import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.ei102725gbgs.model.Selection;
import es.uji.ei1027.ei102725gbgs.utils.database.Dao;

/**
 * Data Access Object implementation for {@link Selection} entities.
 * <p>
 * Provides JDBC-based persistence operations for assistant selections linked
 * to personal assistance requests. The primary key type is {@link Integer}.
 * </p>
 */
@Repository
public class SelectionDaoImpl implements Dao<Selection, Integer> {

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
     * @param id the selection identifier to look up
     * @return the matching {@link Selection}, or {@code null} if not found
     */
    @Override
    public Selection getByID(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'getByID'");
    }

    /**
     * {@inheritDoc}
     *
     * @return a list of all {@link Selection} records
     */
    @Override
    public List<Selection> getAll() {
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    /**
     * {@inheritDoc}
     *
     * @param entity the {@link Selection} to persist
     */
    @Override
    public void save(Selection entity) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    /**
     * {@inheritDoc}
     *
     * @param entity the {@link Selection} containing updated values
     */
    @Override
    public void update(Selection entity) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    /**
     * {@inheritDoc}
     *
     * @param id the identifier of the selection to update
     */
    @Override
    public void updateByID(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'updateByID'");
    }

    /**
     * {@inheritDoc}
     *
     * @param entity the {@link Selection} to remove
     */
    @Override
    public void delete(Selection entity) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    /**
     * {@inheritDoc}
     *
     * @param id the identifier of the selection to remove
     */
    @Override
    public void deleteByID(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteByID'");
    }
}
