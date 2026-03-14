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
     * Injects the data source and initialises the internal {@link JdbcTemplate}.
     *
     * @param dataSource the data source to use; must not be {@code null}
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource));
    }

    // Añadir UsuariOVI usando VALUES(?)
    public void addUsuariOVI(UsuariOVI usuario) {
        jdbcTemplate.update("INSERT INTO USUARIO_OVI VALUES(?, ?, ?, ?, ?, ?, ?)",
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getTelefono(),
                usuario.isConsentimientoRgpd()
        );
    }

    // Borrar por ID (String)
    public void deleteUsuariOVIPorId(String idUsuario) {
        jdbcTemplate.update("DELETE FROM USUARIO_OVI WHERE id_usuario = ?", idUsuario);
    }

    // Borrar por Email (String)
    public void deleteUsuariOVIPorEmail(String email) {
        jdbcTemplate.update("DELETE FROM USUARIO_OVI WHERE email = ?", email);
    }

    // Actualizar UsuariOVI
    public void updateUsuariOVI(UsuariOVI usuario) {
        jdbcTemplate.update("UPDATE USUARIO_OVI SET nombre = ?, apellidos = ?, email = ?, password = ?, telefono = ?, consentimiento_rgpd = ?, WHERE id_usuario = ?",
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getTelefono(),
                usuario.isConsentimientoRgpd(),
                usuario.getIdUsuario()
        );
    }

    // Obtener un usuario por su ID
    public UsuariOVI getUsuariOVI(String idUsuario) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM USUARIO_OVI WHERE id_usuario = ?",
                    new UsuariOVIRowMapper(), idUsuario);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Listar todos los usuarios OVI
    public List<UsuariOVI> getUsuariosOVI() {
        try {
            return jdbcTemplate.query("SELECT * FROM USUARIO_OVI", new UsuariOVIRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<UsuariOVI>();
        }
    }
}
