package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.stereotype.Component;


@Component
class UsuariOVIValidator implements Validator {

    /**
     * Checks whether this validator supports UsuariOVI.
     * @param clazz class to check
     * @return true if supported
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return UsuariOVI.class.equals(clazz);
    }

    /**
     * Validates a UsuariOVI.
     * @param obj object to validate
     * @param errors validation errors
     */
    @Override
    public void validate(Object obj, Errors errors) {
        UsuariOVI usuario = (UsuariOVI) obj;

        // 1. Validar Email (formato básico)
        if (usuario.getEmail().trim().isEmpty()) {
            errors.rejectValue("email",
            "obligatori", "L'email és obligatori");
        } else if (!usuario.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.rejectValue("email",
            "format", "El format de l'email no és vàlid");
        }

        // 2. Validar Teléfono (exactamente 9 dígitos)
        if (!usuario.getTelefono().matches("\\d{9}")) {
                errors.rejectValue("telefono", "format",
                    "El telèfon ha de tenir exactament 9 dígits numèrics");
        }

        // 3. Validar Consentimiento RGPD (debe estar marcado)
        if (!usuario.isConsentimientoRgpd()) {
                errors.rejectValue("consentimientoRgpd",
                "obligatori",
                "Has d'acceptar el tractament de dades per a continuar");
        }

        // 4. Validar Nombre (sin números)
        if (usuario.getNombre().matches(".*\\d.*")) {
            errors.rejectValue("nombre",
            "format",
            "El nom no pot contenir números");
        }
        if (usuario.getNombre().trim().isEmpty()) {
            errors.rejectValue("nombre",
            "obligatori",
            "El nom és obligatori");
        }

        // 5. Validar Apellidos (sin números)
        if (usuario.getApellidos().matches(".*\\d.*")) {
                errors.rejectValue("apellidos", "format",
                    "Els cognoms no poden contenir números");
        }
        if (usuario.getApellidos().trim().isEmpty()) {
            errors.rejectValue("apellidos",
            "obligatori",
            "Els cognoms són obligatoris");
        }

        // 6. Validar ID (obligatorio)
        if (usuario.getIdUsuario().trim().isEmpty()) {
                errors.rejectValue("idUsuario",
            "obligatori",
            "L'identificador d'usuari és obligatori");
        }
    }
}

@Controller
@RequestMapping("/UsuariOVI")
public class UsuariOVIController {
    /**
     * DAO for accessing UsuariOVI data, used to manage user information in the application.
     */
    private final UsuariOVIDaoImpl usuariOVIDao;

    /**
     * Validator for validating UsuariOVI data.
     */
    private final UsuariOVIValidator usuariOVIValidator;

    /**
     * Creates a new controller.
     * @param usuariOVIDao user DAO
     * @param usuariOVIValidator user validator
     */
    @Autowired
        public UsuariOVIController(
            UsuariOVIDaoImpl usuariOVIDao,
            UsuariOVIValidator usuariOVIValidator) {
        this.usuariOVIDao = usuariOVIDao;
        this.usuariOVIValidator = usuariOVIValidator;
    }

    /**
     * Shows the user list.
     * @param model model for the view
     * @return the list view
     */
    @RequestMapping("/list")
    public String listUsuariOVI(Model model) {
        model.addAttribute("usuarios", usuariOVIDao.getUsuariosOVI());
        return "UsuariOVI/list";
    }

    /**
     * Shows the add form.
     * @param model model for the view
     * @return the add view
     */
    @RequestMapping(value = "/add")
    public String addUsuariOVI(Model model) {
        model.addAttribute("usuariOVI", new UsuariOVI());
        return "UsuariOVI/add";
    }

