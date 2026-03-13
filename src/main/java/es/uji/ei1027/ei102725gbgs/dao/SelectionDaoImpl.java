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
     * Injects the data source and initialises the internal {@link JdbcTemplate}.
     *
     * @param dataSource the data source to use; must not be {@code null}
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource));
    }

    // Añadir Selection usando VALUES(?)
    public void addSelection(Selection selection) {
        jdbcTemplate.update("INSERT INTO SELECCION VALUES(?, ?, ?)",
                selection.getIdSeleccion(),
                selection.getIdSolicitud(),
                selection.getIdAsistente()
        );
    }

    // Borrar por ID de Selección (Integer)
    public void deleteSelectionPorId(int idSeleccion) {
        jdbcTemplate.update("DELETE FROM SELECCION WHERE id_seleccion = ?", idSeleccion);
    }

    // Borrar por ID de Asistente (String)
    public void deleteSelectionPorAsistente(String idAsistente) {
        jdbcTemplate.update("DELETE FROM SELECCION WHERE id_asistente = ?", idAsistente);
    }

    // Actualizar Selection
    public void updateSelection(Selection selection) {
        jdbcTemplate.update("UPDATE SELECCION SET id_solicitud = ?, id_asistente = ? WHERE id_seleccion = ?",
                selection.getIdSolicitud(),
                selection.getIdAsistente(),
                selection.getIdSeleccion()
        );
    }

    // Obtener una selección específica
    public Selection getSelection(int idSeleccion) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM SELECCION WHERE id_seleccion = ?",
                    new SelectionRowMapper(), idSeleccion);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Listar todas las selecciones
    public List<Selection> getSelections() {
        try {
            return jdbcTemplate.query("SELECT * FROM SELECCION", new SelectionRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Selection>();
        }
    }
}
