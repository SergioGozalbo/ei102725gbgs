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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

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
     * DAO for AssistentPersonal entities.
     */
    private final AssistentPersonalDaoImpl assistentPersonalDao;

    /**
     * Validator for AssistentPersonal entities.
     */
    private final AssistentPersonalValidator assistentPersonalValidator;

    /**
     * DAO for APRequest entities.
     */
    private final APRequestDaoImpl apRequestDao;

    /**
     * DAO for Selection entities.
     */
    private final SelectionDaoImpl selectionDao;

    /**
     * DAO for UsuariOVI entities.
     */
    private final UsuariOVIDaoImpl usuariOVIDao;

    /**
     * DAO for RegistreContracte entities.
     */
    private final RegistreContracteDaoImpl registreContracteDao;

    /**
     * Font size for the title.
     */
    private static final int TITLE_FONT_SIZE = 20;

    /**
     * Font size for PDF body text.
     */
    private static final int BODY_FONT_SIZE = 12;

    /**
     * Constructor with dependencies injected by Spring.
     * @param assistentPersonalDao DAO
     * @param assistentPersonalValidator Validator
     * @param apRequestDao DAO
     * @param selectionDao DAO
     * @param usuariOVIDao DAO
     * @param registreContracteDao DAO
     */
    @Autowired
    public AssistentPersonalController(AssistentPersonalDaoImpl assistentPersonalDao,
                                       AssistentPersonalValidator assistentPersonalValidator,
                                       APRequestDaoImpl apRequestDao,
                                       SelectionDaoImpl selectionDao,
                                       UsuariOVIDaoImpl usuariOVIDao,
                                       RegistreContracteDaoImpl registreContracteDao) {
        this.assistentPersonalDao = assistentPersonalDao;
        this.assistentPersonalValidator = assistentPersonalValidator;
        this.apRequestDao = apRequestDao;
        this.selectionDao = selectionDao;
        this.usuariOVIDao = usuariOVIDao;
        this.registreContracteDao = registreContracteDao;
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
     * @param redirectAttributes attributes for messages
     * @return redirect to the list view
     */
    @RequestMapping(value = "/delete/{idAsistente}")
    public String processDelete(@PathVariable String idAsistente,
                                org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            assistentPersonalDao.deleteAssistentPersonalPorId(idAsistente);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMsg",
                    "No es pot eliminar aquest assistent perquè té contractes o seleccions associades.");
            return "redirect:/AssistentPersonal/list";
        }
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
        if (assistent == null) {
            return "redirect:/login";
        }

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
        if (assistent == null) {
            return "redirect:/login";
        }

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
        if (bindingResult.hasErrors()) {
            return "AssistentPersonal/assistentProfileEdit";
        }

        assistentPersonalDao.updateAssistentPersonal(assistentPersonal);

        // Actualizar también la sesión con los nuevos datos
        session.setAttribute("assistentPersonal", assistentPersonal);

        return "redirect:profile";
    }

    /**
     * Shows the requests where this assistant has been chosen by a user.
     * @param model model for the view
     * @param session HTTP session containing the logged-in assistant
     * @return the view or redirect to login
     */
    @RequestMapping("/requests")
    public String myAssignedRequests(Model model, HttpSession session) {
        AssistentPersonal ap =
                (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) {
            return "redirect:/login";
        }

        // Buscar todas las selecciones donde este asistente fue elegido
        List<Selection> misSelecciones =
                selectionDao.getSelectionsByAsistente(ap.getIdAsistente());

        List<APRequest> solicitudesAsignadas = new ArrayList<>();
        List<RegistreContracte> todosLosContratos =
                registreContracteDao.getRegistresContractes();

        for (Selection s : misSelecciones) {
            // Solo incluir si tiene contrato (es decir, el usuario lo eligió de verdad)
            boolean tieneContrato = todosLosContratos.stream()
                    .anyMatch(c -> c.getIdSeleccion() == s.getIdSeleccion());
            if (tieneContrato) {
                APRequest req = apRequestDao.getAPRequest(s.getIdSolicitud());
                if (req != null) {
                    solicitudesAsignadas.add(req);
                }
            }
        }

        model.addAttribute("requests", solicitudesAsignadas);
        model.addAttribute("usuarios", usuariOVIDao.getUsuariosOVI());
        return "AssistentPersonal/requestList";
    }

    /**
     * Generates and downloads the PDF contract for a given request.
     * @param idSolicitud request identifier
     * @param session session data
     * @param response HTTP response to write the PDF to
     */
    @RequestMapping(value = "/contrato/{idSolicitud}", method = RequestMethod.GET)
    public void verContrato(@PathVariable int idSolicitud,
                            HttpSession session,
                            jakarta.servlet.http.HttpServletResponse response) throws Exception {

        AssistentPersonal ap =
                (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) {
            response.sendRedirect("/login");
            return;
        }

        // Buscar la selección de este asistente para esta solicitud
        Selection seleccion = null;
        for (Selection s : selectionDao.getSelectionsByAsistente(ap.getIdAsistente())) {
            if (s.getIdSolicitud() == idSolicitud) {
                seleccion = s;
                break;
            }
        }
        if (seleccion == null) {
            response.sendRedirect("/AssistentPersonal/requests");
            return;
        }

        RegistreContracte contracte = registreContracteDao
                .getRegistreContracteBySeleccion(seleccion.getIdSeleccion());
        if (contracte == null) {
            response.sendRedirect("/AssistentPersonal/requests");
            return;
        }

        APRequest solicitud = apRequestDao.getAPRequest(idSolicitud);
        UsuariOVI usuari = usuariOVIDao.getUsuariOVI(solicitud.getIdUsuarioOvi());

        // Generar PDF en memoria
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "inline; filename=\"contrato_" + idSolicitud + ".pdf\"");

        com.itextpdf.text.Document document =
                new com.itextpdf.text.Document();
        com.itextpdf.text.pdf.PdfWriter.getInstance(
                document, response.getOutputStream());
        document.open();

        // Título
        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, TITLE_FONT_SIZE,
            com.itextpdf.text.Font.BOLD);
        document.add(new com.itextpdf.text.Paragraph(
                "CONTRACTE D'ASSISTÈNCIA PERSONAL", titleFont));
        document.add(new com.itextpdf.text.Paragraph(
                "SgOVI - Servei de Gestió OVI\n\n"));

        // Línea separadora
        com.itextpdf.text.pdf.draw.LineSeparator line =
                new com.itextpdf.text.pdf.draw.LineSeparator();
        document.add(new com.itextpdf.text.Chunk(line));
        document.add(new com.itextpdf.text.Paragraph("\n"));

        // Datos del contrato
        com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, BODY_FONT_SIZE,
            com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, BODY_FONT_SIZE);

        document.add(new com.itextpdf.text.Paragraph(
                "Data d'inici: " + contracte.getFechaInicio(), normalFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Data de fi: " + (contracte.getFechaFin() != null
                        ? contracte.getFechaFin() : "Indefinida"), normalFont));
        document.add(new com.itextpdf.text.Paragraph("\n"));

        document.add(new com.itextpdf.text.Paragraph("USUARI OVI", boldFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Nom: " + usuari.getNombre() + " " + usuari.getApellidos(),
                normalFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Email: " + usuari.getEmail(), normalFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Telèfon: " + usuari.getTelefono(), normalFont));
        document.add(new com.itextpdf.text.Paragraph("\n"));

        document.add(new com.itextpdf.text.Paragraph("ASSISTENT PERSONAL", boldFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Nom: " + ap.getNombre() + " " + ap.getApellidos(), normalFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Email: " + ap.getEmail(), normalFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Telèfon: " + ap.getTelefono(), normalFont));
        document.add(new com.itextpdf.text.Paragraph("\n"));

        document.add(new com.itextpdf.text.Paragraph("SERVEI", boldFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Tipus d'assistència: " + solicitud.getTipoAsistencia(), normalFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Província: " + solicitud.getProximidad(), normalFont));
        if (solicitud.getPreferencias() != null
                && !solicitud.getPreferencias().isEmpty()) {
            document.add(new com.itextpdf.text.Paragraph(
                    "Preferències: " + solicitud.getPreferencias(), normalFont));
        }

        document.close();
    }

}
