package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.AssistentPersonalDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
import java.util.concurrent.atomic.AtomicInteger;
import es.uji.ei1027.ei102725gbgs.utils.request.AssistentPersonalRequest;

/**
 * Service class for managing AssistentPersonal entities.
 */
@Service
public class AssistentPersonalServiceImpl {

    /**
     * A simple counter to generate unique IDs for new AssistentPersonal entities.
     */
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    /**
     * The DAO used to access the data store to manage {@link AssistentPersonal}.
     */
    private final AssistentPersonalDaoImpl dao;

    /**
     * Creates a new service with the given DAO.
     * @param dao the DAO to use for data access; must not be {@code null}
     */
    @Autowired
    public AssistentPersonalServiceImpl(AssistentPersonalDaoImpl dao) {
        this.dao = dao;
    }

    /**
     * Returns the AssistentPersonal with the given ID.
     * @param id the ID of the AssistentPersonal to retrieve; must not be {@code null}
     * @return the AssistentPersonal with the given ID, or {@code null} if no such AssistentPersonal exists
     */
    public AssistentPersonal getByID(String id) {
        return dao.getAssistentPersonal(id);
    }

    /**
     * Returns a list of all AssistentPersonal entities.
     * @return a list of all AssistentPersonal entities; never {@code null}
     */
    public List<AssistentPersonal> getAll() {
        return dao.getAssistentsPersonals();
    }

    /**
     * Adds a new AssistentPersonal based on the given request data.
     * @param entity the request data for the new AssistentPersonal; must not be {@code null}
     */
    public void addAssistentPersonal(AssistentPersonalRequest entity) {
        int newId = COUNTER.getAndIncrement();
        String generatedId = String.format("A%03d", newId);

        AssistentPersonal assistant = new AssistentPersonal();
        assistant.setIdAsistente(generatedId);
        assistant.setNombre(entity.getNombre());
        assistant.setApellidos(entity.getApellidos());
        assistant.setEmail(entity.getEmail());
        assistant.setPassword(entity.getPassword());
        assistant.setTelefono(entity.getTelefono());
        assistant.setFormacionAcademica(entity.getFormacionAcademica());
        assistant.setExperiencia(entity.getExperiencia());
        assistant.setEstadoAceptado(entity.getEstadoAceptado());

        dao.addAssistentPersonal(assistant);
    }

    /**
     * Updates an existing AssistentPersonal with the given data.
     * @param entity the AssistentPersonal data to update; must not be {@code null}
     */
    public void updateAssistentPersonal(AssistentPersonal entity) {
        dao.updateAssistentPersonal(entity);
    }

    /**
     * Deletes the AssistentPersonal with the given email.
     * @param email the email of the AssistentPersonal to delete; must not be {@code null}
     */
    public void deleteAssistentPersonalPorEmail(String email) {
        dao.deleteAssistentPersonalPorEmail(email);
    }

    /**
     * Deletes the AssistentPersonal with the given ID.
     * @param id the ID of the AssistentPersonal to delete; must not be {@code null}
     */
    public void deleteAssistentPersonalPorId(String id) {
        dao.deleteAssistentPersonalPorId(id);
    }
}
