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

import es.uji.ei1027.ei102725gbgs.model.Formador;
import es.uji.ei1027.ei102725gbgs.services.FormadorServiceImpl;

@RestController
@RequestMapping("/api/formador")
public class FormadorController {

	private final FormadorServiceImpl service;

	@Autowired
	public FormadorController(FormadorServiceImpl service) {
		this.service = service;
	}

	@GetMapping("/{id}")
	public Formador getByID(@PathVariable int id) {
		return service.getByID(id);
	}

	@GetMapping("/all")
	public List<Formador> getAll() {
		return service.getAll();
	}

	@PostMapping("/create")
	public void addFormador(@RequestBody Formador entity) {
		service.addFormador(entity);
	}

	@PutMapping("/update")
	public void updateFormador(@RequestBody Formador entity) {
		service.updateFormador(entity);
	}

	@DeleteMapping("/{id}")
	public void deleteFormadorPorId(@PathVariable int id) {
		service.deleteFormadorPorId(id);
	}

	@DeleteMapping("/name/{nombre}")
	public void deleteFormadorPorNombre(@PathVariable String nombre) {
		service.deleteFormadorPorNombre(nombre);
	}
}
