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
    * Injects the data source and initialises the internal JDBC template.
     *
     * @param dataSource the data source to use; must not be {@code null}
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource));
    }

    /**
     * Adds a new RegistreContracte to the database.
     * @param contracte the RegistreContracte entity to add; must not be {@code null}
     */
    public void addRegistreContracte(RegistreContracte contracte) {
        jdbcTemplate.update(
            "INSERT INTO REGISTRE_CONTRACTE VALUES(?, ?, ?, ?, ?)",
            contracte.getIdContrato(),
            contracte.getIdSeleccion(),
            contracte.getFechaInicio(),
            contracte.getFechaFin(),
            contracte.getUrlPdf());
    }

    /**
     * Deletes the RegistreContracte with the given ID from the database.
     * @param idContrato the ID of the RegistreContracte to delete; must not be {@code null}
     */
    public void deleteRegistreContractePorId(int idContrato) {
        jdbcTemplate.update(
            "DELETE FROM REGISTRE_CONTRACTE WHERE id_contrato = ?",
            idContrato);
    }

    /**
     * Deletes the RegistreContracte with the given URL from the database.
     * @param urlPdf the URL of the RegistreContracte to delete; must not be {@code null}
     */
    public void deleteRegistreContractePorUrl(String urlPdf) {
        jdbcTemplate.update(
            "DELETE FROM REGISTRE_CONTRACTE WHERE url_pdf = ?",
            urlPdf);
    }

    /**
     * Updates an existing RegistreContracte in the database with the given data.
     * @param contracte the RegistreContracte data to update; must not be {@code null}
     */
    public void updateRegistreContracte(RegistreContracte contracte) {
        jdbcTemplate.update(
            "UPDATE REGISTRE_CONTRACTE SET id_seleccion = ?, "
                + "fecha_inicio = ?, fecha_fin = ?, url_pdf = ? "
                + "WHERE id_contrato = ?",
            contracte.getIdSeleccion(),
            contracte.getFechaInicio(),
            contracte.getFechaFin(),
            contracte.getUrlPdf(),
            contracte.getIdContrato());
    }

    /**
     * Retrieves the RegistreContracte with the given ID from the database.
     * @param idContrato the ID of the RegistreContracte to retrieve; must not be {@code null}
     * @return the RegistreContracte with the given ID, or {@code null} if no such RegistreContracte exists
     */
    public RegistreContracte getRegistreContracte(int idContrato) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM REGISTRE_CONTRACTE WHERE id_contrato = ?",
                    new RegistreContracteRowMapper(), idContrato);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Retrieves a list of all RegistreContracte entities from the database.
     * @return a list of all RegistreContracte entities; never {@code null}
     */
    public List<RegistreContracte> getRegistresContractes() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM REGISTRE_CONTRACTE",
                    new RegistreContracteRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<RegistreContracte>();
        }
    }
    /**
     * Retrieves the RegistreContracte linked to a given selection.
     * @param idSeleccion the selection identifier
     * @return the contract or null if none exists
     */
    public RegistreContracte getRegistreContracteBySeleccion(int idSeleccion) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM REGISTRE_CONTRACTE WHERE id_seleccion = ?",
                    new RegistreContracteRowMapper(), idSeleccion);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
