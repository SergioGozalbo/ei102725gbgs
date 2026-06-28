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

class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return UsuariOVI.class.isAssignableFrom(cls);
    }

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

@Controller
public class LoginController {

    @Autowired
    private UsuariOVIDaoImpl usuariOVIDao;

    @Autowired
    private AssistentPersonalDaoImpl assistentPersonalDao;

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) {
        if (session.getAttribute("usuariOVI") != null)
            return "redirect:/dashboard/usuariOVI";
        if (session.getAttribute("assistentPersonal") != null)
            return "redirect:/dashboard/assistentPersonal";
        if (Boolean.TRUE.equals(session.getAttribute("admin")))
            return "redirect:/dashboard/admin";

        model.addAttribute("usuariOVI", new UsuariOVI());
        return "autenticacion/login";
    }

    @PostMapping("/login")
    public String processLogin(
            @ModelAttribute("usuariOVI") UsuariOVI form,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        new UserValidator().validate(form, bindingResult);
        if (bindingResult.hasErrors()) return "autenticacion/login";

        String email    = form.getEmail().trim();
        String password = form.getPassword();

        // Admin — sin encriptar (credencial fija)
        if (email.equals("admin@sgovi.es") && password.equals("admin1234")) {
            session.setAttribute("admin", true);
            session.setAttribute("adminEmail", email);
            return "redirect:/dashboard/admin";
        }

        org.jasypt.util.password.BasicPasswordEncryptor enc =
                new org.jasypt.util.password.BasicPasswordEncryptor();

        // UsuariOVI
        UsuariOVI usuariOVI = usuariOVIDao.getUsuariOVIByEmail(email);
        if (usuariOVI != null) {
            boolean ok = false;
            try {
                ok = enc.checkPassword(password, usuariOVI.getPassword());
            } catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e) {
                // Contraseña en texto plano (usuario creado antes de encriptación)
                ok = password.equals(usuariOVI.getPassword());
            }
            if (ok) {
                session.setAttribute("usuariOVI", usuariOVI);
                return "redirect:/dashboard/usuariOVI";
            }
        }

        // AssistentPersonal
        AssistentPersonal assistent =
                assistentPersonalDao.getAssistentPersonalByEmail(email);
        if (assistent != null) {
            boolean ok = false;
            try {
                ok = enc.checkPassword(password, assistent.getPassword());
            } catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e) {
                // Contraseña en texto plano (usuario creado antes de encriptación)
                ok = password.equals(assistent.getPassword());
            }
            if (ok) {
                session.setAttribute("assistentPersonal", assistent);
                return "redirect:/dashboard/assistentPersonal";
            }
        }

        model.addAttribute("error", "Email o contrasenya incorrectes");
        return "autenticacion/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}