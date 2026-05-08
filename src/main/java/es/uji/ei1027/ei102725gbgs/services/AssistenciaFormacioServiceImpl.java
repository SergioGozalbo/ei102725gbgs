package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.AssistenciaFormacioDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.AssistenciaFormacio;

/**
 * Service class for managing AssistenciaFormacio entities.
 */
@Service
public class AssistenciaFormacioServiceImpl {

    /**
     * The DAO used to access the data store to manage {@link AssistenciaFormacio}.
     */
    private final AssistenciaFormacioDaoImpl dao;

    /**
     * Creates a new service with the given DAO.
     * @param dao the DAO to use for data access; must not be {@code null}
     */
    @Autowired
    public AssistenciaFormacioServiceImpl(AssistenciaFormacioDaoImpl dao) {
        this.dao = dao;
    }

    /**
     * Returns the AssistenciaFormacio with the given ID.
     * @param id the ID of the AssistenciaFormacio to retrieve; must not be {@code null}
     * @return the AssistenciaFormacio with the given ID, or {@code null} if no such AssistenciaFormacio exists
     */
    public AssistenciaFormacio getByID(int id) {
        return dao.getAssistenciaFormacio(id);
    }

    /**
     * Returns a list of all AssistenciaFormacio entities.
     * @return a list of all AssistenciaFormacio entities; never {@code null}
     */
    public List<AssistenciaFormacio> getAll() {
        return dao.getAssistenciesFormacio();
    }

    /**
     * Adds a new AssistenciaFormacio based on the given entity data.
     * @param entity the AssistenciaFormacio entity to add; must not be {@code null}
     */
    public void addAssistenciaFormacio(AssistenciaFormacio entity) {
        dao.addAssistenciaFormacio(entity);
    }

    /**
     * Updates an existing AssistenciaFormacio with the given data.
     * @param entity the AssistenciaFormacio data to update; must not be {@code null}
     */
    public void updateAssistenciaFormacio(AssistenciaFormacio entity) {
        dao.updateAssistenciaFormacio(entity);
    }

    /**
     * Deletes the AssistenciaFormacio with the given ID.
     * @param id the ID of the AssistenciaFormacio to delete; must not be {@code null}
     */
    public void deleteAssistenciaFormacioPorId(int id) {
        dao.deleteAssistenciaFormacioPorId(id);
    }

    /**
     * Deletes the AssistenciaFormacio entities associated with the given activity ID.
     * @param idActividad the ID of the activity whose AssistenciaFormacio entities to delete; must not be {@code null}
     */
    public void deleteAssistenciaFormacioPorActividad(int idActividad) {
        dao.deleteAssistenciaFormacioPorActividad(idActividad);
    }
}
