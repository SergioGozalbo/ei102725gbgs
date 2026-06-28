package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Component
class UsuariOVIValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UsuariOVI.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        UsuariOVI u = (UsuariOVI) obj;

        if (u.getNombre() == null || u.getNombre().trim().isEmpty()) {
            errors.rejectValue("nombre", "obligatori", "El nom és obligatori");
        } else if (u.getNombre().matches(".*\\d.*")) {
            errors.rejectValue("nombre", "format",
                    "El nom no pot contenir números");
        }

        if (u.getApellidos() == null || u.getApellidos().trim().isEmpty()) {
            errors.rejectValue("apellidos", "obligatori",
                    "Els cognoms són obligatoris");
        } else if (u.getApellidos().matches(".*\\d.*")) {
            errors.rejectValue("apellidos", "format",
                    "Els cognoms no poden contenir números");
        }

        if (u.getEmail() == null || u.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "obligatori", "L'email és obligatori");
        } else if (!u.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.rejectValue("email", "format",
                    "El format de l'email no és vàlid");
        }

        if (u.getPassword() == null || u.getPassword().trim().isEmpty()) {
            errors.rejectValue("password", "obligatori",
                    "La contrasenya és obligatòria");
        }

        if (u.getTelefono() == null || !u.getTelefono().matches("\\d{9}")) {
            errors.rejectValue("telefono", "format",
                    "El telèfon ha de tenir exactament 9 dígits numèrics");
        }

        if (!u.isConsentimientoRgpd()) {
            errors.rejectValue("consentimientoRgpd", "obligatori",
                    "Has d'acceptar el tractament de dades per a continuar");
        }

        if (u.getIdUsuario() == null || u.getIdUsuario().trim().isEmpty()) {
            errors.rejectValue("idUsuario", "obligatori",
                    "L'identificador d'usuari és obligatori");
        }
    }
}

@Controller
@RequestMapping("/UsuariOVI")
public class UsuariOVIController {

    private final UsuariOVIDaoImpl usuariOVIDao;
    private final UsuariOVIValidator usuariOVIValidator;

    @Autowired
    public UsuariOVIController(UsuariOVIDaoImpl usuariOVIDao,
                               UsuariOVIValidator usuariOVIValidator) {
        this.usuariOVIDao = usuariOVIDao;
        this.usuariOVIValidator = usuariOVIValidator;
    }

    @RequestMapping("/list")
    public String listUsuariOVI(
            @RequestParam(defaultValue = "") String busqueda,
            @RequestParam(defaultValue = "0") int pagina,
            Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/login";

        int tamanyPagina = 10;
        List<UsuariOVI> todos = usuariOVIDao.getUsuariosOVI();
        List<UsuariOVI> filtrados = new ArrayList<>();
        for (UsuariOVI u : todos) {
            String nombre = (u.getNombre() + " " + u.getApellidos()).toLowerCase();
            if (busqueda.isEmpty() || nombre.contains(busqueda.toLowerCase()))
                filtrados.add(u);
        }

        int totalRegistres = filtrados.size();
        int totalPagines = (int) Math.ceil((double) totalRegistres / tamanyPagina);
        if (pagina < 0) pagina = 0;
        if (pagina >= totalPagines && totalPagines > 0) pagina = totalPagines - 1;

        int inici = pagina * tamanyPagina;
        int fi = Math.min(inici + tamanyPagina, totalRegistres);
        List<UsuariOVI> paginats = totalRegistres > 0
                ? filtrados.subList(inici, fi) : filtrados;

        model.addAttribute("usuarios", paginats);
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPagines", totalPagines);
        model.addAttribute("totalRegistres", totalRegistres);
        return "UsuariOVI/list";
    }

    @RequestMapping(value = "/add")
    public String addUsuariOVI(Model model) {
        model.addAttribute("usuariOVI", new UsuariOVI());
        return "UsuariOVI/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(
            @ModelAttribute("usuariOVI") UsuariOVI usuariOVI,
            BindingResult bindingResult) {

        int nextId = usuariOVIDao.getUsuariosOVI().stream()
                .mapToInt(u -> Integer.parseInt(u.getIdUsuario().substring(1)))
                .max().orElse(0) + 1;
        usuariOVI.setIdUsuario("U" + String.format("%03d", nextId));

        usuariOVIValidator.validate(usuariOVI, bindingResult);
        if (bindingResult.hasErrors()) return "UsuariOVI/add";

        // Encriptar contraseña con Jasypt (según PDF de prácticas)
        org.jasypt.util.password.BasicPasswordEncryptor enc =
                new org.jasypt.util.password.BasicPasswordEncryptor();
        usuariOVI.setPassword(enc.encryptPassword(usuariOVI.getPassword()));

        usuariOVIDao.addUsuariOVI(usuariOVI);
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{idUsuario}", method = RequestMethod.GET)
    public String editUsuariOVI(Model model, @PathVariable String idUsuario) {
        model.addAttribute("usuariOVI", usuariOVIDao.getUsuariOVI(idUsuario));
        return "UsuariOVI/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("usuariOVI") UsuariOVI usuariOVI,
            BindingResult bindingResult) {

        usuariOVIValidator.validate(usuariOVI, bindingResult);
        if (bindingResult.hasErrors()) return "UsuariOVI/update";

        usuariOVIDao.updateUsuariOVI(usuariOVI);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idUsuario}")
    public String processDelete(@PathVariable String idUsuario,
                                RedirectAttributes redirectAttributes) {
        try {
            usuariOVIDao.deleteUsuariOVIPorId(idUsuario);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMsg",
                    "No es pot eliminar aquest usuari perquè té sol·licituds "
                            + "associades. Elimina primer les seues sol·licituds.");
            return "redirect:/UsuariOVI/list";
        }
        return "redirect:/UsuariOVI/list";
    }

    @RequestMapping(value = "/confirmDelete/{idUsuario}",
            method = RequestMethod.GET)
    public String confirmDelete(@PathVariable String idUsuario,
                                Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/login";
        model.addAttribute("usuario", usuariOVIDao.getUsuariOVI(idUsuario));
        return "UsuariOVI/confirmDelete";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(HttpSession session, Model model) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) return "redirect:/login";
        model.addAttribute("usuari",
                usuariOVIDao.getUsuariOVI(usuari.getIdUsuario()));
        return "UsuariOVI/userOVIProfile";
    }

    @RequestMapping(value = "/profileEdit", method = RequestMethod.GET)
    public String profileEdit(HttpSession session, Model model) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) return "redirect:/login";
        model.addAttribute("usuariOVI",
                usuariOVIDao.getUsuariOVI(usuari.getIdUsuario()));
        return "UsuariOVI/userOVIProfileEdit";
    }

    @RequestMapping(value = "/profileEdit", method = RequestMethod.POST)
    public String processProfileEdit(
            @ModelAttribute("usuariOVI") UsuariOVI usuariOVI,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        usuariOVIValidator.validate(usuariOVI, bindingResult);
        if (bindingResult.hasErrors()) return "UsuariOVI/userOVIProfileEdit";

        usuariOVIDao.updateUsuariOVI(usuariOVI);
        session.setAttribute("usuariOVI", usuariOVI);
        redirectAttributes.addFlashAttribute("msgOk",
                "El teu perfil ha sigut actualitzat correctament.");
        return "redirect:profile";
    }
}