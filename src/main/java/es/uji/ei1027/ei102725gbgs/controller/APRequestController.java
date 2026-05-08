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

@Component
class APRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return APRequest.class.equals(clazz);
    }

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



@Controller
@RequestMapping("/APRequest")
public class APRequestController {

    private final APRequestDaoImpl apRequestDao;
    private final UsuariOVIDaoImpl usuariOVIDao;
    private final AssistentPersonalDaoImpl assistentPersonalDao;
    private final APRequestValidator apRequestValidator;

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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listAPRequest(Model model) {
        model.addAttribute("requests", apRequestDao.getAPRequests());
        return "APRequest/list";
    }

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

    @RequestMapping(value = "/update/{idSolicitud}", method = RequestMethod.GET)
    public String editAPRequest(Model model, @PathVariable int idSolicitud) {
        model.addAttribute("apRequest", apRequestDao.getAPRequest(idSolicitud));
        model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
        model.addAttribute("provincias", getListaProvincias());
        return "APRequest/update";
    }

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

    @RequestMapping(value = "/delete/{idSolicitud}", method = RequestMethod.GET)
    public String processDelete(@PathVariable int idSolicitud) {
        apRequestDao.deleteAPRequestPorId(idSolicitud);
        return "redirect:/APRequest/list";
    }

    @RequestMapping(value = "/aprobar/{id}", method = RequestMethod.GET)
    public String mostrarAprobar(Model model, @PathVariable int id) {
        APRequest solicitud = apRequestDao.getAPRequest(id);
        List<AssistentPersonal> candidatos =
                assistentPersonalDao.getAssistentsPersonalsByEstado("aceptado");
        model.addAttribute("solicitud", solicitud);
        model.addAttribute("candidatos", candidatos);
        return "APRequest/aprobar";
    }

    @RequestMapping(value = "/aprobar", method = RequestMethod.POST)
    public String processAprobar(@RequestParam int idSolicitud) {
        apRequestDao.updateEstado(idSolicitud, "Aprobada");
        return "redirect:/APRequest/list";
    }
}
