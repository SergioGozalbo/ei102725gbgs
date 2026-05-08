package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.APRequest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A row mapper for converting database rows to {@link APRequest} objects.
 */
public final class APRequestRowMapper implements RowMapper<APRequest> {
    /**
     * Maps a row from the result set to an {@link APRequest} instance.
     * @param rs the result set; must not be {@code null}
     * @param rowNum the row number; must not be negative
     * @return the mapped {@link APRequest} instance; never {@code null}
     * @throws SQLException if an error occurs while accessing the result set
     */
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
