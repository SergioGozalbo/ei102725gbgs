package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.AssistentPersonalDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.Validator;

@Component
class AssistentPersonalValidator implements Validator {

    /**
     * Checks whether this validator supports AssistentPersonal.
     * @param clazz class to check
     * @return true if supported
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return AssistentPersonal.class.equals(clazz);
    }

    /**
     * Validates an AssistentPersonal.
     * @param obj object to validate
     * @param errors validation errors
     */
    @Override
    public void validate(Object obj, Errors errors) {
        AssistentPersonal asistente = (AssistentPersonal) obj;

        if (asistente.getIdAsistente().trim().isEmpty()) {
            errors.rejectValue("idAsistente", "obligatori",
                    "L'identificador és obligatori");
        }

        if (asistente.getNombre() == null || asistente.getNombre().trim().isEmpty()) {
            errors.rejectValue("nombre",
                    "obligatori",
                    "El nom és obligatori");
        }

        if (asistente.getEmail().trim().isEmpty()) {
            errors.rejectValue("email",
                    "obligatori",
                    "L'email és obligatori");
        } else if (!asistente.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.rejectValue("email",
                    "format",
                    "El format de l'email no és vàlid");
        }

        if (asistente.getTelefono() == null
                || asistente.getTelefono().trim().isEmpty()) {
            errors.rejectValue("telefono",
                    "obligatori",
                    "El telèfon és obligatori");
        } else if (!asistente.getTelefono().matches("\\d{9}")) {
            errors.rejectValue("telefono",
                    "format",
                    "El telèfon ha de tenir 9 dígits");
        }
        if (asistente.getFormacionAcademica().trim().isEmpty()) {
            errors.rejectValue("formacionAcademica", "obligatori",
                    "La formació és obligatòria");
        }

        // El estado por defecto suele ser 'pendent' si no se indica nada
    }
}


/**
 * Controller for managing AssistentPersonal entities.
 */
@Controller
@RequestMapping("/AssistentPersonal")
public class AssistentPersonalController {

    /**
     * The DAO used to access the data store to manage {@link AssistentPersonal}.
     */
    private final AssistentPersonalDaoImpl assistentPersonalDao;

    /**
     * Validator for validating AssistentPersonal data before processing it.
     */
    private final AssistentPersonalValidator assistentPersonalValidator;

    /**
     * Creates a new controller.
     * @param assistentPersonalDao DAO for AssistentPersonal data
     * @param assistentPersonalValidator validator for AssistentPersonal data
     */
    @Autowired
        public AssistentPersonalController(
            AssistentPersonalDaoImpl assistentPersonalDao,
            AssistentPersonalValidator assistentPersonalValidator) {
        this.assistentPersonalDao = assistentPersonalDao;
        this.assistentPersonalValidator = assistentPersonalValidator;
    }

