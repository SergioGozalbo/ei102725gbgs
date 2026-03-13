package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.ActivitatFormacio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public final class ActivitatFormacioRowMapper implements RowMapper<ActivitatFormacio> {
    @Override
    public ActivitatFormacio mapRow(ResultSet rs, int rowNum) throws SQLException {
        ActivitatFormacio activitat = new ActivitatFormacio();
        activitat.setIdActividad(rs.getInt("id_actividad"));
        activitat.setIdFormador(rs.getInt("id_formador"));
        activitat.setNombre(rs.getString("nombre"));
        activitat.setDescripcion(rs.getString("descripcion"));
        activitat.setTipo(rs.getString("tipo"));

        // Extraer LocalDateTime del timestamp de SQL
        activitat.setFecha(rs.getObject("fecha", LocalDateTime.class));

        // Aforo puede ser nulo en base de datos
        int aforo = rs.getInt("aforo_max");
        activitat.setAforoMax(rs.wasNull() ? null : aforo);

        return activitat;
    }
}