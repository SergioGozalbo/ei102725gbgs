package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.stereotype.Component;
@Component
class UsuariOVIValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UsuariOVI.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		UsuariOVI usuario = (UsuariOVI) obj;

		// 1. Validar Email (formato básico)
		if (usuario.getEmail().trim().isEmpty()) {
			errors.rejectValue("email", "obligatori", "L'email és obligatori");
		} else if (!usuario.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			errors.rejectValue("email", "format", "El format de l'email no és vàlid");
		}

		// 2. Validar Teléfono (exactamente 9 dígitos)
		if (!usuario.getTelefono().matches("\\d{9}")) {
			errors.rejectValue("telefono", "format", "El telèfon ha de tenir exactament 9 dígits numèrics");
		}

		// 3. Validar Consentimiento RGPD (debe estar marcado)
		if (!usuario.isConsentimientoRgpd()) {
			errors.rejectValue("consentimientoRgpd", "obligatori", "Has d'acceptar el tractament de dades per a continuar");
		}

		// 4. Validar Nombre (sin números)
		if (usuario.getNombre().matches(".*\\d.*")) {
			errors.rejectValue("nombre", "format", "El nom no pot contenir números");
		}
		if (usuario.getNombre().trim().isEmpty()) {
			errors.rejectValue("nombre", "obligatori", "El nom és obligatori");
		}

		// 5. Validar Apellidos (sin números)
		if (usuario.getApellidos().matches(".*\\d.*")) {
			errors.rejectValue("apellidos", "format", "Els cognoms no poden contenir números");
		}
		if (usuario.getApellidos().trim().isEmpty()) {
			errors.rejectValue("apellidos", "obligatori", "Els cognoms són obligatoris");
		}

		// 6. Validar ID (obligatorio)
		if (usuario.getIdUsuario().trim().isEmpty()) {
			errors.rejectValue("idUsuario", "obligatori", "L'identificador d'usuari és obligatori");
		}
	}
}

@Controller
@RequestMapping("/UsuariOVI")
public class UsuariOVIController {

	private UsuariOVIDaoImpl usuariOVIDao;

	@Autowired
	UsuariOVIValidator usuariOVIValidator;


	@Autowired
	public void setUsuariOVIDao(UsuariOVIDaoImpl usuariOVIDao) {
		this.usuariOVIDao = usuariOVIDao;
	}

	// Listar
	@RequestMapping("/list")
	public String listUsuariOVI(Model model) {
		model.addAttribute("usuarios", usuariOVIDao.getUsuariosOVI());
		return "UsuariOVI/list";
	}

	// Añadir: Mostrar formulario
	@RequestMapping(value="/add")
	public String addUsuariOVI(Model model) {
		model.addAttribute("usuariOVI", new UsuariOVI());
		return "UsuariOVI/add";
	}


	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String processAddSubmit(@ModelAttribute("usuariOVI") UsuariOVI usuariOVI,
								   BindingResult bindingResult) {

		// ASIGNACIÓN AUTOMÁTICA DEL ID (Máximo + 1)
		int nextId = usuariOVIDao.getUsuariosOVI().stream()
				.mapToInt(u -> Integer.parseInt(u.getIdUsuario().substring(1)))
				.max().orElse(0) + 1;

		usuariOVI.setIdUsuario("U" + String.format("%03d", nextId));

		usuariOVIValidator.validate(usuariOVI, bindingResult);

		if (bindingResult.hasErrors()) {
			return "UsuariOVI/add";
		}

		usuariOVIDao.addUsuariOVI(usuariOVI);
		return "redirect:list";
	}

	// Modificar: Mostrar formulario
	@RequestMapping(value="/update/{idUsuario}", method=RequestMethod.GET)
	public String editUsuariOVI(Model model, @PathVariable String idUsuario) {
		model.addAttribute("usuariOVI", usuariOVIDao.getUsuariOVI(idUsuario));
		return "UsuariOVI/update";
	}



	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String processUpdateSubmit(@ModelAttribute("usuariOVI") UsuariOVI usuariOVI,
									  BindingResult bindingResult) {

		usuariOVIValidator.validate(usuariOVI, bindingResult);

		if (bindingResult.hasErrors()) {
			return "UsuariOVI/update";
		}

		usuariOVIDao.updateUsuariOVI(usuariOVI);
		return "redirect:list";
	}

	// Borrar
	@RequestMapping(value="/delete/{idUsuario}")
	public String processDelete(@PathVariable String idUsuario) {
		usuariOVIDao.deleteUsuariOVIPorId(idUsuario);
		return "redirect:../list";
	}

}

