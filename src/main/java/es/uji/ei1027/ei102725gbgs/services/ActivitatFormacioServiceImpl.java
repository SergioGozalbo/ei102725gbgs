package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.ActivitatFormacioDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.ActivitatFormacio;

@Service
public class ActivitatFormacioServiceImpl {

    private final ActivitatFormacioDaoImpl dao;

    @Autowired
    public ActivitatFormacioServiceImpl(ActivitatFormacioDaoImpl dao) {
        this.dao = dao;
    }

    public ActivitatFormacio getByID(int id) {
        return dao.getActivitatFormacio(id);
    }

    public List<ActivitatFormacio> getAll() {
        return dao.getActivitatsFormacio();
    }

    public void addActivitatFormacio(ActivitatFormacio entity) {
        dao.addAportacion(entity);
    }

    public void updateActivitatFormacio(ActivitatFormacio entity) {
        dao.updateActivitatFormacio(entity);
    }

    public void deleteActivitatFormacioPorId(int id) {
        dao.deleteActivitatFormacioPorId(id);
    }

    public void deleteActivitatFormacioPorNombre(String nombre) {
        dao.deleteActivitatFormacioPorNombre(nombre);
    }
}