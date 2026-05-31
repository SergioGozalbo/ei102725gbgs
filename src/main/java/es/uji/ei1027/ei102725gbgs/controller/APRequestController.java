package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.APRequestDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.AssistentPersonalDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.SelectionDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.RegistreContracteDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.APRequest;
import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
import es.uji.ei1027.ei102725gbgs.model.Selection;
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import es.uji.ei1027.ei102725gbgs.model.RegistreContracte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * APRequest validator class to validate APRequest entities before processing them in the controller.
 */
@Component
class APRequestValidator implements Validator {
    /**
     * Checks whether this validator supports APRequest.
     * @param clazz class to check
     * @return true if supported
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return APRequest.class.equals(clazz);
    }

    /**
     * Validates an APRequest.
     * @param obj object to validate
     * @param errors validation errors
     */
    @Override
    public void validate(Object obj, Errors errors) {
        APRequest request = (APRequest) obj;
        if (request.getIdUsuarioOvi() == null
                || request.getIdUsuarioOvi().trim().isEmpty()) {
            errors.rejectValue("idUsuarioOvi", "obligatori",
                    "L'ID d'usuari OVI és obligatori");
        }
        if (request.getTipoAsistencia() == null
                || request.getTipoAsistencia().trim().isEmpty()) {
            errors.rejectValue("tipoAsistencia", "obligatori",
                    "El tipus d'assistència és obligatori");
        }
    }
}



/**
 * Controller class for managing APRequest entities.
 * It provides endpoints for listing, adding, updating, and deleting APRequest entities,
 * as well as specific endpoints for approving requests and listing requests by user.
 */
@Controller
@RequestMapping("/APRequest")
public class APRequestController {

    /**
     * DAO for APRequest entities.
     */
    private final APRequestDaoImpl apRequestDao;

    /**
     * DAO for UsuariOVI entities.
     */
    private final UsuariOVIDaoImpl usuariOVIDao;

    /**
     * DAO for AssistentPersonal entities.
     */
    private final AssistentPersonalDaoImpl assistentPersonalDao;

    /**
     * Validator for APRequest entities.
     */
    private final APRequestValidator apRequestValidator;

    /**
     * DAO for Selection entities.
     */
    private final SelectionDaoImpl selectionDao;

    /**
     * DAO for RegistreContracte entities.
     */
    private final RegistreContracteDaoImpl registreContracteDao;


    /**
     * Constructor for APRequestController.
    * @param apRequestDao DAO for APRequest entities; must not be {@code null}
     * @param usuariOVIDao DAO for UsuariOVI entities; must not be {@code null}
     * @param assistentPersonalDao DAO for AssistentPersonal entities; must not be {@code null}
     * @param apRequestValidator Validator for APRequest entities; must not be {@code null}
     * @param selectionDao DAO for Selection entities; must not be {@code null}
     * @param registreContracteDao DAO for RegistreContracte entities; must not be {@code null}
     */
    @Autowired
    public APRequestController(APRequestDaoImpl apRequestDao,
                                UsuariOVIDaoImpl usuariOVIDao,
                                AssistentPersonalDaoImpl assistentPersonalDao,
                                APRequestValidator apRequestValidator,
                               SelectionDaoImpl selectionDao,
                               RegistreContracteDaoImpl registreContracteDao) {
        this.apRequestDao = apRequestDao;
        this.usuariOVIDao = usuariOVIDao;
        this.assistentPersonalDao = assistentPersonalDao;
        this.apRequestValidator = apRequestValidator;
        this.selectionDao = selectionDao;
        this.registreContracteDao = registreContracteDao;
    }

