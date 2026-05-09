package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.RegistreContracte;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * A row mapper for converting database rows to {@link RegistreContracte} entities.
 */
public final class RegistreContracteRowMapper
        implements RowMapper<RegistreContracte> {
    /**
     * Maps a row from the database result set to a {@link RegistreContracte} entity.
     *
     * @param rs the result set containing the row data
     * @param rowNum the row number
     * @return the mapped {@link RegistreContracte} entity
     * @throws SQLException if an error occurs while reading the result set
     */
    @Override
    public RegistreContracte mapRow(@NonNull ResultSet rs,
            int rowNum) throws SQLException {
        RegistreContracte contracte = new RegistreContracte();
        contracte.setIdContrato(rs.getInt("id_contrato"));
        contracte.setIdSeleccion(rs.getInt("id_seleccion"));
        contracte.setFechaInicio(rs.getObject("fecha_inicio", LocalDate.class));
        contracte.setFechaFin(rs.getObject("fecha_fin", LocalDate.class));
        contracte.setUrlPdf(rs.getString("url_pdf"));
        return contracte;
    }
}
