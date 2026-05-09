package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.RegistreContracteDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.RegistreContracte;

/**
 * Service class for managing RegistreContracte entities.
 */
@Service
public class RegistreContracteServiceImpl {

    /**
     * The DAO used to access the data store to manage {@link RegistreContracte}.
     */
    private final RegistreContracteDaoImpl dao;

    /**
     * Creates a new service with the given DAO.
     * @param dao the DAO to use for data access; must not be {@code null}
     */
    @Autowired
    public RegistreContracteServiceImpl(RegistreContracteDaoImpl dao) {
        this.dao = dao;
    }

    /**
     * Returns the RegistreContracte with the given ID.
     * @param id the ID of the RegistreContracte to retrieve; must not be {@code null}
     * @return the RegistreContracte with the given ID, or {@code null} if no such RegistreContracte exists
     */
    public RegistreContracte getByID(int id) {
        return dao.getRegistreContracte(id);
    }

    /**
     * Returns a list of all RegistreContracte entities.
     * @return a list of all RegistreContracte entities; never {@code null}
     */
    public List<RegistreContracte> getAll() {
        return dao.getRegistresContractes();
    }

    /**
     * Adds a new RegistreContracte.
     * @param entity the RegistreContracte entity to add; must not be {@code null}
     */
    public void addRegistreContracte(RegistreContracte entity) {
        dao.addRegistreContracte(entity);
    }

    /**
     * Updates an existing RegistreContracte.
     * @param entity the RegistreContracte entity to update; must not be {@code null}
     */
    public void updateRegistreContracte(RegistreContracte entity) {
        dao.updateRegistreContracte(entity);
    }

    /**
     * Deletes the RegistreContracte with the given ID.
     * @param id the ID of the RegistreContracte to delete; must not be {@code null}
     */
    public void deleteRegistreContractePorId(int id) {
        dao.deleteRegistreContractePorId(id);
    }

    /**
     * Deletes the RegistreContracte with the given URL.
     * @param urlPdf the URL of the RegistreContracte to delete; must not be {@code null}
     */
    public void deleteRegistreContractePorUrl(String urlPdf) {
        dao.deleteRegistreContractePorUrl(urlPdf);
    }
}
