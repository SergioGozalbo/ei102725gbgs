package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** DAO for Mensaje entities. */
@Repository
public class MensajeDaoImpl {

    /**
     * JDBC template used to execute SQL statements.
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Injects the data source and initializes the JDBC template.
     * @param dataSource the data source to use; must not be {@code null}
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource));
    }

    /**
     * Inserts a new message.
     * @param mensaje message to insert
     */
    public void addMensaje(Mensaje mensaje) {
        jdbcTemplate.update(
                "INSERT INTO MENSAJE "
                        + "(id_solicitud, remitente_tipo, remitente_id, "
                        + "contenido, fecha_envio) VALUES (?, ?, ?, ?, ?)",
                mensaje.getIdSolicitud(),
                mensaje.getRemitenteType(),
                mensaje.getRemitenteId(),
                mensaje.getContenido(),
                java.time.LocalDateTime.now());
    }

    /**
     * Returns all messages for a given request, ordered by date.
     * @param idSolicitud request identifier
     * @return list of messages
     */
    public List<Mensaje> getMensajesBySolicitud(int idSolicitud) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM MENSAJE WHERE id_solicitud = ? "
                            + "ORDER BY fecha_envio ASC",
                    new MensajeRowMapper(), idSolicitud);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }
}
