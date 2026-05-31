package es.uji.ei1027.ei102725gbgs.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;

/**
 * Data Access Object implementation for {@link UsuariOVI} entities.
 * <p>
 * Provides JDBC-based persistence operations for OVI user profiles stored in
 * the database. The primary key type is {@link String}.
 * </p>
 */
@Repository
public class UsuariOVIDaoImpl {

    /** JDBC template used to execute SQL statements against the data source. */
    private JdbcTemplate jdbcTemplate;

    /**
    * Injects the data source and initialises the internal JDBC template.
     *
     * @param dataSource the data source to use; must not be {@code null}
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource));
    }

    /**
     * Adds a new UsuariOVI to the database.
     * @param usuario the UsuariOVI entity to add; must not be {@code null}
     */
    public void addUsuariOVI(UsuariOVI usuario) {
        jdbcTemplate.update(
            "INSERT INTO USUARIO_OVI VALUES(?, ?, ?, ?, ?, ?, ?)",
            usuario.getIdUsuario(),
            usuario.getNombre(),
            usuario.getApellidos(),
            usuario.getEmail(),
            usuario.getPassword(),
            usuario.getTelefono(),
            usuario.isConsentimientoRgpd());
    }

    /**
     * Deletes the UsuariOVI with the given ID from the database.
     * @param idUsuario the ID of the UsuariOVI to delete; must not be {@code null}
     */
    public void deleteUsuariOVIPorId(String idUsuario) {
        jdbcTemplate.update(
            "DELETE FROM USUARIO_OVI WHERE id_usuario = ?",
            idUsuario);
    }

    /**
     * Deletes the UsuariOVI with the given email from the database.
     * @param email the email of the UsuariOVI to delete; must not be {@code null}
     */
    public void deleteUsuariOVIPorEmail(String email) {
        jdbcTemplate.update("DELETE FROM USUARIO_OVI WHERE email = ?", email);
    }

    /**
     * Updates an existing UsuariOVI in the database with the given data.
     * @param usuario the UsuariOVI data to update; must not be {@code null}
     */
    public void updateUsuariOVI(UsuariOVI usuario) {
        jdbcTemplate.update(
            "UPDATE USUARIO_OVI SET nombre = ?, apellidos = ?, email = ?, "
                + "password = ?, telefono = ?, consentimiento_rgpd = ? "
                + "WHERE id_usuario = ?",
            usuario.getNombre(),
            usuario.getApellidos(),
            usuario.getEmail(),
            usuario.getPassword(),
            usuario.getTelefono(),
            usuario.isConsentimientoRgpd(),
            usuario.getIdUsuario());
    }

    /**
     * Retrieves the UsuariOVI with the given ID from the database.
     * @param idUsuario the ID of the UsuariOVI to retrieve; must not be {@code null}
     * @return the UsuariOVI with the given ID, or {@code null} if no such UsuariOVI exists
     */
    public UsuariOVI getUsuariOVI(String idUsuario) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM USUARIO_OVI WHERE id_usuario = ?",
                    new UsuariOVIRowMapper(), idUsuario);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Retrieves a list of all UsuariOVI entities from the database.
     * @return a list of all UsuariOVI entities; never {@code null}
     */
    public List<UsuariOVI> getUsuariosOVI() {
        try {
            return jdbcTemplate.query("SELECT * FROM USUARIO_OVI",
                    new UsuariOVIRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<UsuariOVI>();
        }
    }

    /**
     * Retrieves the UsuariOVI with the given email from the database.
     * @param email the email of the UsuariOVI to retrieve; must not be {@code null}
     * @return the UsuariOVI with the given email, or {@code null} if no such UsuariOVI exists
     */
    public UsuariOVI getUsuariOVIByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM USUARIO_OVI WHERE email = ?",
                    new UsuariOVIRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Checks if a UsuariOVI with the given email exists in the database.
     * @param email the email to check for existence; must not be {@code null}
     * @return {@code true} if a UsuariOVI with the given email exists, {@code false} otherwise
     */
    public boolean existeEmail(String email) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM USUARIO_OVI WHERE email = ?",
                Integer.class, email);
        return count != null && count > 0;
    }
}
