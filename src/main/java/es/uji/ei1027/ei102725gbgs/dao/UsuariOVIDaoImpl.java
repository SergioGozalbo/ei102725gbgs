package es.uji.ei1027.ei102725gbgs.dao;

import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import es.uji.ei1027.ei102725gbgs.utils.database.Dao;

/**
 * Data Access Object implementation for {@link UsuariOVI} entities.
 * <p>
 * Provides JDBC-based persistence operations for OVI user profiles stored in
 * the database. The primary key type is {@link String}.
 * </p>
 */
@Repository
public class UsuariOVIDaoImpl implements Dao<UsuariOVI, String> {

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
     * @param id the user identifier to look up
     * @return the matching {@link UsuariOVI}, or {@code null} if not found
     */
    @Override
    public UsuariOVI getByID(String id) {
        throw new UnsupportedOperationException("Unimplemented method 'getByID'");
    }

    /**
     * {@inheritDoc}
     *
     * @return a list of all {@link UsuariOVI} records
     */
    @Override
    public List<UsuariOVI> getAll() {
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    /**
     * {@inheritDoc}
     *
     * @param entity the {@link UsuariOVI} to persist
     */
    @Override
    public void save(UsuariOVI entity) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    /**
     * {@inheritDoc}
     *
     * @param entity the {@link UsuariOVI} containing updated values
     */
    @Override
    public void update(UsuariOVI entity) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    /**
     * {@inheritDoc}
     *
     * @param id the identifier of the user to update
     */
    @Override
    public void updateByID(String id) {
        throw new UnsupportedOperationException("Unimplemented method 'updateByID'");
    }

    /**
     * {@inheritDoc}
     *
     * @param entity the {@link UsuariOVI} to remove
     */
    @Override
    public void delete(UsuariOVI entity) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    /**
     * {@inheritDoc}
     *
     * @param id the identifier of the user to remove
     */
    @Override
    public void deleteByID(String id) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteByID'");
    }
}
