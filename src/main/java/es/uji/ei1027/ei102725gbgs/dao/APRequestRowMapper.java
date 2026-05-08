package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.APRequest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class APRequestRowMapper implements RowMapper<APRequest> {
    @Override
    public APRequest mapRow(@NonNull ResultSet rs,
            int rowNum) throws SQLException {
        APRequest request = new APRequest();
        request.setIdSolicitud(rs.getInt("id_solicitud"));
        request.setIdUsuarioOvi(rs.getString("id_usuario_ovi"));
        request.setEstado(rs.getString("estado"));
        request.setTipoAsistencia(rs.getString("tipo_asistencia"));
        request.setPreferencias(rs.getString("preferencias"));
        request.setProximidad(rs.getString("proximidad"));
        return request;
    }
}
