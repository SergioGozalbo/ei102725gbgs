package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.RegistreContracteDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.RegistreContracte;

@Service
public class RegistreContracteServiceImpl {

    private final RegistreContracteDaoImpl dao;

    @Autowired
    public RegistreContracteServiceImpl(RegistreContracteDaoImpl dao) {
        this.dao = dao;
    }

    public RegistreContracte getByID(int id) {
        return dao.getRegistreContracte(id);
    }

    public List<RegistreContracte> getAll() {
        return dao.getRegistresContractes();
    }

    public void addRegistreContracte(RegistreContracte entity) {
        dao.addRegistreContracte(entity);
    }

    public void updateRegistreContracte(RegistreContracte entity) {
        dao.updateRegistreContracte(entity);
    }

    public void deleteRegistreContractePorId(int id) {
        dao.deleteRegistreContractePorId(id);
    }

    public void deleteRegistreContractePorUrl(String urlPdf) {
        dao.deleteRegistreContractePorUrl(urlPdf);
    }
}