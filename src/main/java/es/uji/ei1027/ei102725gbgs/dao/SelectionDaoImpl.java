package es.uji.ei1027.ei102725gbgs.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.ei102725gbgs.model.Selection;

/**
 * Data Access Object implementation for {@link Selection} entities.
 * <p>
 * Provides JDBC-based persistence operations for assistant selections linked
 * to personal assistance requests. The primary key type is {@link Integer}.
 * </p>
 */
@Repository
public class SelectionDaoImpl {

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
     * Adds a new Selection to the database.
     * @param selection the Selection entity to add; must not be {@code null}
     */
    public void addSelection(Selection selection) {
        jdbcTemplate.update(
            "INSERT INTO SELECCION VALUES(?, ?, ?)",
            selection.getIdSeleccion(),
            selection.getIdSolicitud(),
            selection.getIdAsistente());
    }

    /**
     * Deletes the Selection with the given ID from the database.
     * @param idSeleccion the ID of the Selection to delete; must not be {@code null}
     */
    public void deleteSelectionPorId(int idSeleccion) {
        jdbcTemplate.update(
            "DELETE FROM SELECCION WHERE id_seleccion = ?",
            idSeleccion);
    }

    /**
     * Deletes the Selection with the given assistant ID from the database.
     * @param idAsistente the assistant ID of the Selection to delete; must not be {@code null}
     */
    public void deleteSelectionPorAsistente(String idAsistente) {
        jdbcTemplate.update(
            "DELETE FROM SELECCION WHERE id_asistente = ?",
            idAsistente);
    }

    /**
     * Deletes the Selection with the given request ID from the database.
     * @param selection the request ID of the Selection to delete; must not be {@code null}
     */
    public void updateSelection(Selection selection) {
        jdbcTemplate.update(
            "UPDATE SELECCION SET id_solicitud = ?, id_asistente = ? "
                + "WHERE id_seleccion = ?",
            selection.getIdSolicitud(),
            selection.getIdAsistente(),
            selection.getIdSeleccion());
    }

    /**
     * Retrieves the Selection with the given ID from the database.
     * @param idSeleccion the ID of the Selection to retrieve; must not be {@code null}
     * @return the Selection with the given ID, or {@code null} if no such Selection exists
     */
    public Selection getSelection(int idSeleccion) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM SELECCION WHERE id_seleccion = ?",
                    new SelectionRowMapper(), idSeleccion);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Retrieves a list of all Selection entities from the database.
     * @return a list of all Selection entities; never {@code null}
     */
    public List<Selection> getSelections() {
        try {
            return jdbcTemplate.query("SELECT * FROM SELECCION",
                    new SelectionRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Selection>();
        }
    }

    /**
     * Retrieves a list of Selection entities associated with the given request ID.
     * @param idSolicitud the request ID to filter by; must not be {@code null}
     * @return a list of Selection entities associated with the given request ID; never {@code null}
     */
    public List<Selection> getSelectionsBySolicitud(int idSolicitud) {
        return jdbcTemplate.query("SELECT * FROM SELECCION WHERE id_solicitud = ?",
                new SelectionRowMapper(), idSolicitud);
    }

    /**
     * Retrieves a list of Selection entities associated with the given assistant ID.
     * @param idAsistente the assistant ID to filter by; must not be {@code null}
     * @return a list of Selection entities associated with the given assistant ID; never {@code null}
     */
    public List<Selection> getSelectionsByAsistente(String idAsistente) {
        return jdbcTemplate.query("SELECT * FROM SELECCION WHERE id_asistente = ?",
                new SelectionRowMapper(), idAsistente);
    }

    /**
     * Deletes the Selection associated with the given request ID and assistant ID from the database.
     * @param idSolicitud the request ID of the Selection to delete; must not be {@code null}
     * @param idAsistente the assistant ID of the Selection to delete; must not be {@code null}
     */
    public void deleteSelection(int idSolicitud, String idAsistente) {
        jdbcTemplate.update("DELETE FROM SELECCION WHERE id_solicitud = ? AND id_asistente = ?",
                idSolicitud, idAsistente);
    }
}
