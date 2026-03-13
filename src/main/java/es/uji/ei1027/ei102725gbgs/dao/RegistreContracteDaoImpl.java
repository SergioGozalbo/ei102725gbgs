package es.uji.ei1027.ei102725gbgs.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.ei102725gbgs.model.RegistreContracte;

/**
 * Data Access Object implementation for {@link RegistreContracte} entities.
 * <p>
 * Provides JDBC-based persistence operations for contract records linked to
 * assistant selections. The primary key type is {@link Integer}.
 * </p>
 */
@Repository
public class RegistreContracteDaoImpl {

    /** JDBC template used to execute SQL statements against the data source. */
    private JdbcTemplate jdbcTemplate;

    /**
     * Injects the data source and initialises the internal {@link JdbcTemplate}.
     *
     * @param dataSource the data source to use; must not be {@code null}
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource));
    }

    // Añadir RegistreContracte usando VALUES(?)
    public void addRegistreContracte(RegistreContracte contracte) {
        jdbcTemplate.update("INSERT INTO REGISTRE_CONTRACTE VALUES(?, ?, ?, ?, ?)",
                contracte.getIdContrato(),
                contracte.getIdSeleccion(),
                contracte.getFechaInicio(),
                contracte.getFechaFin(),
                contracte.getUrlPdf()
        );
    }

    // Borrar por ID de Contrato (Integer)
    public void deleteRegistreContractePorId(int idContrato) {
        jdbcTemplate.update("DELETE FROM REGISTRE_CONTRACTE WHERE id_contrato = ?", idContrato);
    }

    // Borrar por URL del PDF (String)
    public void deleteRegistreContractePorUrl(String urlPdf) {
        jdbcTemplate.update("DELETE FROM REGISTRE_CONTRACTE WHERE url_pdf = ?", urlPdf);
    }

    // Actualizar RegistreContracte
    public void updateRegistreContracte(RegistreContracte contracte) {
        jdbcTemplate.update("UPDATE REGISTRE_CONTRACTE SET id_seleccion = ?, fecha_inicio = ?, fecha_fin = ?, url_pdf = ? WHERE id_contrato = ?",
                contracte.getIdSeleccion(),
                contracte.getFechaInicio(),
                contracte.getFechaFin(),
                contracte.getUrlPdf(),
                contracte.getIdContrato()
        );
    }

    // Obtener un contrato específico
    public RegistreContracte getRegistreContracte(int idContrato) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM REGISTRE_CONTRACTE WHERE id_contrato = ?",
                    new RegistreContracteRowMapper(), idContrato);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Listar todos los contratos
    public List<RegistreContracte> getRegistresContractes() {
        try {
            return jdbcTemplate.query("SELECT * FROM REGISTRE_CONTRACTE", new RegistreContracteRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<RegistreContracte>();
        }
    }
}
