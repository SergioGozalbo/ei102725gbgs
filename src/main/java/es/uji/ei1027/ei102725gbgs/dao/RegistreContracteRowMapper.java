package es.uji.ei1027.ei102725gbgs.dao;

import es.uji.ei1027.ei102725gbgs.model.RegistreContracte;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class RegistreContracteRowMapper
        implements RowMapper<RegistreContracte> {
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
