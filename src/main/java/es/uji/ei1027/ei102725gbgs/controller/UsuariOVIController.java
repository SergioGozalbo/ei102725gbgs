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

import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import es.uji.ei1027.ei102725gbgs.services.UsuariOVIServiceImpl;
import es.uji.ei1027.ei102725gbgs.utils.request.UsuariOVIRequest;

@RestController
@RequestMapping("/api/usuariovi")
public class UsuariOVIController {

	private final UsuariOVIServiceImpl service;

	@Autowired
	public UsuariOVIController(UsuariOVIServiceImpl service) {
		this.service = service;
	}

	@GetMapping("/{id}")
	public UsuariOVI getByID(@PathVariable String id) {
		return service.getByID(id);
	}

	@GetMapping
	public List<UsuariOVI> getAll() {
		return service.getAll();
	}

	@PostMapping
	public void addUsuariOVI(@RequestBody UsuariOVIRequest entity) {
		service.addUsuariOVI(entity);
	}

	@PutMapping
	public void updateUsuariOVI(@RequestBody UsuariOVI entity) {
		service.updateUsuariOVI(entity);
	}

	@DeleteMapping("/email/{email}")
	public void deleteUsuariOVIPorEmail(@PathVariable String email) {
		service.deleteUsuariOVIPorEmail(email);
	}

	@DeleteMapping("/{id}")
	public void deleteUsuariOVIPorId(@PathVariable String id) {
		service.deleteUsuariOVIPorId(id);
	}

}
