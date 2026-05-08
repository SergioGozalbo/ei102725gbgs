package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.APRequestDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.APRequest;

@Service
public class APRequestServiceImpl {

    private final APRequestDaoImpl dao;

    @Autowired
    public APRequestServiceImpl(APRequestDaoImpl dao) {
        this.dao = dao;
    }

    public APRequest getByID(int id) {
        return dao.getAPRequest(id);
    }

    public List<APRequest> getAll() {
        return dao.getAPRequests();
    }

    public void addAPRequest(APRequest entity) {
        dao.addAPRequest(entity);
    }

    public void updateAPRequest(APRequest entity) {
        dao.updateAPRequest(entity);
    }

    public void deleteAPRequestPorId(int id) {
        dao.deleteAPRequestPorId(id);
    }

    public void deleteAPRequestPorEstado(String estado) {
        dao.deleteAPRequestPorEstado(estado);
    }

    public List<APRequest> getPendientes() {
        return dao.getAPRequestsByEstado("pendiente");
    }

    public void aprobarSolicitud(int idSolicitud) {
        dao.updateEstado(idSolicitud, "aprobada");
    }
}