    private List<String> getListaProvincias() {
        return Arrays.asList(
                "A Coruña", "Álava", "Albacete", "Alicante", "Almería", "Asturias", "Ávila", "Badajoz", "Baleares", "Barcelona", "Burgos", "Cáceres", "Cádiz", "Cantabria", "Castellón", "Ciudad Real", "Córdoba", "Cuenca", "Girona", "Granada", "Guadalajara", "Guipúzcoa", "Huelva", "Huesca", "Jaén", "La Rioja", "Las Palmas", "León", "Lleida", "Lugo", "Madrid", "Málaga", "Murcia", "Navarra", "Ourense", "Palencia", "Pontevedra", "Salamanca", "Segovia", "Sevilla", "Soria", "Tarragona", "Santa Cruz de Tenerife", "Teruel", "Toledo", "Valencia", "Valladolid", "Vizcaya", "Zamora", "Zaragoza");
    }

    /**
     * Endpoint to list all APRequest entities.
     * @param model the Model object to pass data to the view for rendering the list of APRequest entities
     * @param session the HttpSession object to access session attributes for user authentication
     * @return the name of the view to render the list of APRequest entities
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listAPRequest(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/login";
        model.addAttribute("requests", apRequestDao.getAPRequests());
        model.addAttribute("usuarios", usuariOVIDao.getUsuariosOVI()); // añade esto
        return "APRequest/list";
    }

    /**
     * Endpoint to show the form for adding a new APRequest.
     * @param model the Model object to pass data to the
     * @param session the HttpSession object to access session attributes for user authentication
     * @return the name of the view to render the form for adding a new APRequest
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addAPRequest(Model model, HttpSession session) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) {
            return "redirect:/login";
        }

        APRequest request = new APRequest();
        request.setEstado("En revisión");
        request.setIdUsuarioOvi(usuari.getIdUsuario());
        model.addAttribute("apRequest", request);
        model.addAttribute("provincias", getListaProvincias());
        return "APRequest/add";
    }

    /**
     * Endpoint to list APRequest entities for the currently logged-in user.
     * @param model the Model object to pass data to the view for rendering the list of APRequest entities
     * @param session the HttpSession object to access session attributes for user authentication
     * @return the list of APRequest entities for the currently logged-in user
     */
    @RequestMapping(value = "/mylist", method = RequestMethod.GET)
    public String listMyRequests(Model model, HttpSession session) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) {
            return "redirect:/login";
        }

        List<APRequest> requests = apRequestDao.getAPRequestsByUsuari(usuari.getIdUsuario());
        List<RegistreContracte> todosContratos = registreContracteDao.getRegistresContractes();

        // Calcular qué solicitudes tienen contrato y cuál es el asistente elegido
        java.util.Map<Integer, String> asistenteElegidoPorSolicitud = new java.util.HashMap<>();

        for (APRequest req : requests) {
            List<Selection> selecciones = selectionDao.getSelectionsBySolicitud(req.getIdSolicitud());
            for (Selection s : selecciones) {
                boolean tieneContrato = todosContratos.stream()
                        .anyMatch(c -> c.getIdSeleccion() == s.getIdSeleccion());
                if (tieneContrato) {
                    asistenteElegidoPorSolicitud.put(req.getIdSolicitud(), s.getIdAsistente());
                    break;
                }
            }
        }

        model.addAttribute("requests", requests);
        model.addAttribute("usuari", usuari);
        model.addAttribute("asistenteElegido", asistenteElegidoPorSolicitud);
        return "APRequest/mylist";
    }

    /**
     * Endpoint to process the submission of a new APRequest.
     * @param apRequest the APRequest object populated with data from the form submission
     * @param bindingResult the BindingResult object to hold validation errors for the APRequest object
     * @param model the Model object to pass data to the view in case of validation errors
     * @return the name of the view to render the form for adding a new APRequest
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
        public String processAddSubmit(
            @ModelAttribute("apRequest") APRequest apRequest,
            BindingResult bindingResult,
            Model model) {
        int nextId = apRequestDao.getAPRequests().stream()
                .mapToInt(APRequest::getIdSolicitud).max().orElse(0) + 1;

        apRequest.setIdSolicitud(nextId);
        apRequestValidator.validate(apRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
            model.addAttribute("provincias", getListaProvincias());
            return "APRequest/add";
        }
        apRequestDao.addAPRequest(apRequest);
        return "redirect:/APRequest/mylist";
    }

    /**
     * Endpoint to show the form for editing an existing APRequest.
     * @param model the Model object to pass data to the view for rendering the form for editing an existing APRequest
     * @param idSolicitud the ID of the APRequest to be edited
     * @return the name of the view to render the form for editing an existing APRequest
     */
    @RequestMapping(value = "/update/{idSolicitud}", method = RequestMethod.GET)
    public String editAPRequest(Model model, @PathVariable int idSolicitud) {
        model.addAttribute("apRequest", apRequestDao.getAPRequest(idSolicitud));
        model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
        model.addAttribute("provincias", getListaProvincias());
        return "APRequest/update";
    }

    /**
     * Endpoint to process the submission of an updated APRequest.
     * @param apRequest the APRequest object populated with updated data from the form submission
     * @param bindingResult the BindingResult object to hold validation errors for the APRequest object
     * @param model the Model object to pass data to the view in case of validation errors
     * @return the name of the view to render the form for editing an existing APRequest
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
        public String processUpdateSubmit(
            @ModelAttribute("apRequest") APRequest apRequest,
            BindingResult bindingResult,
            Model model) {
        apRequestValidator.validate(apRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
            model.addAttribute("provincias", getListaProvincias());
            return "APRequest/update";
        }
        apRequestDao.updateAPRequest(apRequest);
        return "redirect:/APRequest/mylist";
    }

    /**
     * Endpoint to delete an existing APRequest.
     * @param idSolicitud the ID of the APRequest to be deleted
     * @return a redirect to the list of APRequest entities after deletion
     */
    @RequestMapping(value = "/delete/{idSolicitud}", method = RequestMethod.GET)
    public String processDelete(@PathVariable int idSolicitud) {
        apRequestDao.deleteAPRequestPorId(idSolicitud);
        return "redirect:/APRequest/mylist";
    }

    /**
     * Endpoint to show the form for approving an APRequest.
     * @param model the Model object to pass data to the view for rendering the form for approving an APRequest
     * @param id the ID of the APRequest to be approved
     * @return the name of the view to render the form for approving an APRequest
     */
    @RequestMapping(value = "/aprobar/{id}", method = RequestMethod.GET)
    public String mostrarAprobar(Model model, @PathVariable int id) {
        APRequest solicitud = apRequestDao.getAPRequest(id);
        model.addAttribute("solicitud", solicitud);
        return "APRequest/aprobar";
    }

    /**
     * Endpoint to process the approval of an APRequest.
     * @param idSolicitud the ID of the APRequest to be approved
     * @param estado the new state to set for the APRequest
     * @return a redirect to the list of APRequest entities after approval
     */
    @RequestMapping(value = "/aprobar", method = RequestMethod.POST)
    public String processAprobar(@RequestParam int idSolicitud, @RequestParam String estado) {
        apRequestDao.updateEstado(idSolicitud, estado);
        return "redirect:/APRequest/list";
    }

    /**
     * Shows the list of candidates for a specific APRequest.
     * @param id the ID of the APRequest to show candidates for
     * @param model the Model object to pass data to the view
     * @return the view for displaying candidates
     */
    @RequestMapping("/candidatos/{id}")
    public String verCandidatos(@PathVariable int id, Model model) {
        List<Selection> selecciones = selectionDao.getSelectionsBySolicitud(id);
        List<AssistentPersonal> asistentes = new ArrayList<>();
        for (Selection s : selecciones) {
            asistentes.add(assistentPersonalDao.getAssistentPersonal(s.getIdAsistente()));
        }
        model.addAttribute("candidatos", asistentes);
        model.addAttribute("idSolicitud", id);
        return "APRequest/verCandidatos";
    }

    /**
     * Shows available assistants for the user to choose from.
     * @param id the ID of the APRequest to choose an assistant for
     * @param model the Model object to pass data to the view
     * @param session the HttpSession object to access session attributes for user authentication
     * @return the view for displaying available assistants
     */
    @RequestMapping(value = "/mylistChooseAP/{id}", method = RequestMethod.GET)
    public String mylistChooseAP(@PathVariable int id, Model model, HttpSession session) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) return "redirect:/login";

        APRequest request = apRequestDao.getAPRequest(id);
        if (request == null) return "redirect:/APRequest/mylist";

        // Cargar TODOS los asistentes aprobados, sin filtrar por Selection
        List<AssistentPersonal> candidatos =
                assistentPersonalDao.getAssistentsPersonalsByEstado("Aceptado");

        model.addAttribute("request", request);
        model.addAttribute("candidatos", candidatos);
        return "APRequest/mylistChooseAP";
    }

    /**
     * Shows the details of a candidate assistant for a given request.
     * @param idSolicitud request identifier
     * @param idAsistente assistant identifier
     * @param model model for the view
     * @param session session data
     * @return the candidate details view or redirect to login
     */
    @RequestMapping(value = "/mylistChooseAPDetails/{idSolicitud}/{idAsistente}",
            method = RequestMethod.GET)
    public String mylistChooseAPDetails(
            @PathVariable int idSolicitud,
            @PathVariable String idAsistente,
            Model model, HttpSession session) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) return "redirect:/login";

        AssistentPersonal assistent =
                assistentPersonalDao.getAssistentPersonal(idAsistente);
        if (assistent == null)
            return "redirect:/APRequest/mylistChooseAP/" + idSolicitud;

        // Comprobar si ya tiene contrato
        List<Selection> selecciones =
                selectionDao.getSelectionsBySolicitud(idSolicitud);
        boolean yaElegido = false;
        for (Selection s : selecciones) {
            if (registreContracteDao.getRegistreContracteBySeleccion(
                    s.getIdSeleccion()) != null) {
                yaElegido = true;
                break;
            }
        }

        model.addAttribute("assistent", assistent);
        model.addAttribute("idSolicitud", idSolicitud);
        model.addAttribute("yaElegido", yaElegido);
        return "APRequest/mylistChooseAPDetails";
    }

    /**
     * Accepts an assistant directly.
     * Creates the Selection on the spot, then creates the contract.
     * @param idSolicitud request identifier
     * @param idAsistente assistant identifier
     * @param session session data
     * @return redirect to the user's request list
     */
    @RequestMapping(value = "/acceptarAssistent/{idSolicitud}/{idAsistente}",
            method = RequestMethod.GET)
    public String acceptarAssistent(
            @PathVariable int idSolicitud,
            @PathVariable String idAsistente,
            HttpSession session) {

        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) return "redirect:/login";

        // 1. Comprobar que esta solicitud no tenga ya un contrato
        List<Selection> seleccionesExistentes =
                selectionDao.getSelectionsBySolicitud(idSolicitud);
        for (Selection s : seleccionesExistentes) {
            if (registreContracteDao.getRegistreContracteBySeleccion(
                    s.getIdSeleccion()) != null) {
                return "redirect:/APRequest/mylist"; // ya tiene contrato
            }
        }

        // 2. Crear la Selection en el momento de la elección
        int nextIdSelection = selectionDao.getSelections().stream()
                .mapToInt(Selection::getIdSeleccion).max().orElse(0) + 1;

        Selection seleccion = new Selection();
        seleccion.setIdSeleccion(nextIdSelection);
        seleccion.setIdSolicitud(idSolicitud);
        seleccion.setIdAsistente(idAsistente);
        selectionDao.addSelection(seleccion);

        // 3. Crear el contrato para esa selección
        int nextIdContrato = registreContracteDao.getRegistresContractes().stream()
                .mapToInt(RegistreContracte::getIdContrato).max().orElse(0) + 1;

        RegistreContracte contracte = new RegistreContracte();
        contracte.setIdContrato(nextIdContrato);
        contracte.setIdSeleccion(nextIdSelection);
        contracte.setFechaInicio(java.time.LocalDate.now());
        registreContracteDao.addRegistreContracte(contracte);

        // 4. Cerrar la solicitud
        apRequestDao.updateEstado(idSolicitud, "Cerrada con contrato");

        return "redirect:/APRequest/mylist";
    }

    /**
     * Endpoint to show the form for adding a new APRequest by an admin.
     * @param model the Model object to pass data to the view
     * @return the name of the view to render the form for adding a new APRequest by an admin
     */
    @RequestMapping(value = "/addAdmin", method = RequestMethod.GET)
    public String addAPRequestAdmin(Model model) {
        APRequest request = new APRequest();
        request.setEstado("En revisión");
        model.addAttribute("apRequest", request);
        model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
        model.addAttribute("provincias", getListaProvincias());
        return "APRequest/addAdmin";
    }

    /**
     * Endpoint to process the submission of a new APRequest by an admin.
     * @param apRequest the APRequest object populated with data from the form submission
     * @param br the BindingResult object to hold validation errors for the APRequest object
     * @param model the Model object to pass data to the view
     * @return the name of the view to render the form for adding a new APRequest by an admin
     */
    @RequestMapping(value = "/addAdmin", method = RequestMethod.POST)
    public String processAddAdmin(@ModelAttribute("apRequest") APRequest apRequest, BindingResult br, Model model) {
        int nextId = apRequestDao.getAPRequests().stream().mapToInt(APRequest::getIdSolicitud).max().orElse(0) + 1;
        apRequest.setIdSolicitud(nextId);
        apRequestValidator.validate(apRequest, br);
        if (br.hasErrors()) {
            model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
            model.addAttribute("provincias", getListaProvincias());
            return "APRequest/addAdmin";
        }
        apRequestDao.addAPRequest(apRequest);
        return "redirect:/APRequest/list";
    }

    /**
     * Endpoint to show the form for editing an existing APRequest by an admin.
     * @param model the Model object to pass data to the view
     * @param idSolicitud the ID of the APRequest to be edited
     * @return the name of the view to render the form for editing an existing APRequest by an admin
     */
    @RequestMapping(value = "/updateAdmin/{idSolicitud}", method = RequestMethod.GET)
    public String editAPRequestAdmin(Model model, @PathVariable int idSolicitud) {
        model.addAttribute("apRequest", apRequestDao.getAPRequest(idSolicitud));
        model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
        model.addAttribute("provincias", getListaProvincias());
        return "APRequest/updateAdmin";
    }

    /**
     * Endpoint to process the submission of an updated APRequest by an admin.
     * @param apRequest the APRequest object populated with updated data from the form submission
     * @param br the BindingResult object to hold validation errors for the APRequest object
     * @param model the Model object to pass data to the view
     * @return the name of the view to render the form for editing an existing APRequest by an admin
     */
    @RequestMapping(value = "/updateAdmin", method = RequestMethod.POST)
    public String processUpdateAdmin(@ModelAttribute("apRequest") APRequest apRequest, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
            return "APRequest/updateAdmin";
        }
        apRequestDao.updateAPRequest(apRequest);
        return "redirect:/APRequest/list";
    }
    /**
     * Generates and downloads the PDF contract for a given request (OVI user side).
     * @param idSolicitud request identifier
     * @param session session data
     * @param response HTTP response
     */
    @RequestMapping(value = "/contrato/{idSolicitud}", method = RequestMethod.GET)
    public void verContrato(@PathVariable int idSolicitud,
                            HttpSession session,
                            jakarta.servlet.http.HttpServletResponse response) throws Exception {

        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) {
            response.sendRedirect("/login");
            return;
        }

        // Buscar la selección de esta solicitud
        List<Selection> selecciones = selectionDao.getSelectionsBySolicitud(idSolicitud);
        Selection seleccion = null;
        for (Selection s : selecciones) {
            if (registreContracteDao.getRegistreContracteBySeleccion(
                    s.getIdSeleccion()) != null) {
                seleccion = s;
                break;
            }
        }
        if (seleccion == null) {
            response.sendRedirect("/APRequest/mylist");
            return;
        }

        RegistreContracte contracte = registreContracteDao
                .getRegistreContracteBySeleccion(seleccion.getIdSeleccion());
        APRequest solicitud = apRequestDao.getAPRequest(idSolicitud);
        AssistentPersonal assistent = assistentPersonalDao
                .getAssistentPersonal(seleccion.getIdAsistente());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "inline; filename=\"contrato_" + idSolicitud + ".pdf\"");

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        com.itextpdf.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 20,
                com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12,
                com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12);

        document.add(new com.itextpdf.text.Paragraph(
                "CONTRACTE D'ASSISTÈNCIA PERSONAL", titleFont));
        document.add(new com.itextpdf.text.Paragraph("SgOVI - Servei de Gestió OVI\n\n"));
        document.add(new com.itextpdf.text.Chunk(
                new com.itextpdf.text.pdf.draw.LineSeparator()));
        document.add(new com.itextpdf.text.Paragraph("\n"));

        document.add(new com.itextpdf.text.Paragraph(
                "Data d'inici: " + contracte.getFechaInicio(), normalFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Data de fi: " + (contracte.getFechaFin() != null
                        ? contracte.getFechaFin() : "Indefinida"), normalFont));
        document.add(new com.itextpdf.text.Paragraph("\n"));

        document.add(new com.itextpdf.text.Paragraph("USUARI OVI", boldFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Nom: " + usuari.getNombre() + " " + usuari.getApellidos(), normalFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Email: " + usuari.getEmail(), normalFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Telèfon: " + usuari.getTelefono(), normalFont));
        document.add(new com.itextpdf.text.Paragraph("\n"));

        document.add(new com.itextpdf.text.Paragraph("ASSISTENT PERSONAL", boldFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Nom: " + assistent.getNombre() + " " + assistent.getApellidos(), normalFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Email: " + assistent.getEmail(), normalFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Telèfon: " + assistent.getTelefono(), normalFont));
        document.add(new com.itextpdf.text.Paragraph("\n"));

        document.add(new com.itextpdf.text.Paragraph("SERVEI", boldFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Tipus: " + solicitud.getTipoAsistencia(), normalFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Província: " + solicitud.getProximidad(), normalFont));
        if (solicitud.getPreferencias() != null && !solicitud.getPreferencias().isEmpty()) {
            document.add(new com.itextpdf.text.Paragraph(
                    "Preferències: " + solicitud.getPreferencias(), normalFont));
        }

        document.close();
    }

    /**
     * Lists all contracts for the admin.
     * @param model model for the view
     * @param session session data
     * @return the contracts list view
     */
    @RequestMapping(value = "/contratos", method = RequestMethod.GET)
    public String listarContratos(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/login";

        List<RegistreContracte> contratos =
                registreContracteDao.getRegistresContractes();

        // Para cada contrato, enriquecer con datos de solicitud y asistente
        List<java.util.Map<String, Object>> contratosEnriquecidos = new ArrayList<>();
        for (RegistreContracte c : contratos) {
            // Buscar la selección para obtener el asistente
            // Reutilizamos selectionDao que ya está inyectado
            List<Selection> sels = selectionDao.getSelections();
            Selection sel = null;
            for (Selection s : sels) {
                if (s.getIdSeleccion() == c.getIdSeleccion()) {
                    sel = s;
                    break;
                }
            }

            java.util.Map<String, Object> row = new java.util.HashMap<>();
            row.put("contrato", c);

            if (sel != null) {
                APRequest req = apRequestDao.getAPRequest(sel.getIdSolicitud());
                AssistentPersonal ap = assistentPersonalDao
                        .getAssistentPersonal(sel.getIdAsistente());
                UsuariOVI ovi = req != null
                        ? usuariOVIDao.getUsuariOVI(req.getIdUsuarioOvi()) : null;

                row.put("solicitud", req);
                row.put("assistent", ap);
                row.put("usuari", ovi);
            }

            contratosEnriquecidos.add(row);
        }

        model.addAttribute("contratos", contratosEnriquecidos);
        return "APRequest/contratos";
    }

    /**
     * Generates the PDF of a contract by contract ID (admin access).
     * @param idContrato contract identifier
     * @param session session data
     * @param response HTTP response
     */
    @RequestMapping(value = "/contratoPdf/{idContrato}", method = RequestMethod.GET)
    public void contratoPdfAdmin(@PathVariable int idContrato,
                                 HttpSession session,
                                 jakarta.servlet.http.HttpServletResponse response) throws Exception {

        if (session.getAttribute("admin") == null) {
            response.sendRedirect("/login");
            return;
        }

        RegistreContracte contracte =
                registreContracteDao.getRegistreContracte(idContrato);
        if (contracte == null) {
            response.sendRedirect("/APRequest/contratos");
            return;
        }

        // Buscar selección
        Selection seleccion = null;
        for (Selection s : selectionDao.getSelections()) {
            if (s.getIdSeleccion() == contracte.getIdSeleccion()) {
                seleccion = s;
                break;
            }
        }

        APRequest solicitud = seleccion != null
                ? apRequestDao.getAPRequest(seleccion.getIdSolicitud()) : null;
        AssistentPersonal assistent = seleccion != null
                ? assistentPersonalDao.getAssistentPersonal(seleccion.getIdAsistente()) : null;
        UsuariOVI usuari = solicitud != null
                ? usuariOVIDao.getUsuariOVI(solicitud.getIdUsuarioOvi()) : null;

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "inline; filename=\"contrato_" + idContrato + ".pdf\"");

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        com.itextpdf.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 20,
                com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12,
                com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12);

        document.add(new com.itextpdf.text.Paragraph(
                "CONTRACTE D'ASSISTÈNCIA PERSONAL", titleFont));
        document.add(new com.itextpdf.text.Paragraph("SgOVI - Servei de Gestió OVI\n\n"));
        document.add(new com.itextpdf.text.Chunk(
                new com.itextpdf.text.pdf.draw.LineSeparator()));
        document.add(new com.itextpdf.text.Paragraph("\n"));


        document.add(new com.itextpdf.text.Paragraph(
                "Data d'inici: " + contracte.getFechaInicio(), normalFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Data de fi: " + (contracte.getFechaFin() != null
                        ? contracte.getFechaFin() : "Indefinida"), normalFont));
        document.add(new com.itextpdf.text.Paragraph("\n"));

        if (usuari != null) {
            document.add(new com.itextpdf.text.Paragraph("USUARI OVI", boldFont));
            document.add(new com.itextpdf.text.Paragraph(
                    "Nom: " + usuari.getNombre() + " " + usuari.getApellidos(), normalFont));
            document.add(new com.itextpdf.text.Paragraph(
                    "Email: " + usuari.getEmail(), normalFont));
            document.add(new com.itextpdf.text.Paragraph("\n"));
        }

        if (assistent != null) {
            document.add(new com.itextpdf.text.Paragraph("ASSISTENT PERSONAL", boldFont));
            document.add(new com.itextpdf.text.Paragraph(
                    "Nom: " + assistent.getNombre() + " " + assistent.getApellidos(), normalFont));
            document.add(new com.itextpdf.text.Paragraph(
                    "Email: " + assistent.getEmail(), normalFont));
            document.add(new com.itextpdf.text.Paragraph("\n"));
        }

        if (solicitud != null) {
            document.add(new com.itextpdf.text.Paragraph("SERVEI", boldFont));
            document.add(new com.itextpdf.text.Paragraph(
                    "Tipus: " + solicitud.getTipoAsistencia(), normalFont));
            document.add(new com.itextpdf.text.Paragraph(
                    "Província: " + solicitud.getProximidad(), normalFont));
        }

        document.close();
    }
}
