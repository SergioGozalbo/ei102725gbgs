package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.AssistentPersonalDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpSession;


/**
 * Validator for the login form, checking that email and password are not empty.
 */
class UserValidator implements Validator {
    /**
     * Checks whether this validator supports UsuariOVI.
     * @param cls class to check
     * @return true if supported
     */
    @Override
    public boolean supports(Class<?> cls) {
        return UsuariOVI.class.isAssignableFrom(cls);
    }

    /**
     * Validates a login form.
     * @param obj object to validate
     * @param errors validation errors
     */
    @Override
    public void validate(Object obj, Errors errors) {
        UsuariOVI form = (UsuariOVI) obj;
        if (form.getEmail() == null || form.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "obligatorio",
                    "El camp Email és obligatori");
        }
        if (form.getPassword() == null || form.getPassword().trim().isEmpty()) {
            errors.rejectValue("password", "obligatorio",
                    "El camp Password és obligatori");
        }
    }
}



/**
 * Controller for handling login operations.
 */
@Controller
public class LoginController {

    /**
     * DAO for accessing UsuariOVI data, used to validate login credentials.
     */
    @Autowired
    private UsuariOVIDaoImpl usuariOVIDao;

    /**
     * DAO for accessing AssistentPersonal data, used to validate login credentials.
     */
    @Autowired
    private AssistentPersonalDaoImpl assistentPersonalDao;

    /**
     * Shows the login form.
     * @param model model for the view
     * @param session session data
     * @return the login view or a redirect to the dashboard
     */
    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) {
        if (session.getAttribute("usuariOVI") != null) {
            return "redirect:/dashboard/usuariOVI";
        }
        if (session.getAttribute("assistentPersonal") != null) {
            return "redirect:/dashboard/assistentPersonal";
        }
        if (Boolean.TRUE.equals(session.getAttribute("admin"))) {
            return "redirect:/dashboard/admin";
        }

        model.addAttribute("usuariOVI", new UsuariOVI());
        return "autenticacion/login";
    }

    /**
     * Processes the login.
     * @param form login data
     * @param bindingResult validation errors
     * @param model model for the view
     * @param session session data
     * @return redirect or login view on error
     */
    @PostMapping("/login")
    public String processLogin(
            @ModelAttribute("usuariOVI") UsuariOVI form,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        new UserValidator().validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "autenticacion/login";
        }

        String email    = form.getEmail().trim();
        String password = form.getPassword();

        // 2. Comprobar si es admin
        if (email.equals("admin@sgovi.es") && password.equals("admin1234")) {
            session.setAttribute("admin", true);
            session.setAttribute("adminEmail", email);
            return "redirect:/dashboard/admin";
        }

        // 3. Buscar como UsuariOVI
        UsuariOVI usuariOVI = usuariOVIDao.getUsuariOVIByEmail(email);
        if (usuariOVI != null && password.equals(usuariOVI.getPassword())) {
            session.setAttribute("usuariOVI", usuariOVI);
            return "redirect:/dashboard/usuariOVI";
        }

        // 4. Buscar como AssistentPersonal
        AssistentPersonal assistent =
            assistentPersonalDao.getAssistentPersonalByEmail(email);
        if (assistent != null && password.equals(assistent.getPassword())) {
            session.setAttribute("assistentPersonal", assistent);
            return "redirect:/dashboard/assistentPersonal";
        }

        // 5. Credenciales incorrectas
        model.addAttribute("error", "Email o contrasenya incorrectes");
        model.addAttribute("loginForm", form);
        return "autenticacion/login";
    }

    /**
     * Logs out the current user.
     * @param session session data
     * @return redirect to the login view
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
