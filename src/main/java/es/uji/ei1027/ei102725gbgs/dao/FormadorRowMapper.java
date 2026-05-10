package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.Formador;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A row mapper for converting database rows to {@link Formador} entities.
 */
public final class FormadorRowMapper implements RowMapper<Formador> {
    /**
     * Maps a row from the database result set to a {@link Formador} entity.
     *
     * @param rs the result set containing the row data
     * @param rowNum the row number
     * @return the mapped {@link Formador} entity
     * @throws SQLException if an error occurs while reading the result set
     */
    @Override
    public Formador mapRow(@NonNull ResultSet rs,
            int rowNum) throws SQLException {
        Formador formador = new Formador();
        formador.setIdFormador(rs.getInt("id_formador"));
        formador.setNombre(rs.getString("nombre"));
        formador.setApellidos(rs.getString("apellidos"));
        formador.setEspecialidad(rs.getString("especialidad"));
        return formador;
    }
}
