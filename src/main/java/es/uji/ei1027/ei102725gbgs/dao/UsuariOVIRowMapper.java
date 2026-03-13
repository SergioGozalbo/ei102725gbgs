package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class UsuariOVIRowMapper implements RowMapper<UsuariOVI> {
    @Override
    public UsuariOVI mapRow(ResultSet rs, int rowNum) throws SQLException {
        UsuariOVI usuario = new UsuariOVI();
        usuario.setIdUsuario(rs.getString("id_usuario"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPassword(rs.getString("password"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setConsentimientoRgpd(rs.getBoolean("consentimiento_rgpd"));
        usuario.setEstadoAprobacion(rs.getString("estado_aprobacion"));
        return usuario;
    }
}