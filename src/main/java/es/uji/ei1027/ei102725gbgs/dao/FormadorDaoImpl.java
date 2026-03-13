package es.uji.ei1027.ei102725gbgs.dao;

import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.ei102725gbgs.model.Formador;
import es.uji.ei1027.ei102725gbgs.utils.database.Dao;

/**
 * Data Access Object implementation for {@link Formador} entities.
 * <p>
 * Provides JDBC-based persistence operations for trainers registered in the
 * OVI system. The primary key type is {@link Integer}.
 * </p>
 */
@Repository
public class FormadorDaoImpl implements Dao<Formador, Integer> {

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
     * @param id the trainer identifier to look up
     * @return the matching {@link Formador}, or {@code null} if not found
     */
    @Override
    public Formador getByID(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'getByID'");
    }

    /**
     * {@inheritDoc}
     *
     * @return a list of all {@link Formador} records
     */
    @Override
    public List<Formador> getAll() {
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    /**
     * {@inheritDoc}
     *
     * @param entity the {@link Formador} to persist
     */
    @Override
    public void save(Formador entity) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    /**
     * {@inheritDoc}
     *
     * @param entity the {@link Formador} containing updated values
     */
    @Override
    public void update(Formador entity) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    /**
     * {@inheritDoc}
     *
     * @param id the identifier of the trainer to update
     */
    @Override
    public void updateByID(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'updateByID'");
    }

    /**
     * {@inheritDoc}
     *
     * @param entity the {@link Formador} to remove
     */
    @Override
    public void delete(Formador entity) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    /**
     * {@inheritDoc}
     *
     * @param id the identifier of the trainer to remove
     */
    @Override
    public void deleteByID(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteByID'");
    }
}
