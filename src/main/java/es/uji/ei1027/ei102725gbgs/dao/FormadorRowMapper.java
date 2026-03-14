package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.Formador;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class FormadorRowMapper implements RowMapper<Formador> {
    @Override
    public Formador mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Formador formador = new Formador();
        formador.setIdFormador(rs.getInt("id_formador"));
        formador.setNombre(rs.getString("nombre"));
        formador.setApellidos(rs.getString("apellidos"));
        formador.setEspecialidad(rs.getString("especialidad"));
        return formador;
    }
}