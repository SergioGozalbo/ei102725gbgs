package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.SelectionDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.Selection;

/**
 * Service class for managing Selection entities.
 */
@Service
public class SelectionServiceImpl {

    /**
     * The DAO used to access the data store to manage {@link Selection}.
     */
    private final SelectionDaoImpl dao;

    /**
     * Creates a new service with the given DAO.
     * @param dao the DAO to use for data access; must not be {@code null}
     */
    @Autowired
    public SelectionServiceImpl(SelectionDaoImpl dao) {
        this.dao = dao;
    }

    /**
     * Returns the Selection with the given ID.
     * @param id the ID of the Selection to retrieve; must not be {@code null}
     * @return the Selection with the given ID, or {@code null} if no such Selection exists
     */
    public Selection getByID(int id) {
        return dao.getSelection(id);
    }

    /**
     * Returns a list of all Selections.
     * @return a list of all Selections; never {@code null}
     */
    public List<Selection> getAll() {
        return dao.getSelections();
    }

    /**
     * Adds a new Selection based on the given entity data.
     * @param entity the Selection entity to add; must not be {@code null}
     */
    public void addSelection(Selection entity) {
        dao.addSelection(entity);
    }

    /**
     * Updates an existing Selection in the database with the given data.
     * @param entity the Selection data to update; must not be {@code null}
     */
    public void updateSelection(Selection entity) {
        dao.updateSelection(entity);
    }

    /**
     * Deletes the Selection with the given ID from the database.
     * @param id the ID of the Selection to delete; must not be {@code null}
     */
    public void deleteSelectionPorId(int id) {
        dao.deleteSelectionPorId(id);
    }

    /**
     * Deletes the Selection with the given assistant ID from the database.
     * @param idAsistente the assistant ID of the Selection to delete; must not be {@code null}
     */
    public void deleteSelectionPorAsistente(String idAsistente) {
        dao.deleteSelectionPorAsistente(idAsistente);
    }
}
