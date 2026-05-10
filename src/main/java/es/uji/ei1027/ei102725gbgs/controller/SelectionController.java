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

    /**
     * Service used by this controller to perform operations on Selection entities.
     */
    private final SelectionServiceImpl service;

    /**
     * Creates a new controller.
     * @param service selection service
     */
    @Autowired
    public SelectionController(SelectionServiceImpl service) {
        this.service = service;
    }

    /**
     * Returns one selection.
     * @param id selection identifier
     * @return the selection
     */
    @GetMapping("/{id}")
    public Selection getByID(@PathVariable int id) {
        return service.getByID(id);
    }

    /**
     * Returns all selections.
     * @return the selection list
     */
    @GetMapping("/all")
    public List<Selection> getAll() {
        return service.getAll();
    }

    /**
     * Creates a selection.
     * @param entity selection data
     */
    @PostMapping("/create")
    public void addSelection(@RequestBody Selection entity) {
        service.addSelection(entity);
    }

    /**
     * Updates a selection.
     * @param entity selection data
     */
    @PutMapping("/update")
    public void updateSelection(@RequestBody Selection entity) {
        service.updateSelection(entity);
    }

    /**
     * Deletes a selection by ID.
     * @param id selection identifier
     */
    @DeleteMapping("/{id}")
    public void deleteSelectionPorId(@PathVariable int id) {
        service.deleteSelectionPorId(id);
    }

    /**
     * Deletes selections for an assistant.
     * @param idAsistente assistant identifier
     */
    @DeleteMapping("/assistant/{idAsistente}")
    public void deleteSelectionPorAsistente(@PathVariable String idAsistente) {
        service.deleteSelectionPorAsistente(idAsistente);
    }
}
