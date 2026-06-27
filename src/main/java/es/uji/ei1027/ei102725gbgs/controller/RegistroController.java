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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
            Model model,
            RedirectAttributes redirectAttributes) {

        // Validación básica (mantenemos la tuya)
        if (usuariOVI.getNombre() == null || usuariOVI.getNombre().trim().isEmpty()) {
            bindingResult.rejectValue("nombre", "obligatorio", "El nom és obligatori");
        }
        if (usuariOVI.getEmail() == null || usuariOVI.getEmail().trim().isEmpty()) {
            bindingResult.rejectValue("email", "obligatorio", "L'email és obligatori");
        }
        if (usuariOVI.getPassword() == null || usuariOVI.getPassword().trim().isEmpty()) {
            bindingResult.rejectValue("password", "obligatorio", "La contrasenya és obligatoria");
        }
        if (!usuariOVI.isConsentimientoRgpd()) {
            bindingResult.rejectValue("consentimientoRgpd", "obligatorio", "Has d'acceptar el tractament de dades");
        }

        if (bindingResult.hasErrors()) {
            return "autenticacion/registroUsuariOVI";
        }

        int nextId = usuariOVIDao.getUsuariosOVI().stream()
                .mapToInt(u -> {
                    try {
                        return Integer.parseInt(u.getIdUsuario().substring(1));
                    } catch (Exception e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0) + 1;
        String formattedId = String.format("U%03d", nextId);
        usuariOVI.setIdUsuario(formattedId);

        usuariOVIDao.addUsuariOVI(usuariOVI);

        redirectAttributes.addFlashAttribute("msgOk",
                "¡Cuenta creada correctamente! Ya puedes iniciar sesión.");

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
            Model model,
            RedirectAttributes redirectAttributes) {

        // Validación básica (mantenemos la tuya)
        if (assistent.getNombre() == null || assistent.getNombre().trim().isEmpty()) {
            bindingResult.rejectValue("nombre", "obligatorio", "El nom és obligatori");
        }
        if (assistent.getEmail() == null || assistent.getEmail().trim().isEmpty()) {
            bindingResult.rejectValue("email", "obligatorio", "L'email és obligatori");
        }
        if (assistent.getPassword() == null || assistent.getPassword().trim().isEmpty()) {
            bindingResult.rejectValue("password", "obligatorio", "La contrasenya és obligatoria");
        }

        if (bindingResult.hasErrors()) {
            return "autenticacion/registroAssistent";
        }

        // --- CORRECCIÓN ID ASISTENTE ---
        int nextId = assistentPersonalDao.getAssistentsPersonals().stream()
                .mapToInt(a -> {
                    try {
                        return Integer.parseInt(a.getIdAsistente().substring(1));
                    } catch (Exception e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0) + 1;
        String formattedId = String.format("A%03d", nextId);
        assistent.setIdAsistente(formattedId);

        assistent.setEstadoAceptado("Pendiente");

        assistentPersonalDao.addAssistentPersonal(assistent);

        redirectAttributes.addFlashAttribute("msgInfo",
                "Solicitud enviada. Tu cuenta está pendiente de aprobación por el administrador. Puedes iniciar sesión");

        return "redirect:/login";
    }
}
