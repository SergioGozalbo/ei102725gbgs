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

    /**
     * Service used by this controller to perform operations on RegistreContracte entities.
     */
    private final RegistreContracteServiceImpl service;

    /**
     * Creates a new controller.
     * @param service contract service
     */
    @Autowired
    public RegistreContracteController(RegistreContracteServiceImpl service) {
        this.service = service;
    }

    /**
     * Returns one contract record.
     * @param id contract identifier
     * @return the contract record
     */
    @GetMapping("/{id}")
    public RegistreContracte getByID(@PathVariable int id) {
        return service.getByID(id);
    }

    /**
     * Returns all contract records.
     * @return the contract list
     */
    @GetMapping("/all")
    public List<RegistreContracte> getAll() {
        return service.getAll();
    }

    /**
     * Creates a contract record.
     * @param entity contract data
     */
    @PostMapping("/create")
    public void addRegistreContracte(@RequestBody RegistreContracte entity) {
        service.addRegistreContracte(entity);
    }

    /**
     * Updates a contract record.
     * @param entity contract data
     */
    @PutMapping("/update")
    public void updateRegistreContracte(@RequestBody RegistreContracte entity) {
        service.updateRegistreContracte(entity);
    }

    /**
     * Deletes a contract record by ID.
     * @param id contract identifier
     */
    @DeleteMapping("/{id}")
    public void deleteRegistreContractePorId(@PathVariable int id) {
        service.deleteRegistreContractePorId(id);
    }

    /**
     * Deletes a contract record by PDF URL.
     * @param urlPdf PDF URL
     */
    @DeleteMapping("/url/{urlPdf}")
    public void deleteRegistreContractePorUrl(@PathVariable String urlPdf) {
        service.deleteRegistreContractePorUrl(urlPdf);
    }
}
