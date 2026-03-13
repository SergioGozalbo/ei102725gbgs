package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class AssistentPersonalRowMapper implements RowMapper<AssistentPersonal> {
    @Override
    public AssistentPersonal mapRow(ResultSet rs, int rowNum) throws SQLException {
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