package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.AssistenciaFormacio;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A row mapper for converting database rows to {@link AssistenciaFormacio} objects.
 */
public final class AssistenciaFormacioRowMapper
        implements RowMapper<AssistenciaFormacio> {
    /**
     * Maps a row from the result set to an {@link AssistenciaFormacio} instance.
     * @param rs the result set; must not be {@code null}
     * @param rowNum the row number; must not be negative
     * @return the mapped {@link AssistenciaFormacio} instance; never {@code null}
     * @throws SQLException if an error occurs while accessing the result set
     */
    @Override
    public AssistenciaFormacio mapRow(@NonNull ResultSet rs,
            int rowNum) throws SQLException {
        AssistenciaFormacio assistencia = new AssistenciaFormacio();
        assistencia.setIdAsistencia(rs.getInt("id_asistencia"));
        assistencia.setIdActividad(rs.getInt("id_actividad"));
        assistencia.setIdUsuarioOvi(rs.getString("id_usuario_ovi"));
        assistencia.setIdAsistente(rs.getString("id_asistente"));
        assistencia.setAsistenciaConfirmada(
            rs.getBoolean("asistencia_confirmada"));
        return assistencia;
    }
}
