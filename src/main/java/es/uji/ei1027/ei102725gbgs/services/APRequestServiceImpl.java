package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.APRequestDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.APRequest;

@Service
public class APRequestServiceImpl {

    /**
     * The DAO used to access the data store to manage {@link APRequest}.
     */
    private final APRequestDaoImpl dao;

    /**
     * Creates a new service with the given DAO.
     * @param dao the DAO to use for data access; must not be {@code null}
     */
    @Autowired
    public APRequestServiceImpl(APRequestDaoImpl dao) {
        this.dao = dao;
    }

    /**
     * Returns the APRequest with the given ID.
     * @param id the ID of the APRequest to retrieve; must not be {@code null}
     * @return the APRequest with the given ID, or {@code null} if no such APRequest exists
     */
    public APRequest getByID(int id) {
        return dao.getAPRequest(id);
    }

    /**
     * Returns a list of all APRequests.
     * @return a list of all APRequests; never {@code null}
     */
    public List<APRequest> getAll() {
        return dao.getAPRequests();
    }

    /**
     * Adds a new APRequest based on the given entity data.
     * @param entity the APRequest entity to add; must not be {@code null}
     */
    public void addAPRequest(APRequest entity) {
        dao.addAPRequest(entity);
    }

    /**
     * Updates an existing APRequest in the database with the given data.
     * @param entity the APRequest data to update; must not be {@code null}
     */
    public void updateAPRequest(APRequest entity) {
        dao.updateAPRequest(entity);
    }

    /**
     * Deletes the APRequest with the given ID from the database.
     * @param id the ID of the APRequest to delete; must not be {@code null}
     */
    public void deleteAPRequestPorId(int id) {
        dao.deleteAPRequestPorId(id);
    }

    /**
     * Deletes the APRequest with the given estado from the database.
     * @param estado the estado of the APRequest to delete; must not be {@code null}
     */
    public void deleteAPRequestPorEstado(String estado) {
        dao.deleteAPRequestPorEstado(estado);
    }

    /**
     * Returns a list of all pending APRequests.
     * @return a list of all pending APRequests; never {@code null}
     */
    public List<APRequest> getPendientes() {
        return dao.getAPRequestsByEstado("pendiente");
    }

    /**
     * Approves the APRequest with the given ID by updating its estado to "aprobada".
     * @param idSolicitud the ID of the APRequest to approve; must not be {@code null}
     */
    public void aprobarSolicitud(int idSolicitud) {
        dao.updateEstado(idSolicitud, "aprobada");
    }
}
