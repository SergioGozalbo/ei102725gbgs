package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.Selection;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A row mapper for converting database rows to {@link Selection} entities.
 */
public final class SelectionRowMapper implements RowMapper<Selection> {
    /**
     * Maps a row from the database result set to a {@link Selection} entity.
     *
     * @param rs the result set containing the row data
     * @param rowNum the row number
     * @return the mapped {@link Selection} entity
     * @throws SQLException if an error occurs while reading the result set
     */
    @Override
    public Selection mapRow(@NonNull ResultSet rs,
            int rowNum) throws SQLException {
        Selection selection = new Selection();
        selection.setIdSeleccion(rs.getInt("id_seleccion"));
        selection.setIdSolicitud(rs.getInt("id_solicitud"));
        selection.setIdAsistente(rs.getString("id_asistente"));
        return selection;
    }
}
