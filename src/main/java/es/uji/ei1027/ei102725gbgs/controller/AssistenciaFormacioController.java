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

import es.uji.ei1027.ei102725gbgs.model.AssistenciaFormacio;
import es.uji.ei1027.ei102725gbgs.services.AssistenciaFormacioServiceImpl;

/**
 * Controller class for managing AssistenciaFormacio entities.
 */
@RestController
@RequestMapping("/api/assistenciaformacio")
public class AssistenciaFormacioController {
	/**
	 * Service used by this controller to perform operations on AssistenciaFormacio entities.
	 */
    private final AssistenciaFormacioServiceImpl service;

	/**
	 * Constructor for AssistenciaFormacioController.
	 * @param service the service implementation to be used by this controller
	 */
    @Autowired
    public AssistenciaFormacioController(AssistenciaFormacioServiceImpl service) {

                this.service = service;
    }

	/**
	 * Endpoint to retrieve an AssistenciaFormacio by its ID.
	 * @param id the ID of the AssistenciaFormacio to retrieve
	 * @return the AssistenciaFormacio with the specified ID, or null if not found
	 */
    @GetMapping("/{id}")
    public AssistenciaFormacio getByID(@PathVariable int id) {
        return service.getByID(id);
    }

	/**
	 * Endpoint to retrieve all AssistenciaFormacio entities.
	 * @return a list of all AssistenciaFormacio entities
	 */
    @GetMapping("/all")
    public List<AssistenciaFormacio> getAll() {
        return service.getAll();
    }

	/**
	 * Endpoint to create a new AssistenciaFormacio.
	 * @param entity the AssistenciaFormacio entity to be created, provided in the request body
	 */
    @PostMapping("/create")
    public void addAssistenciaFormacio(
        @RequestBody AssistenciaFormacio entity) {
        service.addAssistenciaFormacio(entity);
    }

	/**
	 * Endpoint to update an existing AssistenciaFormacio.
	 * @param entity the AssistenciaFormacio entity with updated information, provided in the request body
	 */
    @PutMapping("/update")
    public void updateAssistenciaFormacio(
            @RequestBody AssistenciaFormacio entity) {
        service.updateAssistenciaFormacio(entity);
    }

	/**
	 * Endpoint to delete an AssistenciaFormacio by its ID.
	 * @param id the ID of the AssistenciaFormacio to be deleted
	 */
    @DeleteMapping("/{id}")
    public void deleteAssistenciaFormacioPorId(@PathVariable int id) {
        service.deleteAssistenciaFormacioPorId(id);
    }

	/**
	 * Endpoint to delete AssistenciaFormacio entities by the associated activity ID.
	 * @param idActividad the ID of the activity for which to delete AssistenciaFormacio entities
	 */
    @DeleteMapping("/activity/{idActividad}")
    public void deleteAssistenciaFormacioPorActividad(
            @PathVariable int idActividad) {
        service.deleteAssistenciaFormacioPorActividad(idActividad);
    }
}
