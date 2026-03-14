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

import es.uji.ei1027.ei102725gbgs.model.RegistreContracte;
import es.uji.ei1027.ei102725gbgs.services.RegistreContracteServiceImpl;

@RestController
@RequestMapping("/api/registrecontracte")
public class RegistreContracteController {

	private final RegistreContracteServiceImpl service;

	@Autowired
	public RegistreContracteController(RegistreContracteServiceImpl service) {
		this.service = service;
	}

	@GetMapping("/{id}")
	public RegistreContracte getByID(@PathVariable int id) {
		return service.getByID(id);
	}

	@GetMapping
	public List<RegistreContracte> getAll() {
		return service.getAll();
	}

	@PostMapping
	public void addRegistreContracte(@RequestBody RegistreContracte entity) {
		service.addRegistreContracte(entity);
	}

	@PutMapping
	public void updateRegistreContracte(@RequestBody RegistreContracte entity) {
		service.updateRegistreContracte(entity);
	}

	@DeleteMapping("/{id}")
	public void deleteRegistreContractePorId(@PathVariable int id) {
		service.deleteRegistreContractePorId(id);
	}

	@DeleteMapping("/url/{urlPdf}")
	public void deleteRegistreContractePorUrl(@PathVariable String urlPdf) {
		service.deleteRegistreContractePorUrl(urlPdf);
	}

}
