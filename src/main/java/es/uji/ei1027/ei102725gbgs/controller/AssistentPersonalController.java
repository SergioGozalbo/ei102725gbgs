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

import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
import es.uji.ei1027.ei102725gbgs.services.AssistentPersonalServiceImpl;
import es.uji.ei1027.ei102725gbgs.utils.request.AssistentPersonalRequest;

@RestController
@RequestMapping("/api/assistentpersonal")
public class AssistentPersonalController {

	private final AssistentPersonalServiceImpl service;

	@Autowired
	public AssistentPersonalController(AssistentPersonalServiceImpl service) {
		this.service = service;
	}

	@GetMapping("/{id}")
	public AssistentPersonal getByID(@PathVariable String id) {
		return service.getByID(id);
	}

	@GetMapping("/all")
	public List<AssistentPersonal> getAll() {
		return service.getAll();
	}

	@PostMapping("/create")
	public void addAssistentPersonal(@RequestBody AssistentPersonalRequest entity) {
		service.addAssistentPersonal(entity);
	}

	@PutMapping("/update")
	public void updateAssistentPersonal(@RequestBody AssistentPersonal entity) {
		service.updateAssistentPersonal(entity);
	}

	@DeleteMapping("/email/{email}")
	public void deleteAssistentPersonalPorEmail(@PathVariable String email) {
		service.deleteAssistentPersonalPorEmail(email);
	}

	@DeleteMapping("/{id}")
	public void deleteAssistentPersonalPorId(@PathVariable String id) {
		service.deleteAssistentPersonalPorId(id);
	}

}
