package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/UsuariOVI")
public class UsuariOVIController {

	private UsuariOVIDaoImpl usuariOVIDao;

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

	// Añadir: Procesar formulario
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String processAddSubmit(@ModelAttribute("usuariOVI") UsuariOVI usuariOVI) {
		usuariOVIDao.addUsuariOVI(usuariOVI);
		return "redirect:list";
	}

	// Modificar: Mostrar formulario
	@RequestMapping(value="/update/{idUsuario}", method=RequestMethod.GET)
	public String editUsuariOVI(Model model, @PathVariable String idUsuario) {
		model.addAttribute("usuariOVI", usuariOVIDao.getUsuariOVI(idUsuario));
		return "UsuariOVI/update";
	}

	// Modificar: Procesar formulario
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String processUpdateSubmit(@ModelAttribute("usuariOVI") UsuariOVI usuariOVI) {
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

