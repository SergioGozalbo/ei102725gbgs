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
    * Injects the data source and initialises the internal JDBC template.
     *
     * @param dataSource the data source to use; must not be {@code null}
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource));
    }

    /**
     * Adds a new AssistentPersonal to the database.
     * @param asistente the AssistentPersonal entity to add; must not be {@code null}
     */
    public void addAssistentPersonal(AssistentPersonal asistente) {
        jdbcTemplate.update(
            "INSERT INTO ASISTENTE_PERSONAL VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
            asistente.getIdAsistente(),
            asistente.getNombre(),
            asistente.getApellidos(),
            asistente.getEmail(),
            asistente.getPassword(),
            asistente.getTelefono(),
            asistente.getFormacionAcademica(),
            asistente.getExperiencia(),
            asistente.getEstadoAceptado());
    }

    /**
     * Deletes the AssistentPersonal with the given ID from the database.
     * @param idAsistente the ID of the AssistentPersonal to delete; must not be {@code null}
     */
    public void deleteAssistentPersonalPorId(String idAsistente) {
        jdbcTemplate.update(
            "DELETE FROM ASISTENTE_PERSONAL WHERE id_asistente = ?",
            idAsistente);
    }

    /**
     * Deletes the AssistentPersonal entities associated with the given acceptance status from the database.
     * @param email the email of the AssistentPersonal entities to delete; must not be {@code null}
     */
    public void deleteAssistentPersonalPorEmail(String email) {
        jdbcTemplate.update(
            "DELETE FROM ASISTENTE_PERSONAL WHERE email = ?",
            email);
    }

    /**
     * Updates an existing AssistentPersonal in the database with the given data.
     * @param asistente the AssistentPersonal data to update; must not be {@code null}
     */
    public void updateAssistentPersonal(AssistentPersonal asistente) {
        jdbcTemplate.update(
            "UPDATE ASISTENTE_PERSONAL SET nombre = ?, apellidos = ?, "
                + "email = ?, password = ?, telefono = ?, "
                + "formacion_academica = ?, experiencia = ?, "
                + "estado_aceptado = ? WHERE id_asistente = ?",
            asistente.getNombre(),
            asistente.getApellidos(),
            asistente.getEmail(),
            asistente.getPassword(),
            asistente.getTelefono(),
            asistente.getFormacionAcademica(),
            asistente.getExperiencia(),
            asistente.getEstadoAceptado(),
            asistente.getIdAsistente());
    }

    /**
     * Retrieves the AssistentPersonal with the given ID from the database.
     * @param idAsistente the ID of the AssistentPersonal to retrieve; must not be {@code null}
     * @return the AssistentPersonal with the given ID, or {@code null} if no such AssistentPersonal exists
     */
    public AssistentPersonal getAssistentPersonal(String idAsistente) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM ASISTENTE_PERSONAL WHERE id_asistente = ?",
                    new AssistentPersonalRowMapper(), idAsistente);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Retrieves a list of all AssistentPersonal entities from the database.
     * @return a list of all AssistentPersonal entities; never {@code null}
     */
    public List<AssistentPersonal> getAssistentsPersonals() {
        try {
            return jdbcTemplate.query("SELECT * FROM ASISTENTE_PERSONAL",
                    new AssistentPersonalRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<AssistentPersonal>();
        }
    }

    /**
     * Retrieves the AssistentPersonal with the given email from the database.
     * @param email the email of the AssistentPersonal to retrieve; must not be {@code null}
     * @return the AssistentPersonal with the given email, or {@code null} if no such AssistentPersonal exists
     */
    public AssistentPersonal getAssistentPersonalByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM ASISTENTE_PERSONAL WHERE email = ?",
                    new AssistentPersonalRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Gets personal assistants filtered by acceptance status.
     *
     * @param estado the acceptance status to filter by
     * @return matching assistants or an empty list if none are found
     */
        public List<AssistentPersonal> getAssistentsPersonalsByEstado(
            String estado) {
        try {
            return jdbcTemplate.query(
                "SELECT * FROM ASISTENTE_PERSONAL WHERE "
                    + "estado_aceptado = ?",
                    new AssistentPersonalRowMapper(), estado);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }
}
