package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.SelectionDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.Selection;

@Service
public class SelectionServiceImpl {

    private final SelectionDaoImpl dao;

    @Autowired
    public SelectionServiceImpl(SelectionDaoImpl dao) {
        this.dao = dao;
    }

    public Selection getByID(int id) {
        return dao.getSelection(id);
    }

    public List<Selection> getAll() {
        return dao.getSelections();
    }

    public void addSelection(Selection entity) {
        dao.addSelection(entity);
    }

    public void updateSelection(Selection entity) {
        dao.updateSelection(entity);
    }

    public void deleteSelectionPorId(int id) {
        dao.deleteSelectionPorId(id);
    }

    public void deleteSelectionPorAsistente(String idAsistente) {
        dao.deleteSelectionPorAsistente(idAsistente);
    }
}
