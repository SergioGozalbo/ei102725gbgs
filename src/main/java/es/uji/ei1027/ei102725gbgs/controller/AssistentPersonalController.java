package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.AssistentPersonalDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
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

    @Override
    public boolean supports(Class<?> clazz) {
        return AssistentPersonal.class.equals(clazz);
    }

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


@Controller
@RequestMapping("/AssistentPersonal")
public class AssistentPersonalController {

    private final AssistentPersonalDaoImpl assistentPersonalDao;
    private final AssistentPersonalValidator assistentPersonalValidator;

    @Autowired
        public AssistentPersonalController(
            AssistentPersonalDaoImpl assistentPersonalDao,
            AssistentPersonalValidator assistentPersonalValidator) {
        this.assistentPersonalDao = assistentPersonalDao;
        this.assistentPersonalValidator = assistentPersonalValidator;
    }

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("asistentes",
            assistentPersonalDao.getAssistentsPersonals());
        return "AssistentPersonal/list";
    }

    @RequestMapping(value = "/add")
    public String add(Model model) {
        AssistentPersonal ap = new AssistentPersonal();
        ap.setEstadoAceptado("Pendiente"); // Estado inicial por defecto
        model.addAttribute("assistentPersonal", ap);
        return "AssistentPersonal/add";
    }

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

    @RequestMapping(value = "/update/{idAsistente}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable String idAsistente) {
        model.addAttribute("assistentPersonal",
            assistentPersonalDao.getAssistentPersonal(idAsistente));
        return "AssistentPersonal/update";
    }

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
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idAsistente}")
    public String processDelete(@PathVariable String idAsistente) {
        assistentPersonalDao.deleteAssistentPersonalPorId(idAsistente);
        return "redirect:../list";
    }
}
