package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.FormadorDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.Formador;

@Service
public class FormadorServiceImpl {

    private final FormadorDaoImpl dao;

    @Autowired
    public FormadorServiceImpl(FormadorDaoImpl dao) {
        this.dao = dao;
    }

    public Formador getByID(int id) {
        return dao.getFormador(id);
    }

    public List<Formador> getAll() {
        return dao.getFormadores();
    }

    public void addFormador(Formador entity) {
        dao.addFormador(entity);
    }

    public void updateFormador(Formador entity) {
        dao.updateFormador(entity);
    }

    public void deleteFormadorPorId(int id) {
        dao.deleteFormadorPorId(id);
    }

    public void deleteFormadorPorNombre(String nombre) {
        dao.deleteFormadorPorNombre(nombre);
    }
}