package es.uji.ei1027.ei102725gbgs.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;

/**
 * Data Access Object implementation for {@link AssistentPersonal} entities.
 * <p>
 * Provides JDBC-based persistence operations for personal assistants
 * registered in the OVI system. The primary key type is {@link String}.
 * </p>
 */
@Repository
public class AssistentPersonalDaoImpl {

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

    // Añadir AssistentPersonal usando VALUES(?)
    public void addAssistentPersonal(AssistentPersonal asistente) {
        jdbcTemplate.update("INSERT INTO ASISTENTE_PERSONAL VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                asistente.getIdAsistente(),
                asistente.getNombre(),
                asistente.getApellidos(),
                asistente.getEmail(),
                asistente.getPassword(),
                asistente.getTelefono(),
                asistente.getFormacionAcademica(),
                asistente.getExperiencia(),
                asistente.getEstadoAceptado()
        );
    }

    // Borrar por ID (String)
    public void deleteAssistentPersonalPorId(String idAsistente) {
        jdbcTemplate.update("DELETE FROM ASISTENTE_PERSONAL WHERE id_asistente = ?", idAsistente);
    }

    // Borrar por Email (String)
    public void deleteAssistentPersonalPorEmail(String email) {
        jdbcTemplate.update("DELETE FROM ASISTENTE_PERSONAL WHERE email = ?", email);
    }

    // Actualizar AssistentPersonal
    public void updateAssistentPersonal(AssistentPersonal asistente) {
        jdbcTemplate.update("UPDATE ASISTENTE_PERSONAL SET nombre = ?, apellidos = ?, email = ?, password = ?, telefono = ?, formacion_academica = ?, experiencia = ?, estado_aceptado = ? WHERE id_asistente = ?",
                asistente.getNombre(),
                asistente.getApellidos(),
                asistente.getEmail(),
                asistente.getPassword(),
                asistente.getTelefono(),
                asistente.getFormacionAcademica(),
                asistente.getExperiencia(),
                asistente.getEstadoAceptado(),
                asistente.getIdAsistente()
        );
    }

    // Obtener un asistente por su ID
    public AssistentPersonal getAssistentPersonal(String idAsistente) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM ASISTENTE_PERSONAL WHERE id_asistente = ?",
                    new AssistentPersonalRowMapper(), idAsistente);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Listar todos los asistentes
    public List<AssistentPersonal> getAssistentsPersonals() {
        try {
            return jdbcTemplate.query("SELECT * FROM ASISTENTE_PERSONAL", new AssistentPersonalRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<AssistentPersonal>();
        }
    }
}
