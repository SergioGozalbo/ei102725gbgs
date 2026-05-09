package es.uji.ei1027.ei102725gbgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import java.util.concurrent.atomic.AtomicInteger;
import es.uji.ei1027.ei102725gbgs.utils.request.UsuariOVIRequest;
import io.micrometer.common.lang.NonNull;

/**
 * Service class for managing UsuariOVI entities.
 */
@Service
public class UsuariOVIServiceImpl {
    /**
     * The DAO used to access the data store to manage {@link ActivitatFormacio}.
     */
    private final UsuariOVIDaoImpl dao;

    /**
     * A simple counter to generate unique IDs.
     */
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    /**
     * Creates a new service with the given DAO.
     * @param dao the DAO to use for data access; must not be {@code null}
     */
    @Autowired
    public UsuariOVIServiceImpl(UsuariOVIDaoImpl dao) {
        this.dao = dao;
    }

    /**
     * Returns the OVI user with the given ID.
     * @param id the ID of the OVI user to retrieve; must not be {@code null}
     * @return the OVI user with the given ID, or {@code null} if no such user exists
     */
    public UsuariOVI getByID(String id) {
        return dao.getUsuariOVI(id);
    }

    /**
     * Returns a list of all OVI users.
     * @return a list of all OVI users; never {@code null}
     */
    public List<UsuariOVI> getAll() {

        return dao.getUsuariosOVI();
    }

    /**
     * Adds a new OVI user based on the given request data.
     * @param entity the request data for the new OVI user; must not be {@code null}
     */
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

    /**
     * Updates an existing OVI user with the given data.
     * @param entity the OVI user data to update; must not be {@code null}
     */
    public void updateUsuariOVI(UsuariOVI entity) {
        dao.updateUsuariOVI(entity);
    }

    /**
     * Deletes the OVI user with the given email.
     * @param email the email of the OVI user to delete; must not be {@code null}
     */
    public void deleteUsuariOVIPorEmail(@NonNull String email) {
        dao.deleteUsuariOVIPorEmail(email);
    }

    /**
     * Deletes the OVI user with the given ID.
     * @param id the ID of the OVI user to delete; must not be {@code null}
     */
    public void deleteUsuariOVIPorId(@NonNull String id) {
        dao.deleteUsuariOVIPorId(id);
    }
}
