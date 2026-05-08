package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.Selection;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class SelectionRowMapper implements RowMapper<Selection> {
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
