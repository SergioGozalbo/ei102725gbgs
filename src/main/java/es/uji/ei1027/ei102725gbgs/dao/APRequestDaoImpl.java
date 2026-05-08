package es.uji.ei1027.ei102725gbgs.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.ei102725gbgs.model.APRequest;

/**
 * Data Access Object implementation for {@link APRequest} entities.
 * <p>
 * Provides JDBC-based persistence operations for personal assistance requests
 * stored in the database. The primary key type is {@link Integer}.
 * </p>
 */
@Repository
public class APRequestDaoImpl {

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
     * Adds a new APRequest to the database.
     * @param apRequest the APRequest entity to add; must not be {@code null}
     */
    public void addAPRequest(APRequest apRequest) {
        jdbcTemplate.update(
            "INSERT INTO AP_REQUEST VALUES(?, ?, ?, ?, ?, ?)",
            apRequest.getIdSolicitud(),
            apRequest.getIdUsuarioOvi(),
            apRequest.getEstado(),
            apRequest.getTipoAsistencia(),
            apRequest.getPreferencias(),
            apRequest.getProximidad());
    }

    /**
     * Deletes the APRequest with the given ID from the database.
     * @param idSolicitud the ID of the APRequest to delete; must not be {@code null}
     */
    public void deleteAPRequestPorId(int idSolicitud) {
        jdbcTemplate.update(
            "DELETE FROM AP_REQUEST WHERE id_solicitud = ?",
            idSolicitud);
    }

    /**
     * Deletes the APRequest entities associated with the given estado from the database.
     * @param estado the state of the APRequest entities to delete; must not be {@code null}
     */
    public void deleteAPRequestPorEstado(String estado) {
        jdbcTemplate.update("DELETE FROM AP_REQUEST WHERE estado = ?", estado);
    }

    /**
     * Updates an existing APRequest in the database with the given data.
     * @param apRequest the APRequest data to update; must not be {@code null}
     */
    public void updateAPRequest(APRequest apRequest) {
        jdbcTemplate.update(
            "UPDATE AP_REQUEST SET id_usuario_ovi = ?, estado = ?, "
                + "tipo_asistencia = ?, preferencias = ?, proximidad = ? "
                + "WHERE id_solicitud = ?",
            apRequest.getIdUsuarioOvi(),
            apRequest.getEstado(),
            apRequest.getTipoAsistencia(),
            apRequest.getPreferencias(),
            apRequest.getProximidad(),
            apRequest.getIdSolicitud());
    }

    /**
     * Retrieves the APRequest with the given ID from the database.
     * @param idSolicitud the ID of the APRequest to retrieve; must not be {@code null}
     * @return the APRequest with the given ID, or {@code null} if no such APRequest exists
     */
    public APRequest getAPRequest(int idSolicitud) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM AP_REQUEST WHERE id_solicitud = ?",
                    new APRequestRowMapper(), idSolicitud);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Retrieves a list of all APRequest entities from the database.
     * @return a list of all APRequest entities; never {@code null}
     */
    public List<APRequest> getAPRequests() {
        try {
            return jdbcTemplate.query("SELECT * FROM AP_REQUEST",
                    new APRequestRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<APRequest>();
        }
    }

    /**
     * Retrieves a list of APRequest entities with the given estado from the database.
     * @param estado the state of the APRequest entities to retrieve; must not be {@code null}
     * @return a list of APRequest entities with the given estado; never {@code null}
     */
    public List<APRequest> getAPRequestsByEstado(String estado) {
        try {
            return jdbcTemplate.query(
                "SELECT * FROM AP_REQUEST WHERE estado = ?",
                new APRequestRowMapper(), estado);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Updates the estado of the APRequest with the given ID in the database.
     * @param idSolicitud the ID of the APRequest to update; must not be {@code null}
     * @param nuevoEstado the new estado to set; must not be {@code null}
     */
    public void updateEstado(int idSolicitud, String nuevoEstado) {
        jdbcTemplate.update(
                "UPDATE AP_REQUEST SET estado = ? WHERE id_solicitud = ?",
                nuevoEstado, idSolicitud);
    }

    /**
     * Retrieves a list of APRequest entities associated with the given OVI user ID from the database.
     * @param idUsuarioOvi the ID of the OVI user whose APRequest entities to retrieve; must not be {@code null}
     * @return a list of APRequest entities associated with the given OVI user ID; never {@code null}
     */
    public List<APRequest> getAPRequestsByUsuari(String idUsuarioOvi) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM AP_REQUEST WHERE id_usuario_ovi = ?",
                    new APRequestRowMapper(), idUsuarioOvi);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }
}
