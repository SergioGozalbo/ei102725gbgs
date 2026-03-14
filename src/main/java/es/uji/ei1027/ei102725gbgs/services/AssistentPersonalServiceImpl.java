package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.AssistentPersonalDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
import es.uji.ei1027.ei102725gbgs.utils.request.AssistentPersonalRequest;

@Service
public class AssistentPersonalServiceImpl {

    private final AssistentPersonalDaoImpl dao;

    private static int counter = 0;

    @Autowired
    public AssistentPersonalServiceImpl(AssistentPersonalDaoImpl dao) {
        this.dao = dao;
    }

    public AssistentPersonal getByID(String id) {
        return dao.getAssistentPersonal(id);
    }

    public List<AssistentPersonal> getAll() {
        return dao.getAssistentsPersonals();
    }

    public void addAssistentPersonal(AssistentPersonalRequest entity) {
        int newId = counter++;
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

    public void updateAssistentPersonal(AssistentPersonal entity) {
        dao.updateAssistentPersonal(entity);
    }

    public void deleteAssistentPersonalPorEmail(String email) {
        dao.deleteAssistentPersonalPorEmail(email);
    }

    public void deleteAssistentPersonalPorId(String id) {
        dao.deleteAssistentPersonalPorId(id);
    }
}