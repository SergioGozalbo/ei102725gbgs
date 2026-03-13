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
     * Injects the data source and initialises the internal {@link JdbcTemplate}.
     *
     * @param dataSource the data source to use; must not be {@code null}
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource));
    }

    // Añadir APRequest usando VALUES(?)
    public void addAPRequest(APRequest apRequest) {
        jdbcTemplate.update("INSERT INTO APRequest VALUES(?, ?, ?, ?, ?, ?)",
                apRequest.getIdSolicitud(),
                apRequest.getIdUsuarioOvi(),
                apRequest.getEstado(),
                apRequest.getTipoAsistencia(),
                apRequest.getPreferencias(),
                apRequest.getProximidad()
        );
    }

    // Borrar por ID (Integer)
    public void deleteAPRequestPorId(int idSolicitud) {
        jdbcTemplate.update("DELETE FROM APRequest WHERE id_solicitud = ?", idSolicitud);
    }

    // Borrar por Estado (String)
    public void deleteAPRequestPorEstado(String estado) {
        jdbcTemplate.update("DELETE FROM APRequest WHERE estado = ?", estado);
    }

    // Actualizar APRequest
    public void updateAPRequest(APRequest apRequest) {
        jdbcTemplate.update("UPDATE APRequest SET id_usuario_ovi = ?, estado = ?, tipo_asistencia = ?, preferencias = ?, proximidad = ? WHERE id_solicitud = ?",
                apRequest.getIdUsuarioOvi(),
                apRequest.getEstado(),
                apRequest.getTipoAsistencia(),
                apRequest.getPreferencias(),
                apRequest.getProximidad(),
                apRequest.getIdSolicitud()
        );
    }

    // Obtener una sola solicitud
    public APRequest getAPRequest(int idSolicitud) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM APRequest WHERE id_solicitud = ?",
                    new APRequestRowMapper(), idSolicitud);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Listar todas las solicitudes
    public List<APRequest> getAPRequests() {
        try {
            return jdbcTemplate.query("SELECT * FROM APRequest", new APRequestRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<APRequest>();
        }
    }
}
