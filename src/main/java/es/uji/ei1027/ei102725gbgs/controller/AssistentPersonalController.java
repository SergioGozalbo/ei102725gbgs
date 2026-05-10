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
     * @return redirect to the list view
     */
    @RequestMapping(value = "/delete/{idAsistente}")
    public String processDelete(@PathVariable String idAsistente) {
        assistentPersonalDao.deleteAssistentPersonalPorId(idAsistente);
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
     * Shows the list of available requests for the logged-in assistant, filtering out those already accepted by him.
     * @param model model for the view
     * @param session HTTP session containing the logged-in assistant
     * @return the view for available requests or redirect to login if not authenticated
     */
    @RequestMapping("/requests")
    public String listRequests(Model model, HttpSession session) {
        AssistentPersonal ap = (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) {
            return "redirect:/login";
        }

        // Obtener IDs de solicitudes que este asistente YA ha aceptado
        List<Integer> acceptedIds = selectionDao.getSelectionsByAsistente(ap.getIdAsistente())
                .stream().map(Selection::getIdSolicitud).toList();

        // Filtrar: Solo "Aprobada" y que NO estén en la lista de aceptadas por él
        List<APRequest> availableRequests = apRequestDao.getAPRequests().stream()
                .filter(r -> "Aprobada".equalsIgnoreCase(r.getEstado()) && !acceptedIds.contains(r.getIdSolicitud()))
                .toList();

        model.addAttribute("requests", availableRequests);
        model.addAttribute("usuarios", usuariOVIDao.getUsuariosOVI()); // Para sacar los nombres
        return "AssistentPersonal/requestList";
    }

    /**
     * Shows the details of a specific request, including whether the logged-in assistant has already accepted it.
     * @param id the ID of the request to show details for
     * @param model model for the view
     * @param session HTTP session containing the logged-in assistant
     * @return the view for request details or redirect to login if not authenticated
     */
    @RequestMapping("/requestDetails/{id}")
    public String requestDetails(@PathVariable int id, Model model, HttpSession session) {
        AssistentPersonal ap = (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) {
            return "redirect:/login";
        }

        APRequest request = apRequestDao.getAPRequest(id);
        if (request == null) {
            return "redirect:/AssistentPersonal/requests";
        }

        UsuariOVI usuario = usuariOVIDao.getUsuariOVI(request.getIdUsuarioOvi());
        if (usuario == null) {
            usuario = new UsuariOVI();
            usuario.setNombre("No disponible");
        }

        // Comprobar si ya aceptó esta solicitud
        boolean yaAceptada = selectionDao.getSelectionsByAsistente(ap.getIdAsistente())
                .stream().anyMatch(s -> s.getIdSolicitud() == id);

        model.addAttribute("request", request);
        model.addAttribute("usuario", usuario);
        model.addAttribute("yaAceptada", yaAceptada);
        return "AssistentPersonal/requestDetails";
    }

    /**
     * Shows the assistant's accepted requests and their contract status.
     * @param model model for the view
     * @param session HTTP session containing the logged-in assistant
     * @return the view for the assistant's requests or redirect to login if not authenticated
     */
    @RequestMapping("/myrequests")
    public String myRequests(Model model, HttpSession session) {
        AssistentPersonal ap = (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) {
            return "redirect:/login";
        }

        List<Selection> mySelections = selectionDao.getSelectionsByAsistente(ap.getIdAsistente());
        List<APRequest> myAcceptedRequests = new ArrayList<>();
        List<Integer> solicitudesConContrato = new ArrayList<>();

        List<RegistreContracte> todosLosContratos = registreContracteDao.getRegistresContractes();

        for (Selection s : mySelections) {
            APRequest req = apRequestDao.getAPRequest(s.getIdSolicitud());
            if (req != null) {
                myAcceptedRequests.add(req);

                // Comprobar si esta selección tiene contrato
                boolean tieneContrato = todosLosContratos.stream()
                        .anyMatch(c -> c.getIdSeleccion() == s.getIdSeleccion());
                if (tieneContrato) {
                    solicitudesConContrato.add(s.getIdSolicitud());
                }
            }
        }

        model.addAttribute("requests", myAcceptedRequests);
        model.addAttribute("selections", mySelections);
        model.addAttribute("solicitudesConContrato", solicitudesConContrato);
        model.addAttribute("usuarios", usuariOVIDao.getUsuariosOVI());

        return "AssistentPersonal/myrequests";
    }

    /**
     * Accepts a request by creating a new selection for the logged-in assistant and the specified request ID.
     * @param id the ID of the request to accept
     * @param session the HTTP session containing the logged-in assistant
     * @return redirect to the assistant's requests page or login if not authenticated
     */
    @RequestMapping(value = "/acceptRequest/{id}")
    public String acceptRequest(@PathVariable int id, HttpSession session) {
        AssistentPersonal ap = (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) {
            return "redirect:/login";
        }

        // Calcular siguiente ID
        int nextId = selectionDao.getSelections().stream()
                .mapToInt(Selection::getIdSeleccion)
                .max().orElse(0) + 1;

        Selection selection = new Selection();
        selection.setIdSeleccion(nextId);
        selection.setIdSolicitud(id);
        selection.setIdAsistente(ap.getIdAsistente());

        selectionDao.addSelection(selection);
        return "redirect:/AssistentPersonal/myrequests";
    }

    /**
     * Cancels a request by deleting the corresponding selection if it exists and has no contract.
     * @param id the ID of the request to cancel
     * @param session the HTTP session containing the logged-in assistant
     * @return redirect to the assistant's requests page or login if not authenticated
     */
    @RequestMapping("/cancelRequest/{id}")
    public String cancelRequest(@PathVariable int id, HttpSession session) {
        AssistentPersonal ap = (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (ap == null) {
            return "redirect:/login";
        }

        // 1. Buscamos la selección que corresponde a esta solicitud (Bucle tradicional)
        List<Selection> selecciones = selectionDao.getSelectionsByAsistente(ap.getIdAsistente());
        Selection miSeleccion = null;
        for (Selection s : selecciones) {
            if (s.getIdSolicitud() == id) {
                miSeleccion = s;
                break;
            }
        }

        if (miSeleccion != null) {
            // 2. Comprobar si existe un contrato para esta selección específica
            List<RegistreContracte> todosLosContratos = registreContracteDao.getRegistresContractes();
            boolean tieneContrato = false;
            for (RegistreContracte c : todosLosContratos) {
                if (c.getIdSeleccion() == miSeleccion.getIdSeleccion()) {
                    tieneContrato = true;
                    break;
                }
            }

            // 3. Solo borramos si no hay contrato (evita el Error 500)
            if (!tieneContrato) {
                selectionDao.deleteSelection(id, ap.getIdAsistente());
            }
        }

        return "redirect:/AssistentPersonal/myrequests";
    }
}
