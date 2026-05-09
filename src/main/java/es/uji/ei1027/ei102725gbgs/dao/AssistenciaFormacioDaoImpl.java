package es.uji.ei1027.ei102725gbgs.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.ei102725gbgs.model.AssistenciaFormacio;

/**
 * Data Access Object implementation for {@link AssistenciaFormacio} entities.
 * <p>
 * Provides JDBC-based persistence operations for attendance records linked to
 * training activities. The primary key type is {@link Integer}.
 * </p>
 */
@Repository
public class AssistenciaFormacioDaoImpl {

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
     * Adds a new AssistenciaFormacio to the database.
     * @param assistencia the AssistenciaFormacio entity to add; must not be {@code null}
     */
    public void addAssistenciaFormacio(AssistenciaFormacio assistencia) {
        jdbcTemplate.update(
            "INSERT INTO ASSISTENCIA_FORMACIO VALUES(?, ?, ?, ?, ?)",
            assistencia.getIdAsistencia(),
            assistencia.getIdActividad(),
            assistencia.getIdUsuarioOvi(),
            assistencia.getIdAsistente(),
            assistencia.isAsistenciaConfirmada());
    }

    /**
     * Deletes the AssistenciaFormacio with the given ID from the database.
     * @param idAsistencia the ID of the AssistenciaFormacio to delete; must not be {@code null}
     */
    public void deleteAssistenciaFormacioPorId(int idAsistencia) {
        jdbcTemplate.update(
            "DELETE FROM ASSISTENCIA_FORMACIO WHERE id_asistencia = ?",
            idAsistencia);
    }

    /**
     * Deletes the AssistenciaFormacio entities associated with the given actividad ID from the database.
     * @param idActividad the ID of the actividad whose AssistenciaFormacio entities to delete; must not be {@code null}
     */
    public void deleteAssistenciaFormacioPorActividad(int idActividad) {
        jdbcTemplate.update(
            "DELETE FROM ASSISTENCIA_FORMACIO WHERE id_actividad = ?",
            idActividad);
    }

    /**
     * Updates an existing AssistenciaFormacio in the database with the given data.
     * @param assistencia the AssistenciaFormacio data to update; must not be {@code null}
     */
    public void updateAssistenciaFormacio(AssistenciaFormacio assistencia) {
        jdbcTemplate.update(
            "UPDATE ASSISTENCIA_FORMACIO SET id_actividad = ?, "
                + "id_usuario_ovi = ?, id_asistente = ?, "
                + "asistencia_confirmada = ? WHERE id_asistencia = ?",
            assistencia.getIdActividad(),
            assistencia.getIdUsuarioOvi(),
            assistencia.getIdAsistente(),
            assistencia.isAsistenciaConfirmada(),
            assistencia.getIdAsistencia());
    }

    /**
     * Retrieves the AssistenciaFormacio with the given ID from the database.
     * @param idAsistencia the ID of the AssistenciaFormacio to retrieve; must not be {@code null}
     * @return the AssistenciaFormacio with the given ID, or {@code null} if no such AssistenciaFormacio exists
     */
    public AssistenciaFormacio getAssistenciaFormacio(int idAsistencia) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM ASSISTENCIA_FORMACIO WHERE "
                        + "id_asistencia = ?",
                    new AssistenciaFormacioRowMapper(), idAsistencia);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Retrieves a list of all AssistenciaFormacio entities from the database.
     * @return a list of all AssistenciaFormacio entities; never {@code null}
     */
    public List<AssistenciaFormacio> getAssistenciesFormacio() {
        try {
            return jdbcTemplate.query("SELECT * FROM ASSISTENCIA_FORMACIO",
                    new AssistenciaFormacioRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<AssistenciaFormacio>();
        }
    }
}