    /**
     * Shows the list of assistants.
     * @param model model for the view
     * @return the list view
     */
    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("asistentes",
            assistentPersonalDao.getAssistentsPersonals());
        return "AssistentPersonal/list";
    }

    /**
     * Shows the add form.
     * @param model model for the view
     * @return the add view
     */
    @RequestMapping(value = "/add")
    public String add(Model model) {
        AssistentPersonal ap = new AssistentPersonal();
        ap.setEstadoAceptado("Pendiente"); // Estado inicial por defecto
        model.addAttribute("assistentPersonal", ap);
        return "AssistentPersonal/add";
    }

    /**
     * Processes a new assistant.
     * @param assistentPersonal assistant data
     * @param bindingResult validation errors
     * @return redirect to the list or the add view on error
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
        public String processAdd(
            @ModelAttribute("assistentPersonal")
            AssistentPersonal assistentPersonal,
            BindingResult bindingResult) {

        int nextId = assistentPersonalDao.getAssistentsPersonals().stream()
                .mapToInt(ap -> {
                    return Integer.parseInt(
                            ap.getIdAsistente().substring(1)
                    );
                }).max().orElse(0) + 1;

        assistentPersonal.setIdAsistente("A" + String.format("%03d", nextId));

        assistentPersonalValidator.validate(assistentPersonal, bindingResult);

        if (bindingResult.hasErrors()) {
            return "AssistentPersonal/add";
        }

        assistentPersonalDao.addAssistentPersonal(assistentPersonal);
        return "redirect:list";
    }

    /**
     * Shows the update form.
     * @param model model for the view
     * @param idAsistente assistant identifier
     * @return the update view
     */
    @RequestMapping(value = "/update/{idAsistente}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable String idAsistente) {
        model.addAttribute("assistentPersonal",
            assistentPersonalDao.getAssistentPersonal(idAsistente));
        return "AssistentPersonal/update";
    }

    /**
     * Processes an updated assistant.
     * @param assistentPersonal assistant data
     * @param bindingResult validation errors
     * @return redirect to the list or the update view on error
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
        public String processUpdate(
            @ModelAttribute("assistentPersonal")
            AssistentPersonal assistentPersonal,
            BindingResult bindingResult) {
        assistentPersonalValidator.validate(assistentPersonal, bindingResult);
        if (bindingResult.hasErrors()) {
            return "AssistentPersonal/update";
        }

        assistentPersonalDao.updateAssistentPersonal(assistentPersonal);
        return "redirect:/dashboard/assistentPersonal";
    }

    /**
     * Deletes an assistant.
     * @param idAsistente assistant identifier
     * @return redirect to the list view
     */
    @RequestMapping(value = "/delete/{idAsistente}")
    public String processDelete(@PathVariable String idAsistente) {
        assistentPersonalDao.deleteAssistentPersonalPorId(idAsistente);
        return "redirect:../list";
    }

    /**
     * Shows the profile of the logged-in assistant.
     * @param session session data
     * @param model model for the view
     * @return the profile view or redirect to login
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(HttpSession session, Model model) {
        AssistentPersonal assistent =
                (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (assistent == null)
            return "redirect:/login";

        // Recargar desde BD para tener datos actualizados
        AssistentPersonal actualitzat =
                assistentPersonalDao.getAssistentPersonal(assistent.getIdAsistente());
        model.addAttribute("assistent", actualitzat);
        return "AssistentPersonal/assistentProfile";
    }

    /**
     * Shows the profile edit form for the logged-in assistant.
     * @param session session data
     * @param model model for the view
     * @return the profile edit view or redirect to login
     */
    @RequestMapping(value = "/profileEdit", method = RequestMethod.GET)
    public String profileEdit(HttpSession session, Model model) {
        AssistentPersonal assistent =
                (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (assistent == null)
            return "redirect:/login";

        AssistentPersonal actualitzat =
                assistentPersonalDao.getAssistentPersonal(assistent.getIdAsistente());
        model.addAttribute("assistentPersonal", actualitzat);
        return "AssistentPersonal/assistentProfileEdit";
    }

    /**
     * Processes the profile edit form.
     * @param assistentPersonal updated assistant data
     * @param bindingResult validation errors
     * @param session session data
     * @return redirect to profile or edit view on error
     */
    @RequestMapping(value = "/profileEdit", method = RequestMethod.POST)
    public String processProfileEdit(
            @ModelAttribute("assistentPersonal") AssistentPersonal assistentPersonal,
            BindingResult bindingResult,
            HttpSession session) {

        assistentPersonalValidator.validate(assistentPersonal, bindingResult);
        if (bindingResult.hasErrors())
            return "AssistentPersonal/assistentProfileEdit";

        assistentPersonalDao.updateAssistentPersonal(assistentPersonal);

        // Actualizar también la sesión con los nuevos datos
        session.setAttribute("assistentPersonal", assistentPersonal);

        return "redirect:profile";
    }
}
