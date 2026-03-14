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

@RestController
@RequestMapping("/api/assistenciaformacio")
public class AssistenciaFormacioController {

	private final AssistenciaFormacioServiceImpl service;

	@Autowired
	public AssistenciaFormacioController(AssistenciaFormacioServiceImpl service) {
		this.service = service;
	}

	@GetMapping("/{id}")
	public AssistenciaFormacio getByID(@PathVariable int id) {
		return service.getByID(id);
	}

	@GetMapping
	public List<AssistenciaFormacio> getAll() {
		return service.getAll();
	}

	@PostMapping
	public void addAssistenciaFormacio(@RequestBody AssistenciaFormacio entity) {
		service.addAssistenciaFormacio(entity);
	}

	@PutMapping
	public void updateAssistenciaFormacio(@RequestBody AssistenciaFormacio entity) {
		service.updateAssistenciaFormacio(entity);
	}

	@DeleteMapping("/{id}")
	public void deleteAssistenciaFormacioPorId(@PathVariable int id) {
		service.deleteAssistenciaFormacioPorId(id);
	}

	@DeleteMapping("/actividad/{idActividad}")
	public void deleteAssistenciaFormacioPorActividad(@PathVariable int idActividad) {
		service.deleteAssistenciaFormacioPorActividad(idActividad);
	}

}
