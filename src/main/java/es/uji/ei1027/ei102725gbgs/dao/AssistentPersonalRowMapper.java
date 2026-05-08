package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A row mapper for converting database rows to {@link AssistentPersonal} entities.
 */
public final class AssistentPersonalRowMapper
        implements RowMapper<AssistentPersonal> {
    /**
     * Maps a row from the database result set to an {@link AssistentPersonal} entity.
     *
     * @param rs the result set containing the row data
     * @param rowNum the row number
     * @return the mapped {@link AssistentPersonal} entity
     * @throws SQLException if an error occurs while reading the result set
     */
    @Override
    public AssistentPersonal mapRow(@NonNull ResultSet rs,
            int rowNum) throws SQLException {
        AssistentPersonal asistente = new AssistentPersonal();
        asistente.setIdAsistente(rs.getString("id_asistente"));
        asistente.setNombre(rs.getString("nombre"));
        asistente.setApellidos(rs.getString("apellidos"));
        asistente.setEmail(rs.getString("email"));
        asistente.setPassword(rs.getString("password"));
        asistente.setTelefono(rs.getString("telefono"));
        asistente.setFormacionAcademica(rs.getString("formacion_academica"));
        asistente.setExperiencia(rs.getString("experiencia"));
        asistente.setEstadoAceptado(rs.getString("estado_aceptado"));
        return asistente;
    }
}
