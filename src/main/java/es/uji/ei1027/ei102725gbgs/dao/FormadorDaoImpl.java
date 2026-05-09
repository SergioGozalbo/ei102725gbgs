package es.uji.ei1027.ei102725gbgs.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.ei102725gbgs.model.Formador;

/**
 * Data Access Object implementation for {@link Formador} entities.
 * <p>
 * Provides JDBC-based persistence operations for trainers registered in the
 * OVI system. The primary key type is {@link Integer}.
 * </p>
 */
@Repository
public class FormadorDaoImpl {

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
     * Adds a new Formador to the database.
     * @param formador the Formador entity to add; must not be {@code null}
     */
    public void addFormador(Formador formador) {
        jdbcTemplate.update(
            "INSERT INTO FORMADOR VALUES(?, ?, ?, ?)",
            formador.getIdFormador(),
            formador.getNombre(),
            formador.getApellidos(),
            formador.getEspecialidad());
    }

    /**
     * Deletes the Formador with the given ID from the database.
     * @param idFormador the ID of the Formador to delete; must not be {@code null}
     */
    public void deleteFormadorPorId(int idFormador) {
        jdbcTemplate.update(
            "DELETE FROM FORMADOR WHERE id_formador = ?",
            idFormador);
    }

    /**
     * Deletes the Formador entities with the given name from the database.
     * @param nombre the name of the Formador entities to delete; must not be {@code null}
     */
    public void deleteFormadorPorNombre(String nombre) {
        jdbcTemplate.update("DELETE FROM FORMADOR WHERE nombre = ?", nombre);
    }

    /**
     * Updates an existing Formador in the database with the given data.
     * @param formador the Formador data to update; must not be {@code null}
     */
    public void updateFormador(Formador formador) {
        jdbcTemplate.update(
            "UPDATE FORMADOR SET nombre = ?, apellidos = ?, "
                + "especialidad = ? WHERE id_formador = ?",
            formador.getNombre(),
            formador.getApellidos(),
            formador.getEspecialidad(),
            formador.getIdFormador());
    }

    /**
     * Retrieves the Formador with the given ID from the database.
     * @param idFormador the ID of the Formador to retrieve; must not be {@code null}
     * @return the Formador with the given ID, or {@code null} if no such Formador exists
     */
    public Formador getFormador(int idFormador) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM FORMADOR WHERE id_formador = ?",
                    new FormadorRowMapper(), idFormador);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Retrieves a list of all Formador entities from the database.
     * @return a list of all Formador entities; never {@code null}
     */
    public List<Formador> getFormadores() {
        try {
            return jdbcTemplate.query("SELECT * FROM FORMADOR",
                    new FormadorRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Formador>();
        }
    }
}
