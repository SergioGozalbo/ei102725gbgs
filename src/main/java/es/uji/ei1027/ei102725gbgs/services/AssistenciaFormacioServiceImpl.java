package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.AssistenciaFormacioDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.AssistenciaFormacio;

@Service
public class AssistenciaFormacioServiceImpl {

    private final AssistenciaFormacioDaoImpl dao;

    @Autowired
    public AssistenciaFormacioServiceImpl(AssistenciaFormacioDaoImpl dao) {
        this.dao = dao;
    }

    public AssistenciaFormacio getByID(int id) {
        return dao.getAssistenciaFormacio(id);
    }

    public List<AssistenciaFormacio> getAll() {
        return dao.getAssistenciesFormacio();
    }

    public void addAssistenciaFormacio(AssistenciaFormacio entity) {
        dao.addAssistenciaFormacio(entity);
    }

    public void updateAssistenciaFormacio(AssistenciaFormacio entity) {
        dao.updateAssistenciaFormacio(entity);
    }

    public void deleteAssistenciaFormacioPorId(int id) {
        dao.deleteAssistenciaFormacioPorId(id);
    }

    public void deleteAssistenciaFormacioPorActividad(int idActividad) {
        dao.deleteAssistenciaFormacioPorActividad(idActividad);
    }
}