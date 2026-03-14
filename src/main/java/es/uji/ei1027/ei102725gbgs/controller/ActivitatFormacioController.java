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

@RestController
@RequestMapping("/api/activitatformacio")
public class ActivitatFormacioController {

	private final ActivitatFormacioServiceImpl service;

	@Autowired
	public ActivitatFormacioController(ActivitatFormacioServiceImpl service) {
		this.service = service;
	}

	@GetMapping("/{id}")
	public ActivitatFormacio getByID(@PathVariable int id) {
		return service.getByID(id);
	}

	@GetMapping("/all")
	public List<ActivitatFormacio> getAll() {
		return service.getAll();
	}

	@PostMapping("/create")
	public void addActivitatFormacio(@RequestBody ActivitatFormacio entity) {
		service.addActivitatFormacio(entity);
	}

	@PutMapping("/update")
	public void updateActivitatFormacio(@RequestBody ActivitatFormacio entity) {
		service.updateActivitatFormacio(entity);
	}

	@DeleteMapping("/{id}")
	public void deleteActivitatFormacioPorId(@PathVariable int id) {
		service.deleteActivitatFormacioPorId(id);
	}

	@DeleteMapping("/name/{nombre}")
	public void deleteActivitatFormacioPorNombre(@PathVariable String nombre) {
		service.deleteActivitatFormacioPorNombre(nombre);
	}
}
