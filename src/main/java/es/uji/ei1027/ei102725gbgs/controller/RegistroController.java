package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.AssistentPersonalDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.UUID;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    /**
     * DAO for accessing UsuariOVI data, used to create new OVI users during registration.
     */
    @Autowired
    private UsuariOVIDaoImpl usuariOVIDao;

    /**
     * DAO for accessing AssistentPersonal data, used to create new assistant users during registration.
     */
    @Autowired
    private AssistentPersonalDaoImpl assistentPersonalDao;

    /**
     * Shows the registration choice page.
     * @return the registration choice view
     */
    @GetMapping("")
    public String registro() {
        return "autenticacion/registro";
    }

    // UsuariOVI

    /**
     * Shows the UsuariOVI registration form.
     * @param model model for the view
     * @return the registration view
     */
    @GetMapping("/usuariOVI")
    public String showRegistroUsuariOVI(Model model) {
        model.addAttribute("usuariOVI", new UsuariOVI());
        return "autenticacion/registroUsuariOVI";
    }

    /**
     * Processes a UsuariOVI registration.
     * @param usuariOVI user data
     * @param bindingResult validation errors
     * @param model model for the view
     * @return redirect or registration view on error
     */
    @PostMapping("/usuariOVI")
    public String processRegistroUsuariOVI(
            @ModelAttribute("usuariOVI") UsuariOVI usuariOVI,
            BindingResult bindingResult,
            Model model) {

        // Validación básica
        if (usuariOVI.getNombre() == null
            || usuariOVI.getNombre().trim().isEmpty()) {
            bindingResult.rejectValue("nombre", "obligatorio",
                    "El nom és obligatori");
        }
        if (usuariOVI.getEmail() == null
            || usuariOVI.getEmail().trim().isEmpty()) {
            bindingResult.rejectValue("email", "obligatorio",
                    "L'email és obligatori");
        }
        if (usuariOVI.getPassword() == null
            || usuariOVI.getPassword().trim().isEmpty()) {
            bindingResult.rejectValue("password", "obligatorio",
                    "La contrasenya és obligatoria");
        }
        if (!usuariOVI.isConsentimientoRgpd()) {
            bindingResult.rejectValue("consentimientoRgpd", "obligatorio",
                    "Has d'acceptar el tractament de dades");
        }

        if (bindingResult.hasErrors()) {
            return "autenticacion/registroUsuariOVI";
        }

        // Generar ID único y guardar
        usuariOVI.setIdUsuario(UUID.randomUUID().toString());
        usuariOVIDao.addUsuariOVI(usuariOVI);

        return "redirect:/login";
    }

    /**
     * Shows the AssistentPersonal registration form.
     * @param model model for the view
     * @return the registration view
     */
    @GetMapping("/assistentPersonal")
    public String showRegistroAssistent(Model model) {
        model.addAttribute("assistentPersonal", new AssistentPersonal());
        return "autenticacion/registroAssistent";
    }

    /**
     * Processes an AssistentPersonal registration.
     * @param assistent assistant data
     * @param bindingResult validation errors
     * @param model model for the view
     * @return redirect or registration view on error
     */
    @PostMapping("/assistentPersonal")
    public String processRegistroAssistent(
            @ModelAttribute("assistentPersonal") AssistentPersonal assistent,
            BindingResult bindingResult,
            Model model) {

        // Validación básica
        if (assistent.getNombre() == null
            || assistent.getNombre().trim().isEmpty()) {
            bindingResult.rejectValue("nombre", "obligatorio",
                    "El nom és obligatori");
        }
        if (assistent.getEmail() == null
            || assistent.getEmail().trim().isEmpty()) {
            bindingResult.rejectValue("email", "obligatorio",
                    "L'email és obligatori");
        }
        if (assistent.getPassword() == null
            || assistent.getPassword().trim().isEmpty()) {
            bindingResult.rejectValue("password", "obligatorio",
                    "La contrasenya és obligatoria");
        }

        if (bindingResult.hasErrors()) {
            return "autenticacion/registroAssistent";
        }

        // El asistente empieza con estado pendiente de aprobación
        assistent.setIdAsistente(UUID.randomUUID().toString());
        assistent.setEstadoAceptado("PENDIENTE");
        assistentPersonalDao.addAssistentPersonal(assistent);

        return "redirect:/login";
    }
}
