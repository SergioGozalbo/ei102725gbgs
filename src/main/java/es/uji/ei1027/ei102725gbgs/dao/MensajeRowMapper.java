package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.Mensaje;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MensajeRowMapper implements RowMapper<Mensaje> {
    @Override
    public Mensaje mapRow(ResultSet rs, int rowNum) throws SQLException {
        Mensaje m = new Mensaje();
        m.setIdMensaje(rs.getInt("id_mensaje"));
        m.setIdSolicitud(rs.getInt("id_solicitud"));
        m.setRemitenteType(rs.getString("remitente_tipo"));
        m.setRemitenteId(rs.getString("remitente_id"));
        m.setContenido(rs.getString("contenido"));
        m.setFechaEnvio(rs.getTimestamp("fecha_envio").toLocalDateTime());
        return m;
    }
}
