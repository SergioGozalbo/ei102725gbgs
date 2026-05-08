package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import java.util.concurrent.atomic.AtomicInteger;
import es.uji.ei1027.ei102725gbgs.utils.request.UsuariOVIRequest;
import io.micrometer.common.lang.NonNull;

@Service
public class UsuariOVIServiceImpl {
    /**
     * The DAO used to access the data store to manage {@link ActivitatFormacio}
     */
    private final UsuariOVIDaoImpl dao;

    /**
     * A simple counter to generate unique IDs.
     */
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    /** Creates a new instance with the given DAO. */
    @Autowired
    public UsuariOVIServiceImpl(UsuariOVIDaoImpl dao) {
        this.dao = dao;
    }

    public UsuariOVI getByID(String id) {
        return dao.getUsuariOVI(id);
    }

    public List<UsuariOVI> getAll() {

        return dao.getUsuariosOVI();
    }

    public void addUsuariOVI(UsuariOVIRequest entity) {

        int newId = COUNTER.getAndIncrement();
        String generatedId = String.format("U%03d", newId);

        UsuariOVI ent = new UsuariOVI();
        ent.setIdUsuario(generatedId);
        ent.setNombre(entity.getNombre());
        ent.setApellidos(entity.getApellidos());
        ent.setEmail(entity.getEmail());
        ent.setPassword(entity.getPassword());
        ent.setTelefono(entity.getTelefono());
        ent.setConsentimientoRgpd(entity.isConsentimientoRgpd());

        dao.addUsuariOVI(ent);
    }

    public void updateUsuariOVI(UsuariOVI entity) {
        dao.updateUsuariOVI(entity);
    }

    public void deleteUsuariOVIPorEmail(@NonNull String email) {
        dao.deleteUsuariOVIPorEmail(email);
    }

    public void deleteUsuariOVIPorId(@NonNull String id) {
        dao.deleteUsuariOVIPorId(id);
    }
}
