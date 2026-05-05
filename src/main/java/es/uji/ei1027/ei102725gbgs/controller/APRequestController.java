package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.APRequestDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.APRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

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
		if (request.getIdUsuarioOvi() == null || request.getIdUsuarioOvi().trim().isEmpty()) {
			errors.rejectValue("idUsuarioOvi", "obligatori", "L'ID d'usuari OVI és obligatori");
		}
		if (request.getTipoAsistencia() == null || request.getTipoAsistencia().trim().isEmpty()) {
			errors.rejectValue("tipoAsistencia", "obligatori", "El tipus d'assistència és obligatori");
		}
	}
}

@Controller
@RequestMapping("/APRequest")
public class APRequestController {

	private APRequestDaoImpl apRequestDao;
	private UsuariOVIDaoImpl usuariOVIDao;
	private APRequestValidator apRequestValidator;

	@Autowired
	public void setApRequestDao(APRequestDaoImpl apRequestDao) { this.apRequestDao = apRequestDao; }
	@Autowired
	public void setUsuariOVIDao(UsuariOVIDaoImpl usuariOVIDao) { this.usuariOVIDao = usuariOVIDao; }
	@Autowired
	public void setApRequestValidator(APRequestValidator apRequestValidator) { this.apRequestValidator = apRequestValidator; }

	private List<String> getListaProvincias() {
		return Arrays.asList("Alicante", "Castellón", "Valencia", "Madrid", "Barcelona", "Murcia", "");
	}

	@RequestMapping("/list")
	public String listAPRequest(Model model) {
		model.addAttribute("requests", apRequestDao.getAPRequests());
		return "APRequest/list";
	}

	@RequestMapping(value="/add")
	public String addAPRequest(Model model) {
		APRequest request = new APRequest();
		request.setEstado("En revisión");

		model.addAttribute("apRequest", request);
		model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
		model.addAttribute("provincias", getListaProvincias());
		return "APRequest/add";
	}

	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String processAddSubmit(@ModelAttribute("apRequest") APRequest apRequest,
								   BindingResult bindingResult, Model model) {

		// ASIGNACIÓN AUTOMÁTICA DEL ID AQUÍ
		int nextId = apRequestDao.getAPRequests().stream()
				.mapToInt(APRequest::getIdSolicitud)
				.max().orElse(0) + 1;
		apRequest.setIdSolicitud(nextId);

		apRequestValidator.validate(apRequest, bindingResult);

		if (bindingResult.hasErrors()) {
			// Si hay error, hay que volver a cargar las listas para el select
			model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
			model.addAttribute("provincias", getListaProvincias());
			return "APRequest/add";
		}

		apRequestDao.addAPRequest(apRequest);
		return "redirect:list";
	}

	@RequestMapping(value="/update/{idSolicitud}", method=RequestMethod.GET)
	public String editAPRequest(Model model, @PathVariable int idSolicitud) {
		model.addAttribute("apRequest", apRequestDao.getAPRequest(idSolicitud));
		model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
		model.addAttribute("provincias", getListaProvincias());
		return "APRequest/update";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String processUpdateSubmit(@ModelAttribute("apRequest") APRequest apRequest,
									  BindingResult bindingResult, Model model) {
		apRequestValidator.validate(apRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
			model.addAttribute("provincias", getListaProvincias());
			return "APRequest/update";
		}
		apRequestDao.updateAPRequest(apRequest);
		return "redirect:list";
	}

	@RequestMapping(value="/delete/{idSolicitud}")
	public String processDelete(@PathVariable int idSolicitud) {
		apRequestDao.deleteAPRequestPorId(idSolicitud);
		return "redirect:../list";
	}
}