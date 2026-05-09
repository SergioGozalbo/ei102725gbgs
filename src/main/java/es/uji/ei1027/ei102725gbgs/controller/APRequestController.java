package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.APRequestDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.AssistentPersonalDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.APRequest;
import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
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
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import jakarta.servlet.http.HttpSession;

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
     * Data access object for APRequest entities.
     */
    private final APRequestDaoImpl apRequestDao;

    /**
     * Data access object for UsuariOVI entities.
     */
    private final UsuariOVIDaoImpl usuariOVIDao;

    /**
     * Data access object for AssistentPersonal entities.
     */
    private final AssistentPersonalDaoImpl assistentPersonalDao;

    /**
     * Validator for APRequest entities to ensure that they meet the required criteria before being processed.
     */
    private final APRequestValidator apRequestValidator;

    /**
     * Constructor for APRequestController.
     * @param apRequestDao the APRequestDaoImpl instance for accessing APRequest data
     * @param usuariOVIDao the UsuariOVIDaoImpl instance for accessing UsuariOVI data
     * @param assistentPersonalDao the AssistentPersonalDaoImpl instance for accessing AssistentPersonal data
     * @param apRequestValidator the APRequestValidator instance for validating APRequest entities
     */
    @Autowired
    public APRequestController(APRequestDaoImpl apRequestDao,
                                UsuariOVIDaoImpl usuariOVIDao,
                                AssistentPersonalDaoImpl assistentPersonalDao,
                                APRequestValidator apRequestValidator) {
        this.apRequestDao = apRequestDao;
        this.usuariOVIDao = usuariOVIDao;
        this.assistentPersonalDao = assistentPersonalDao;
        this.apRequestValidator = apRequestValidator;
    }

    private List<String> getListaProvincias() {
        return Arrays.asList(
            "Alicante", "Castellón", "Valencia",
        "Madrid", "Barcelona", "Murcia",
        "");
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

        model.addAttribute("requests",
                apRequestDao.getAPRequestsByUsuari(usuari.getIdUsuario()));
        model.addAttribute("usuari", usuari);
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
        return "redirect:/APRequest/list";
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
        return "redirect:/APRequest/list";
    }

    /**
     * Endpoint to delete an existing APRequest.
     * @param idSolicitud the ID of the APRequest to be deleted
     * @return a redirect to the list of APRequest entities after deletion
     */
    @RequestMapping(value = "/delete/{idSolicitud}", method = RequestMethod.GET)
    public String processDelete(@PathVariable int idSolicitud) {
        apRequestDao.deleteAPRequestPorId(idSolicitud);
        return "redirect:/APRequest/list";
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
}
