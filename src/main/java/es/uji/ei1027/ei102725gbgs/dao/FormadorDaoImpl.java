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
     * Injects the data source and initialises the internal {@link JdbcTemplate}.
     *
     * @param dataSource the data source to use; must not be {@code null}
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource));
    }

    // Añadir Formador usando VALUES(?)
    public void addFormador(Formador formador) {
        jdbcTemplate.update("INSERT INTO FORMADOR VALUES(?, ?, ?, ?)",
                formador.getIdFormador(),
                formador.getNombre(),
                formador.getApellidos(),
                formador.getEspecialidad()
        );
    }

    // Borrar por ID (Integer)
    public void deleteFormadorPorId(int idFormador) {
        jdbcTemplate.update("DELETE FROM FORMADOR WHERE id_formador = ?", idFormador);
    }

    // Borrar por Nombre (String)
    public void deleteFormadorPorNombre(String nombre) {
        jdbcTemplate.update("DELETE FROM FORMADOR WHERE nombre = ?", nombre);
    }

    // Actualizar Formador
    public void updateFormador(Formador formador) {
        jdbcTemplate.update("UPDATE FORMADOR SET nombre = ?, apellidos = ?, especialidad = ? WHERE id_formador = ?",
                formador.getNombre(),
                formador.getApellidos(),
                formador.getEspecialidad(),
                formador.getIdFormador()
        );
    }

    // Obtener un formador por su ID
    public Formador getFormador(int idFormador) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM FORMADOR WHERE id_formador = ?",
                    new FormadorRowMapper(), idFormador);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Listar todos los formadores
    public List<Formador> getFormadores() {
        try {
            return jdbcTemplate.query("SELECT * FROM FORMADOR", new FormadorRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Formador>();
        }
    }
}
