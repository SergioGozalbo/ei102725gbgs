package es.uji.ei1027.ei102725gbgs.controller;

import java.util.List;

import es.uji.ei1027.ei102725gbgs.validation.FormadorValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import es.uji.ei1027.ei102725gbgs.model.Formador;
import es.uji.ei1027.ei102725gbgs.services.FormadorServiceImpl;

@Controller
@RequestMapping("/api/formador")
public class FormadorController {

    /**
     * Service used by this controller to perform operations on Formador entities.
     */
    private final FormadorServiceImpl service;

    /**
     * Creates a new controller.
     * @param service formador service
     */
    @Autowired
    public FormadorController(FormadorServiceImpl service) {
        this.service = service;
    }

    /**
     * Returns one formador.
     * @param id formador identifier
     * @return the formador
     */
    @GetMapping("/id/{id}")
    public Formador getByID(@PathVariable int id) {
        return service.getByID(id);
    }

    /**
     * Shows the add form.
     * @param model model for the view
     * @return the add view
     */
    @GetMapping("/add-formador")
    public String showAddForm(Model model) {
        model.addAttribute("formador", new Formador());
        return "formador/add";
    }

    /**
     * Processes a new formador.
     * @param formador formador data
     * @param result validation errors
     * @return redirect or add view on error
     */
    @PostMapping("/add")
    public String addFormador(@ModelAttribute("formador") Formador formador,
                              BindingResult result) {
        FormadorValidator validator = new FormadorValidator();
        validator.validate(formador, result);

        if (result.hasErrors()) {
            return "formador/add";
        }
        service.addFormador(formador);
        return "redirect:/api/formador/add-formador";
    }

    /**
     * Deletes a formador.
     * @param id formador identifier
     * @return redirect to the list view
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        service.deleteFormadorPorId(id);
        return "redirect:/api/formador/list";
    }

    /**
     * Returns all formadors.
     * @return the formador list
     */
    @GetMapping("/all")
    public List<Formador> getAll() {
        return service.getAll();
    }

    /**
     * Shows the list of formadors.
     * @param model model for the view
     * @return the list view
     */
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("formadors", service.getAll());
        return "formador/list";
    }

    /**
     * Creates a formador.
     * @param entity formador data
     */
    @PostMapping("/create")
    public void addFormador(@RequestBody Formador entity) {
        service.addFormador(entity);
    }

    /**
     * Updates a formador.
     * @param entity formador data
     */
    @PutMapping("/update")
    public void updateFormador(@RequestBody Formador entity) {
        service.updateFormador(entity);
    }

    /**
     * Deletes a formador by ID.
     * @param id formador identifier
     */
    @DeleteMapping("/{id}")
    public void deleteFormadorPorId(@PathVariable int id) {
        service.deleteFormadorPorId(id);
    }

    /**
     * Deletes a formador by name.
     * @param nombre formador name
     */
    @DeleteMapping("/name/{nombre}")
    public void deleteFormadorPorNombre(@PathVariable String nombre) {
        service.deleteFormadorPorNombre(nombre);
    }
}
