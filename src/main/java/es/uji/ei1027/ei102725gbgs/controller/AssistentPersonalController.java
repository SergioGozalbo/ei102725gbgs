package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.APRequestDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.AssistentPersonalDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.RegistreContracteDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.SelectionDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.APRequest;
import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
import es.uji.ei1027.ei102725gbgs.model.RegistreContracte;
import es.uji.ei1027.ei102725gbgs.model.Selection;
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
class AssistentPersonalValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AssistentPersonal.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        AssistentPersonal a = (AssistentPersonal) obj;

        if (a.getIdAsistente() == null || a.getIdAsistente().trim().isEmpty()) {
            errors.rejectValue("idAsistente", "obligatori",
                    "L'identificador és obligatori");
        }

        if (a.getNombre() == null || a.getNombre().trim().isEmpty()) {
            errors.rejectValue("nombre", "obligatori", "El nom és obligatori");
        }

        if (a.getApellidos() == null || a.getApellidos().trim().isEmpty()) {
            errors.rejectValue("apellidos", "obligatori",
                    "Els cognoms són obligatoris");
        }

        if (a.getEmail() == null || a.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "obligatori", "L'email és obligatori");
        } else if (!a.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.rejectValue("email", "format",
                    "El format de l'email no és vàlid");
        }

        if (a.getPassword() == null || a.getPassword().trim().isEmpty()) {
            errors.rejectValue("password", "obligatori",
                    "La contrasenya és obligatòria");
        }

        if (a.getTelefono() == null || a.getTelefono().trim().isEmpty()) {
            errors.rejectValue("telefono", "obligatori",
                    "El telèfon és obligatori");
        } else if (!a.getTelefono().matches("\\d{9}")) {
            errors.rejectValue("telefono", "format",
                    "El telèfon ha de tenir 9 dígits");
        }

        if (a.getFormacionAcademica() == null
                || a.getFormacionAcademica().trim().isEmpty()) {
            errors.rejectValue("formacionAcademica", "obligatori",
                    "La formació és obligatòria");
        }
    }
}

@Controller
@RequestMapping("/AssistentPersonal")
public class AssistentPersonalController {

    private final AssistentPersonalDaoImpl assistentPersonalDao;
    private final AssistentPersonalValidator assistentPersonalValidator;
    private final APRequestDaoImpl apRequestDao;
    private final SelectionDaoImpl selectionDao;
    private final UsuariOVIDaoImpl usuariOVIDao;
    private final RegistreContracteDaoImpl registreContracteDao;

    private static final int TITLE_FONT_SIZE = 20;
    private static final int BODY_FONT_SIZE  = 12;

    @Autowired
    public AssistentPersonalController(
            AssistentPersonalDaoImpl assistentPersonalDao,
            AssistentPersonalValidator assistentPersonalValidator,
            APRequestDaoImpl apRequestDao,
            SelectionDaoImpl selectionDao,
            UsuariOVIDaoImpl usuariOVIDao,
            RegistreContracteDaoImpl registreContracteDao) {
        this.assistentPersonalDao       = assistentPersonalDao;
        this.assistentPersonalValidator = assistentPersonalValidator;
        this.apRequestDao               = apRequestDao;
        this.selectionDao               = selectionDao;
        this.usuariOVIDao               = usuariOVIDao;
        this.registreContracteDao       = registreContracteDao;
    }

