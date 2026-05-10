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
     * @return the name of the view to render the list of APRequest entities
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listAPRequest(Model model) {
        model.addAttribute("requests", apRequestDao.getAPRequests());
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
        List<AssistentPersonal> candidatos =
                assistentPersonalDao.getAssistentsPersonalsByEstado("aceptado");
        model.addAttribute("solicitud", solicitud);
        model.addAttribute("candidatos", candidatos);
        return "APRequest/aprobar";
    }

    /**
     * Endpoint to process the approval of an APRequest.
     * @param idSolicitud the ID of the APRequest to be approved
     * @return a redirect to the list of APRequest entities after approval
     */
    @RequestMapping(value = "/aprobar", method = RequestMethod.POST)
    public String processAprobar(@RequestParam int idSolicitud) {
        apRequestDao.updateEstado(idSolicitud, "Aprobada");
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
     * Shows the list of candidates (assistants) who applied for a request.
     * @param id request identifier
     * @param model model for the view
     * @param session session data
     * @return the candidate list view or redirect to login
     */
    @RequestMapping(value = "/mylistChooseAP/{id}", method = RequestMethod.GET)
    public String mylistChooseAP(@PathVariable int id, Model model, HttpSession session) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) {
            return "redirect:/login";
        }

        APRequest request = apRequestDao.getAPRequest(id);
        if (request == null) {
            return "redirect:/APRequest/mylist";
        }

        // Obtener todas las selecciones para esta solicitud
        List<Selection> selecciones = selectionDao.getSelectionsBySolicitud(id);

        // Obtener los asistentes correspondientes
        List<AssistentPersonal> candidatos = new ArrayList<>();
        for (Selection s : selecciones) {
            AssistentPersonal ap = assistentPersonalDao.getAssistentPersonal(s.getIdAsistente());
            if (ap != null) {
                candidatos.add(ap);
            }
        }

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
        if (usuari == null) {
            return "redirect:/login";
        }

        AssistentPersonal assistent = assistentPersonalDao.getAssistentPersonal(idAsistente);
        if (assistent == null) {
            return "redirect:/APRequest/mylistChooseAP/" + idSolicitud;
        }

        model.addAttribute("assistent", assistent);
        model.addAttribute("idSolicitud", idSolicitud);
        return "APRequest/mylistChooseAPDetails";
    }

    /**
     * Processes the acceptance of an assistant for a request.
     * Creates a RegistreContracte, removes other candidates' selections.
     * @param idSolicitud request identifier
     * @param idAsistente chosen assistant identifier
     * @param session session data
     * @return redirect to mylist
     */
    @RequestMapping(value = "/acceptarAssistent/{idSolicitud}/{idAsistente}",
            method = RequestMethod.GET)
    public String acceptarAssistent(
            @PathVariable int idSolicitud,
            @PathVariable String idAsistente,
            HttpSession session) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) {
            return "redirect:/login";
        }

        // 1. Comprobar si esta solicitud ya tiene un contrato — si es así, no hacer nada
        List<Selection> todasSelecciones = selectionDao.getSelectionsBySolicitud(idSolicitud);
        for (Selection s : todasSelecciones) {
            RegistreContracte existente = registreContracteDao
                    .getRegistreContracteBySeleccion(s.getIdSeleccion());
            if (existente != null) {
                // Ya hay contrato, no crear otro
                return "redirect:/APRequest/mylist";
            }
        }

        // 2. Encontrar la selección del asistente elegido
        Selection seleccionElegida = null;
        for (Selection s : todasSelecciones) {
            if (s.getIdAsistente().equals(idAsistente)) {
                seleccionElegida = s;
                break;
            }
        }
        if (seleccionElegida == null) {
            return "redirect:/APRequest/mylist";
        }

        // 3. Crear el contrato solo para la selección elegida
        int nextIdContrato = registreContracteDao.getRegistresContractes().stream()
                .mapToInt(RegistreContracte::getIdContrato)
                .max().orElse(0) + 1;

        RegistreContracte contracte = new RegistreContracte();
        contracte.setIdContrato(nextIdContrato);
        contracte.setIdSeleccion(seleccionElegida.getIdSeleccion());
        contracte.setFechaInicio(java.time.LocalDate.now());
        registreContracteDao.addRegistreContracte(contracte);

        // 4. Borrar SOLO las selecciones que NO tienen contrato
        for (Selection s : todasSelecciones) {
            if (s.getIdSeleccion() != seleccionElegida.getIdSeleccion()) {
                RegistreContracte c = registreContracteDao
                        .getRegistreContracteBySeleccion(s.getIdSeleccion());
                if (c == null) {
                    // Solo borramos si no tiene contrato
                    selectionDao.deleteSelectionPorId(s.getIdSeleccion());
                }
            }
        }

        // 5. Actualizar estado — usa el valor exacto que acepta tu BD
        // Cambia "Tancada amb contracte" por lo que devuelva el constraint
        apRequestDao.updateEstado(idSolicitud, "Cerrada con contrato");

        return "redirect:/APRequest/mylist";
    }
}
