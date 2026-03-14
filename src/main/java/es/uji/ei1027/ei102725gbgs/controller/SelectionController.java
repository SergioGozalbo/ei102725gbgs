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

import es.uji.ei1027.ei102725gbgs.model.Selection;
import es.uji.ei1027.ei102725gbgs.services.SelectionServiceImpl;

@RestController
@RequestMapping("/api/selection")
public class SelectionController {

	private final SelectionServiceImpl service;

	@Autowired
	public SelectionController(SelectionServiceImpl service) {
		this.service = service;
	}

	@GetMapping("/{id}")
	public Selection getByID(@PathVariable int id) {
		return service.getByID(id);
	}

	@GetMapping("/all")
	public List<Selection> getAll() {
		return service.getAll();
	}

	@PostMapping("/create")
	public void addSelection(@RequestBody Selection entity) {
		service.addSelection(entity);
	}

	@PutMapping("/update")
	public void updateSelection(@RequestBody Selection entity) {
		service.updateSelection(entity);
	}

	@DeleteMapping("/{id}")
	public void deleteSelectionPorId(@PathVariable int id) {
		service.deleteSelectionPorId(id);
	}

	@DeleteMapping("/assistant/{idAsistente}")
	public void deleteSelectionPorAsistente(@PathVariable String idAsistente) {
		service.deleteSelectionPorAsistente(idAsistente);
	}

}
