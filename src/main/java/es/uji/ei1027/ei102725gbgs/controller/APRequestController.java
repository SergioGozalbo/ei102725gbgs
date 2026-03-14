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

import es.uji.ei1027.ei102725gbgs.model.APRequest;
import es.uji.ei1027.ei102725gbgs.services.APRequestServiceImpl;

@RestController
@RequestMapping("/api/aprequest")
public class APRequestController {

	private final APRequestServiceImpl service;

	@Autowired
	public APRequestController(APRequestServiceImpl service) {
		this.service = service;
	}

	@GetMapping("/{id}")
	public APRequest getByID(@PathVariable int id) {
		return service.getByID(id);
	}

	@GetMapping("/all")
	public List<APRequest> getAll() {
		return service.getAll();
	}

	@PostMapping("/create")
	public void addAPRequest(@RequestBody APRequest entity) {
		service.addAPRequest(entity);
	}

	@PutMapping("/update")
	public void updateAPRequest(@RequestBody APRequest entity) {
		service.updateAPRequest(entity);
	}

	@DeleteMapping("/{id}")
	public void deleteAPRequestPorId(@PathVariable int id) {
		service.deleteAPRequestPorId(id);
	}

	@DeleteMapping("/state/{estado}")
	public void deleteAPRequestPorEstado(@PathVariable String estado) {
		service.deleteAPRequestPorEstado(estado);
	}
}