    // ---- LIST ----

    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "") String busqueda,
            @RequestParam(defaultValue = "") String estat,
            @RequestParam(defaultValue = "0") int pagina,
            Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/login";

        int tamanyPagina = 10;
        List<AssistentPersonal> todos =
                assistentPersonalDao.getAssistentsPersonals();
        List<AssistentPersonal> filtrados = new ArrayList<>();
        for (AssistentPersonal a : todos) {
            String nombre = (a.getNombre() + " " + a.getApellidos()).toLowerCase();
            boolean coincideixNom = busqueda.isEmpty()
                    || nombre.contains(busqueda.toLowerCase());
            boolean coincideixEstat = estat.isEmpty()
                    || estat.equals(a.getEstadoAceptado());
            if (coincideixNom && coincideixEstat) filtrados.add(a);
        }

        int totalRegistres = filtrados.size();
        int totalPagines = (int) Math.ceil((double) totalRegistres / tamanyPagina);
        if (pagina < 0) pagina = 0;
        if (pagina >= totalPagines && totalPagines > 0) pagina = totalPagines - 1;

        int inici = pagina * tamanyPagina;
        int fi = Math.min(inici + tamanyPagina, totalRegistres);
        List<AssistentPersonal> paginats = totalRegistres > 0
                ? filtrados.subList(inici, fi) : filtrados;

        model.addAttribute("asistentes", paginats);
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("estat", estat);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPagines", totalPagines);
        model.addAttribute("totalRegistres", totalRegistres);
        return "AssistentPersonal/list";
    }

    // ---- ADD ----

    @RequestMapping(value = "/add")
    public String add(Model model) {
        AssistentPersonal ap = new AssistentPersonal();
        ap.setEstadoAceptado("Pendiente");
        model.addAttribute("assistentPersonal", ap);
        return "AssistentPersonal/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAdd(
            @ModelAttribute("assistentPersonal") AssistentPersonal assistentPersonal,
            BindingResult bindingResult) {

        int nextId = assistentPersonalDao.getAssistentsPersonals().stream()
                .mapToInt(ap -> Integer.parseInt(ap.getIdAsistente().substring(1)))
                .max().orElse(0) + 1;
        assistentPersonal.setIdAsistente("A" + String.format("%03d", nextId));

        assistentPersonalValidator.validate(assistentPersonal, bindingResult);
        if (bindingResult.hasErrors()) return "AssistentPersonal/add";

        // Encriptar contraseña con Jasypt (según PDF de prácticas)
        org.jasypt.util.password.BasicPasswordEncryptor enc =
                new org.jasypt.util.password.BasicPasswordEncryptor();
        assistentPersonal.setPassword(
                enc.encryptPassword(assistentPersonal.getPassword()));

        assistentPersonalDao.addAssistentPersonal(assistentPersonal);
        return "redirect:list";
    }

    // ---- UPDATE ----

    @RequestMapping(value = "/update/{idAsistente}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable String idAsistente) {
        model.addAttribute("assistentPersonal",
                assistentPersonalDao.getAssistentPersonal(idAsistente));
        return "AssistentPersonal/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdate(
            @ModelAttribute("assistentPersonal") AssistentPersonal assistentPersonal,
            BindingResult bindingResult) {

        // En update no revalidamos contraseña (viene hidden, ya encriptada)
        if (assistentPersonal.getNombre() == null
                || assistentPersonal.getNombre().trim().isEmpty()) {
            bindingResult.rejectValue("nombre", "obligatori",
                    "El nom és obligatori");
        }
        if (assistentPersonal.getApellidos() == null
                || assistentPersonal.getApellidos().trim().isEmpty()) {
            bindingResult.rejectValue("apellidos", "obligatori",
                    "Els cognoms són obligatoris");
        }
        if (assistentPersonal.getEmail() == null
                || assistentPersonal.getEmail().trim().isEmpty()) {
            bindingResult.rejectValue("email", "obligatori",
                    "L'email és obligatori");
        }
        if (assistentPersonal.getTelefono() == null
                || !assistentPersonal.getTelefono().matches("\\d{9}")) {
            bindingResult.rejectValue("telefono", "format",
                    "El telèfon ha de tenir 9 dígits");
        }

        if (bindingResult.hasErrors()) return "AssistentPersonal/update";

        assistentPersonalDao.updateAssistentPersonal(assistentPersonal);
        return "redirect:/AssistentPersonal/list";
    }

    // ---- DELETE ----

    @RequestMapping(value = "/delete/{idAsistente}")
    public String processDelete(@PathVariable String idAsistente,
                                RedirectAttributes redirectAttributes) {
        try {
            assistentPersonalDao.deleteAssistentPersonalPorId(idAsistente);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMsg",
                    "No es pot eliminar aquest assistent perquè té contractes "
                            + "o seleccions associades.");
            return "redirect:/AssistentPersonal/list";
        }
        return "redirect:/AssistentPersonal/list";
    }

    @RequestMapping(value = "/confirmDelete/{idAsistente}",
            method = RequestMethod.GET)
    public String confirmDelete(@PathVariable String idAsistente,
                                Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/login";
        model.addAttribute("asistente",
                assistentPersonalDao.getAssistentPersonal(idAsistente));
        return "AssistentPersonal/confirmDelete";
    }

    // ---- CANCEL REQUEST ----

    @RequestMapping(value = "/cancelRequest/{idSolicitud}",
            method = RequestMethod.GET)
    public String cancelRequest(@PathVariable int idSolicitud,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        AssistentPersonal ap =
                (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) return "redirect:/login";
        selectionDao.deleteSelection(idSolicitud, ap.getIdAsistente());
        redirectAttributes.addFlashAttribute("msgOk",
                "Sol·licitud eliminada de la teua llista.");
        return "redirect:/AssistentPersonal/requests";
    }

    @RequestMapping(value = "/confirmCancelRequest/{id}",
            method = RequestMethod.GET)
    public String confirmCancelRequest(@PathVariable int id,
                                       Model model, HttpSession session) {
        AssistentPersonal ap =
                (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) return "redirect:/login";
        model.addAttribute("idSolicitud", id);
        return "AssistentPersonal/confirmCancelRequest";
    }

    // ---- PROFILE ----

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(HttpSession session, Model model) {
        AssistentPersonal ap =
                (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) return "redirect:/login";
        model.addAttribute("assistent",
                assistentPersonalDao.getAssistentPersonal(ap.getIdAsistente()));
        return "AssistentPersonal/assistentProfile";
    }

    @RequestMapping(value = "/profileEdit", method = RequestMethod.GET)
    public String profileEdit(HttpSession session, Model model) {
        AssistentPersonal ap =
                (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) return "redirect:/login";
        model.addAttribute("assistentPersonal",
                assistentPersonalDao.getAssistentPersonal(ap.getIdAsistente()));
        return "AssistentPersonal/assistentProfileEdit";
    }

    @RequestMapping(value = "/profileEdit", method = RequestMethod.POST)
    public String processProfileEdit(
            @ModelAttribute("assistentPersonal") AssistentPersonal assistentPersonal,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        assistentPersonalValidator.validate(assistentPersonal, bindingResult);
        if (bindingResult.hasErrors())
            return "AssistentPersonal/assistentProfileEdit";

        assistentPersonalDao.updateAssistentPersonal(assistentPersonal);
        session.setAttribute("assistentPersonal", assistentPersonal);
        redirectAttributes.addFlashAttribute("msgOk",
                "El teu perfil ha sigut actualitzat correctament.");
        return "redirect:profile";
    }

    // ---- MYREQUESTS (legacy, mantingut per si cal) ----

    @RequestMapping("/myrequests")
    public String myRequests(
            @RequestParam(defaultValue = "") String busqueda,
            @RequestParam(defaultValue = "10") int registres,
            Model model, HttpSession session) {
        AssistentPersonal ap =
                (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) return "redirect:/login";

        List<Selection> mySelections =
                selectionDao.getSelectionsByAsistente(ap.getIdAsistente());
        List<APRequest> todas = new ArrayList<>();
        List<Integer> solicitudesConContrato = new ArrayList<>();
        List<RegistreContracte> todosLosContratos =
                registreContracteDao.getRegistresContractes();
        List<UsuariOVI> usuarios = usuariOVIDao.getUsuariosOVI();

        for (Selection s : mySelections) {
            APRequest req = apRequestDao.getAPRequest(s.getIdSolicitud());
            if (req != null) {
                todas.add(req);
                boolean tieneContrato = todosLosContratos.stream()
                        .anyMatch(c -> c.getIdSeleccion() == s.getIdSeleccion());
                if (tieneContrato)
                    solicitudesConContrato.add(s.getIdSolicitud());
            }
        }

        List<APRequest> filtradas = new ArrayList<>();
        for (APRequest req : todas) {
            if (busqueda.isEmpty()) {
                filtradas.add(req);
            } else {
                for (UsuariOVI u : usuarios) {
                    if (u.getIdUsuario().equals(req.getIdUsuarioOvi())) {
                        String nombre = (u.getNombre() + " "
                                + u.getApellidos()).toLowerCase();
                        if (nombre.contains(busqueda.toLowerCase()))
                            filtradas.add(req);
                        break;
                    }
                }
            }
        }

        model.addAttribute("requests",
                filtradas.size() > registres
                        ? filtradas.subList(0, registres) : filtradas);
        model.addAttribute("selections", mySelections);
        model.addAttribute("solicitudesConContrato", solicitudesConContrato);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("registres", registres);
        model.addAttribute("totalRegistres", filtradas.size());
        return "AssistentPersonal/myrequests";
    }

    // ---- REQUESTS ASSIGNADES ----

    @RequestMapping("/requests")
    public String myAssignedRequests(
            @RequestParam(defaultValue = "") String busqueda,
            @RequestParam(defaultValue = "") String estat,
            @RequestParam(defaultValue = "0") int pagina,
            Model model, HttpSession session) {
        AssistentPersonal ap =
                (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) return "redirect:/login";

        int tamanyPagina = 10;
        List<Selection> misSelecciones =
                selectionDao.getSelectionsByAsistente(ap.getIdAsistente());
        List<RegistreContracte> todosContratos =
                registreContracteDao.getRegistresContractes();
        List<UsuariOVI> usuarios = usuariOVIDao.getUsuariosOVI();

        List<APRequest> totas = new ArrayList<>();
        java.util.Map<Integer, String> estatPerSolicitud =
                new java.util.HashMap<>();

        for (Selection s : misSelecciones) {
            APRequest req = apRequestDao.getAPRequest(s.getIdSolicitud());
            if (req == null) continue;

            RegistreContracte nostreContracte =
                    registreContracteDao.getRegistreContracteBySeleccion(
                            s.getIdSeleccion());
            if (nostreContracte != null) {
                estatPerSolicitud.put(req.getIdSolicitud(), "contractat");
            } else if ("Cerrada con contrato".equals(req.getEstado())) {
                estatPerSolicitud.put(req.getIdSolicitud(), "tancat");
            } else {
                estatPerSolicitud.put(req.getIdSolicitud(), "candidat");
            }
            totas.add(req);
        }

        List<APRequest> filtrades = new ArrayList<>();
        for (APRequest req : totas) {
            boolean coincideixNom = busqueda.isEmpty();
            if (!coincideixNom) {
                for (UsuariOVI u : usuarios) {
                    if (u.getIdUsuario().equals(req.getIdUsuarioOvi())) {
                        coincideixNom = (u.getNombre() + " " + u.getApellidos())
                                .toLowerCase().contains(busqueda.toLowerCase());
                        break;
                    }
                }
            }
            String estatReq = estatPerSolicitud
                    .getOrDefault(req.getIdSolicitud(), "");
            boolean coincideixEstat = estat.isEmpty()
                    || estat.equals(estatReq);
            if (coincideixNom && coincideixEstat) filtrades.add(req);
        }

        int totalRegistres = filtrades.size();
        int totalPagines = (int) Math.ceil((double) totalRegistres / tamanyPagina);
        if (pagina < 0) pagina = 0;
        if (pagina >= totalPagines && totalPagines > 0) pagina = totalPagines - 1;

        int inici = pagina * tamanyPagina;
        int fi = Math.min(inici + tamanyPagina, totalRegistres);
        List<APRequest> paginades = totalRegistres > 0
                ? filtrades.subList(inici, fi) : filtrades;

        model.addAttribute("requests", paginades);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("estatPerSolicitud", estatPerSolicitud);
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("estat", estat);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPagines", totalPagines);
        model.addAttribute("totalRegistres", totalRegistres);
        return "AssistentPersonal/requestList";
    }

    // ---- REQUEST DETAILS ----

    @RequestMapping(value = "/requestDetails/{id}", method = RequestMethod.GET)
    public String requestDetails(@PathVariable int id,
                                 Model model, HttpSession session) {
        AssistentPersonal ap =
                (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) return "redirect:/login";

        APRequest request = apRequestDao.getAPRequest(id);
        if (request == null) return "redirect:/AssistentPersonal/requests";

        model.addAttribute("request", request);
        model.addAttribute("usuari",
                usuariOVIDao.getUsuariOVI(request.getIdUsuarioOvi()));
        return "AssistentPersonal/requestDetails";
    }

    // ---- PDF CONTRACTE ----

    @RequestMapping(value = "/contrato/{idSolicitud}", method = RequestMethod.GET)
    public void verContrato(@PathVariable int idSolicitud,
                            HttpSession session,
                            jakarta.servlet.http.HttpServletResponse response)
            throws Exception {

        AssistentPersonal ap =
                (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) { response.sendRedirect("/login"); return; }

        Selection seleccion = null;
        for (Selection s :
                selectionDao.getSelectionsByAsistente(ap.getIdAsistente())) {
            if (s.getIdSolicitud() == idSolicitud) { seleccion = s; break; }
        }
        if (seleccion == null) {
            response.sendRedirect("/AssistentPersonal/requests"); return;
        }

        RegistreContracte contracte = registreContracteDao
                .getRegistreContracteBySeleccion(seleccion.getIdSeleccion());
        if (contracte == null) {
            response.sendRedirect("/AssistentPersonal/requests"); return;
        }

        APRequest solicitud = apRequestDao.getAPRequest(idSolicitud);
        UsuariOVI usuari =
                usuariOVIDao.getUsuariOVI(solicitud.getIdUsuarioOvi());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "inline; filename=\"contrato_" + idSolicitud + ".pdf\"");

        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        com.itextpdf.text.pdf.PdfWriter.getInstance(
                doc, response.getOutputStream());
        doc.open();

        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, TITLE_FONT_SIZE,
                com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, BODY_FONT_SIZE,
                com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, BODY_FONT_SIZE);

        doc.add(new com.itextpdf.text.Paragraph(
                "CONTRACTE D'ASSISTÈNCIA PERSONAL", titleFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "SgOVI - Servei de Gestió OVI\n\n"));
        doc.add(new com.itextpdf.text.Chunk(
                new com.itextpdf.text.pdf.draw.LineSeparator()));
        doc.add(new com.itextpdf.text.Paragraph("\n"));

        doc.add(new com.itextpdf.text.Paragraph(
                "Data d'inici: " + contracte.getFechaInicio(), normalFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Data de fi: " + (contracte.getFechaFin() != null
                        ? contracte.getFechaFin() : "Indefinida"), normalFont));
        doc.add(new com.itextpdf.text.Paragraph("\n"));

        doc.add(new com.itextpdf.text.Paragraph("USUARI OVI", boldFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Nom: " + usuari.getNombre() + " " + usuari.getApellidos(),
                normalFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Email: " + usuari.getEmail(), normalFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Telèfon: " + usuari.getTelefono(), normalFont));
        doc.add(new com.itextpdf.text.Paragraph("\n"));

        doc.add(new com.itextpdf.text.Paragraph("ASSISTENT PERSONAL", boldFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Nom: " + ap.getNombre() + " " + ap.getApellidos(), normalFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Email: " + ap.getEmail(), normalFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Telèfon: " + ap.getTelefono(), normalFont));
        doc.add(new com.itextpdf.text.Paragraph("\n"));

        doc.add(new com.itextpdf.text.Paragraph("SERVEI", boldFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Tipus: " + solicitud.getTipoAsistencia(), normalFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Província: " + solicitud.getProximidad(), normalFont));
        if (solicitud.getPreferencias() != null
                && !solicitud.getPreferencias().isEmpty()) {
            doc.add(new com.itextpdf.text.Paragraph(
                    "Preferències: " + solicitud.getPreferencias(), normalFont));
        }

        doc.close();
    }
}