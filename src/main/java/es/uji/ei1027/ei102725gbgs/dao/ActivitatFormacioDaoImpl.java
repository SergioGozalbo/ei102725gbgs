package es.uji.ei1027.ei102725gbgs.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.ei102725gbgs.model.ActivitatFormacio;

/**
 * Data Access Object implementation for {@link ActivitatFormacio} entities.
 * <p>
 * Provides JDBC-based persistence operations for training and outreach
 * activities stored in the database. The primary key type is {@link Integer}.
 * </p>
 */
@Repository
public class ActivitatFormacioDaoImpl {

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

    ///Añadir Aportacion
    public void addAportacion(ActivitatFormacio actFormacio) {
        jdbcTemplate.update("INSERT INTO ACTIVITAT_FORMACIO VALUES(?, ?, ?, ?, ?)", actFormacio.getIdActividad(), actFormacio.getIdFormador(), actFormacio.getNombre(), actFormacio.getTipo(), actFormacio.getFecha());

    }

    // Borrar por ID (Integer)
    public void deleteActivitatFormacioPorId(int idActividad) {
        jdbcTemplate.update("DELETE FROM ACTIVITAT_FORMACIO WHERE id_actividad = ?", idActividad);
    }

    // Borrar por Nombre (String)
    public void deleteActivitatFormacioPorNombre(String nombre) {
        jdbcTemplate.update("DELETE FROM ACTIVITAT_FORMACIO WHERE nombre = ?", nombre);
    }

    // Actualizar ActivitatFormacio
    public void updateActivitatFormacio(ActivitatFormacio actFormacio) {
        jdbcTemplate.update("UPDATE ACTIVITAT_FORMACIO SET id_formador = ?, nombre = ?, descripcion = ?, tipo = ?, fecha = ?, aforo_max = ? WHERE id_actividad = ?",
                actFormacio.getIdFormador(),
                actFormacio.getNombre(),
                actFormacio.getDescripcion(),
                actFormacio.getTipo(),
                actFormacio.getFecha(),
                actFormacio.getAforoMax(),
                actFormacio.getIdActividad()
        );
    }

    // Obtener una sola actividad
    public ActivitatFormacio getActivitatFormacio(int idActividad) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM ACTIVITAT_FORMACIO WHERE id_actividad = ?",
                    new ActivitatFormacioRowMapper(), idActividad);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Listar todas las actividades
    public List<ActivitatFormacio> getActivitatsFormacio() {
        try {
            return jdbcTemplate.query("SELECT * FROM ACTIVITAT_FORMACIO", new ActivitatFormacioRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<ActivitatFormacio>();
        }
    }
}
