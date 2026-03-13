package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.AssistenciaFormacio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class AssistenciaFormacioRowMapper implements RowMapper<AssistenciaFormacio> {
    @Override
    public AssistenciaFormacio mapRow(ResultSet rs, int rowNum) throws SQLException {
        AssistenciaFormacio assistencia = new AssistenciaFormacio();
        assistencia.setIdAsistencia(rs.getInt("id_asistencia"));
        assistencia.setIdActividad(rs.getInt("id_actividad"));
        assistencia.setIdUsuarioOvi(rs.getString("id_usuario_ovi"));
        assistencia.setIdAsistente(rs.getString("id_asistente"));
        assistencia.setAsistenciaConfirmada(rs.getBoolean("asistencia_confirmada"));
        return assistencia;
    }
}