    /**
     * Processes a new user.
     * @param usuariOVI user data
     * @param bindingResult validation errors
     * @return redirect or add view on error
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
        public String processAddSubmit(
            @ModelAttribute("usuariOVI") UsuariOVI usuariOVI,
            BindingResult bindingResult) {

        // ASIGNACIÓN AUTOMÁTICA DEL ID (Máximo + 1)
        int nextId = usuariOVIDao.getUsuariosOVI().stream()
                .mapToInt(u -> Integer.parseInt(u.getIdUsuario().substring(1)))
                .max().orElse(0) + 1;

        usuariOVI.setIdUsuario("U" + String.format("%03d", nextId));

        usuariOVIValidator.validate(usuariOVI, bindingResult);

        if (bindingResult.hasErrors()) {
            return "UsuariOVI/add";
        }

        usuariOVIDao.addUsuariOVI(usuariOVI);
        return "redirect:list";
    }

    /**
     * Shows the update form.
     * @param model model for the view
     * @param idUsuario user identifier
     * @return the update view
     */
    @RequestMapping(value = "/update/{idUsuario}", method = RequestMethod.GET)
    public String editUsuariOVI(Model model, @PathVariable String idUsuario) {
        model.addAttribute("usuariOVI", usuariOVIDao.getUsuariOVI(idUsuario));
        return "UsuariOVI/update";
    }

    /**
     * Processes an updated user.
     * @param usuariOVI user data
     * @param bindingResult validation errors
     * @return redirect or update view on error
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
        public String processUpdateSubmit(
            @ModelAttribute("usuariOVI") UsuariOVI usuariOVI,
            BindingResult bindingResult) {

        usuariOVIValidator.validate(usuariOVI, bindingResult);

        if (bindingResult.hasErrors()) {
            return "UsuariOVI/update";
        }

        usuariOVIDao.updateUsuariOVI(usuariOVI);
        return "redirect:list";
    }

    /**
     * Deletes a user.
     * @param idUsuario user identifier
     * @param redirectAttributes attributes for messages
     * @return redirect to the list view
     */
    @RequestMapping(value = "/delete/{idUsuario}")
    public String processDelete(@PathVariable String idUsuario,
                                org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            usuariOVIDao.deleteUsuariOVIPorId(idUsuario);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMsg",
                    "No es pot eliminar aquest usuari perquè té sol·licituds associades. "
                            + "Elimina primer les seues sol·licituds.");
            return "redirect:/UsuariOVI/list";
        }
        return "redirect:/UsuariOVI/list";
    }

    /**
     * Shows the profile of the logged-in OVI user.
     * @param session session data
     * @param model model for the view
     * @return the profile view or redirect to login
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(HttpSession session, Model model) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) {
            return "redirect:/login";
        }

        UsuariOVI actualitzat = usuariOVIDao.getUsuariOVI(usuari.getIdUsuario());
        model.addAttribute("usuari", actualitzat);
        return "UsuariOVI/userOVIProfile";
    }

    /**
     * Shows the profile edit form for the logged-in OVI user.
     * @param session session data
     * @param model model for the view
     * @return the profile edit view or redirect to login
     */
    @RequestMapping(value = "/profileEdit", method = RequestMethod.GET)
    public String profileEdit(HttpSession session, Model model) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) {
            return "redirect:/login";
        }

        UsuariOVI actualitzat = usuariOVIDao.getUsuariOVI(usuari.getIdUsuario());
        model.addAttribute("usuariOVI", actualitzat);
        return "UsuariOVI/userOVIProfileEdit";
    }

    /**
     * Processes the profile edit form for the logged-in OVI user.
     * @param usuariOVI updated user data
     * @param bindingResult validation errors
     * @param session session data
     * @return redirect to profile or edit view on error
     */
    @RequestMapping(value = "/profileEdit", method = RequestMethod.POST)
    public String processProfileEdit(
            @ModelAttribute("usuariOVI") UsuariOVI usuariOVI,
            BindingResult bindingResult,
            HttpSession session) {

        usuariOVIValidator.validate(usuariOVI, bindingResult);
        if (bindingResult.hasErrors()) {
            return "UsuariOVI/userOVIProfileEdit";
        }

        usuariOVIDao.updateUsuariOVI(usuariOVI);
        session.setAttribute("usuariOVI", usuariOVI);
        return "redirect:profile";
    }

}
