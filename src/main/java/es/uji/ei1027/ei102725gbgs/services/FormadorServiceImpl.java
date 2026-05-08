package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.FormadorDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.Formador;

/**
 * Service class for managing Formador entities.
 */
@Service
public class FormadorServiceImpl {

    /**
     * The DAO used to access the data store to manage {@link Formador}.
     */
    private final FormadorDaoImpl dao;

    /**
     * Creates a new service with the given DAO.
     * @param dao the DAO to use for data access; must not be {@code null}
     */
    @Autowired
    public FormadorServiceImpl(FormadorDaoImpl dao) {
        this.dao = dao;
    }

    /**
     * Returns the Formador with the given ID.
     * @param id the ID of the Formador to retrieve; must not be {@code null}
     * @return the Formador with the given ID, or {@code null} if no such Formador exists
     */
    public Formador getByID(int id) {
        return dao.getFormador(id);
    }

    /**
     * Returns a list of all Formador entities.
     * @return a list of all Formador entities; never {@code null}
     */
    public List<Formador> getAll() {
        return dao.getFormadores();
    }

    /**
     * Adds a new Formador.
     * @param entity the Formador entity to add; must not be {@code null}
     */
    public void addFormador(Formador entity) {
        dao.addFormador(entity);
    }

    /**
     * Updates an existing Formador.
     * @param entity the Formador entity to update; must not be {@code null}
     */
    public void updateFormador(Formador entity) {
        dao.updateFormador(entity);
    }

    /**
     * Deletes the Formador with the given ID.
     * @param id the ID of the Formador to delete; must not be {@code null}
     */
    public void deleteFormadorPorId(int id) {
        dao.deleteFormadorPorId(id);
    }

    /**
     * Deletes the Formador with the given name.
     * @param nombre the name of the Formador to delete; must not be {@code null}
     */
    public void deleteFormadorPorNombre(String nombre) {
        dao.deleteFormadorPorNombre(nombre);
    }
}
