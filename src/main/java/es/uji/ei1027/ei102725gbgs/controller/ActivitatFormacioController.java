package es.uji.ei1027.ei102725gbgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uji.ei1027.ei102725gbgs.model.ActivitatFormacio;
import es.uji.ei1027.ei102725gbgs.services.ActivitatFormacioServiceImpl;

/**
 * Controller class for managing ActivitatFormacio entities.
 * It provides RESTful endpoints for CRUD operations on ActivitatFormacio.
 */
@RestController
@RequestMapping("/api/activitatformacio")
public class ActivitatFormacioController {

    /**
     * Service used by this controller to perform operations on ActivitatFormacio entities.
     */
    private final ActivitatFormacioServiceImpl service;

    /**
     * Creates a new controller.
     * @param service service used by this controller
     */
    @Autowired
    public ActivitatFormacioController(ActivitatFormacioServiceImpl service) {
        this.service = service;
    }

    /**
     * Returns one activity.
     * @param id activity identifier
     * @return the activity
     */
    @GetMapping("/{id}")
    public ActivitatFormacio getByID(@PathVariable int id) {
        return service.getByID(id);
    }

    /**
     * Returns all activities.
     * @return the activity list
     */
    @GetMapping("/all")
    public List<ActivitatFormacio> getAll() {
        return service.getAll();
    }

    /**
     * Creates an activity.
     * @param entity activity data
     */
    @PostMapping("/create")
    public void addActivitatFormacio(@RequestBody ActivitatFormacio entity) {
        service.addActivitatFormacio(entity);
    }

    /**
     * Updates an activity.
     * @param entity activity data
     */
    @PutMapping("/update")
    public void updateActivitatFormacio(@RequestBody ActivitatFormacio entity) {
        service.updateActivitatFormacio(entity);
    }

    /**
     * Deletes an activity by ID.
     * @param id activity identifier
     */
    @DeleteMapping("/{id}")
    public void deleteActivitatFormacioPorId(@PathVariable int id) {
        service.deleteActivitatFormacioPorId(id);
    }

    /**
     * Deletes an activity by name.
     * @param nombre activity name
     */
    @DeleteMapping("/name/{nombre}")
    public void deleteActivitatFormacioPorNombre(@PathVariable String nombre) {
        service.deleteActivitatFormacioPorNombre(nombre);
    }
}
