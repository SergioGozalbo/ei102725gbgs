package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.ActivitatFormacioDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.ActivitatFormacio;

/**
 * Service class for managing ActivitatFormacio entities.
 */
@Service
public class ActivitatFormacioServiceImpl {

    /**
     * The DAO used to access the data store to manage {@link ActivitatFormacio}.
     */
    private final ActivitatFormacioDaoImpl dao;

    /**
     * Creates a new service with the given DAO.
     * @param dao the DAO to use for data access; must not be {@code null}
     */
    @Autowired
    public ActivitatFormacioServiceImpl(ActivitatFormacioDaoImpl dao) {
        this.dao = dao;
    }

    /**
     * Returns the ActivitatFormacio with the given ID.
     * @param id the ID of the ActivitatFormacio to retrieve; must not be {@code null}
     * @return the ActivitatFormacio with the given ID, or {@code null} if no such ActivitatFormacio exists
     */
    public ActivitatFormacio getByID(int id) {
        return dao.getActivitatFormacio(id);
    }

    /**
     * Returns a list of all ActivitatFormacio entities.
     * @return a list of all ActivitatFormacio entities; never {@code null}
     */
    public List<ActivitatFormacio> getAll() {
        return dao.getActivitatsFormacio();
    }

    /**
     * Adds a new ActivitatFormacio based on the given entity data.
     * @param entity the ActivitatFormacio entity to add; must not be {@code null}
     */
    public void addActivitatFormacio(ActivitatFormacio entity) {
        dao.addAportacion(entity);
    }

    /**
     * Updates an existing ActivitatFormacio with the given data.
     * @param entity the ActivitatFormacio data to update; must not be {@code null}
     */
    public void updateActivitatFormacio(ActivitatFormacio entity) {
        dao.updateActivitatFormacio(entity);
    }

    /**
     * Deletes the ActivitatFormacio with the given ID.
     * @param id the ID of the ActivitatFormacio to delete; must not be {@code null}
     */
    public void deleteActivitatFormacioPorId(int id) {
        dao.deleteActivitatFormacioPorId(id);
    }

    /**
     * Deletes the ActivitatFormacio entities associated with the given activity name.
     * @param nombre the name of the activity whose ActivitatFormacio entities to delete; must not be {@code null}
     */
    public void deleteActivitatFormacioPorNombre(String nombre) {
        dao.deleteActivitatFormacioPorNombre(nombre);
    }
}
