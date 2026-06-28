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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
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
        if (request.getProximidad() == null
                || request.getProximidad().trim().isEmpty()) {
            errors.rejectValue("proximidad", "obligatori",
                    "La província és obligatòria");
        }
        if (request.getPreferencias() == null
                || request.getPreferencias().trim().isEmpty()) {
            errors.rejectValue("preferencias", "obligatori",
                    "Les preferències són obligatòries");
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
    private final SelectionDaoImpl selectionDao;
    private final RegistreContracteDaoImpl registreContracteDao;

    private static final int TITLE_FONT_SIZE = 20;
    private static final int BODY_FONT_SIZE = 12;

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
                "A Coruña", "Álava", "Albacete", "Alicante", "Almería",
                "Asturias", "Ávila", "Badajoz", "Baleares", "Barcelona",
                "Burgos", "Cáceres", "Cádiz", "Cantabria", "Castellón",
                "Ciudad Real", "Córdoba", "Cuenca", "Girona", "Granada",
                "Guadalajara", "Guipúzcoa", "Huelva", "Huesca", "Jaén",
                "La Rioja", "Las Palmas", "León", "Lleida", "Lugo", "Madrid",
                "Málaga", "Murcia", "Navarra", "Ourense", "Palencia",
                "Pontevedra", "Salamanca", "Segovia", "Sevilla", "Soria",
                "Tarragona", "Santa Cruz de Tenerife", "Teruel", "Toledo",
                "Valencia", "Valladolid", "Vizcaya", "Zamora", "Zaragoza");
    }

    // =========================================================
    // ADMIN — listado general
    // =========================================================

    /**
     * Lists all requests for admin.
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listAPRequest(
            @RequestParam(defaultValue = "") String busqueda,
            @RequestParam(defaultValue = "") String estat,
            @RequestParam(defaultValue = "") String tipus,
            @RequestParam(defaultValue = "0") int pagina,
            Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/login";

        int tamanyPagina = 10;
        List<APRequest> todas = apRequestDao.getAPRequests();
        List<UsuariOVI> usuarios = usuariOVIDao.getUsuariosOVI();

        List<APRequest> filtradas = new ArrayList<>();
        for (APRequest r : todas) {
            boolean coincideixEstat = estat.isEmpty()
                    || estat.equals(r.getEstado());
            boolean coincideixTipus = tipus.isEmpty()
                    || tipus.equals(r.getTipoAsistencia());
            if (!coincideixEstat || !coincideixTipus) continue;

            if (busqueda.isEmpty()) {
                filtradas.add(r);
            } else {
                for (UsuariOVI u : usuarios) {
                    if (u.getIdUsuario().equals(r.getIdUsuarioOvi())) {
                        String nombre = (u.getNombre() + " "
                                + u.getApellidos()).toLowerCase();
                        if (nombre.contains(busqueda.toLowerCase()))
                            filtradas.add(r);
                        break;
                    }
                }
            }
        }

        int totalRegistres = filtradas.size();
        int totalPagines = (int) Math.ceil((double) totalRegistres / tamanyPagina);
        if (pagina < 0) pagina = 0;
        if (pagina >= totalPagines && totalPagines > 0) pagina = totalPagines - 1;

        int inici = pagina * tamanyPagina;
        int fi = Math.min(inici + tamanyPagina, totalRegistres);
        List<APRequest> paginades = totalRegistres > 0
                ? filtradas.subList(inici, fi) : filtradas;

        model.addAttribute("requests", paginades);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("estat", estat);
        model.addAttribute("tipus", tipus);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPagines", totalPagines);
        model.addAttribute("totalRegistres", totalRegistres);
        return "APRequest/list";
    }

    // =========================================================
    // USUARI OVI — mylist
    // =========================================================

    /**
     * Lists requests for the logged-in OVI user with three states per request.
     */
    @RequestMapping(value = "/mylist", method = RequestMethod.GET)
    public String listMyRequests(
            @RequestParam(defaultValue = "") String busqueda,
            @RequestParam(defaultValue = "") String estat,
            @RequestParam(defaultValue = "") String tipus,
            @RequestParam(defaultValue = "0") int pagina,
            Model model, HttpSession session) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) return "redirect:/login";

        int tamanyPagina = 10;
        List<APRequest> requests = apRequestDao.getAPRequestsByUsuari(
                usuari.getIdUsuario());
        List<RegistreContracte> todosContratos =
                registreContracteDao.getRegistresContractes();

        java.util.Map<Integer, String> asistenteConContracte =
                new java.util.HashMap<>();
        java.util.Set<Integer> solicitudesConCandidats =
                new java.util.HashSet<>();

        for (APRequest req : requests) {
            List<Selection> sels =
                    selectionDao.getSelectionsBySolicitud(req.getIdSolicitud());
            for (Selection s : sels) {
                RegistreContracte rc = todosContratos.stream()
                        .filter(c -> c.getIdSeleccion() == s.getIdSeleccion())
                        .findFirst().orElse(null);
                if (rc != null) {
                    asistenteConContracte.put(req.getIdSolicitud(),
                            s.getIdAsistente());
                    break;
                } else {
                    solicitudesConCandidats.add(req.getIdSolicitud());
                }
            }
        }

        List<APRequest> filtrades = new ArrayList<>();
        for (APRequest r : requests) {
            // Busqueda solo por província
            boolean coincideixBusqueda = busqueda.isEmpty()
                    || r.getProximidad().toLowerCase()
                    .contains(busqueda.toLowerCase());
            boolean coincideixEstat = estat.isEmpty()
                    || estat.equals(r.getEstado());
            boolean coincideixTipus = tipus.isEmpty()
                    || tipus.equals(r.getTipoAsistencia());
            if (coincideixBusqueda && coincideixEstat && coincideixTipus)
                filtrades.add(r);
        }

        // Més recents primer
        filtrades.sort((a, b) -> b.getIdSolicitud() - a.getIdSolicitud());

        int totalRegistres = filtrades.size();
        int totalPagines = (int) Math.ceil((double) totalRegistres / tamanyPagina);
        if (pagina < 0) pagina = 0;
        if (pagina >= totalPagines && totalPagines > 0) pagina = totalPagines - 1;

        int inici = pagina * tamanyPagina;
        int fi = Math.min(inici + tamanyPagina, totalRegistres);
        List<APRequest> paginades = totalRegistres > 0
                ? filtrades.subList(inici, fi) : filtrades;

        model.addAttribute("requests", paginades);
        model.addAttribute("usuari", usuari);
        model.addAttribute("asistenteConContracte", asistenteConContracte);
        model.addAttribute("solicitudesConCandidats", solicitudesConCandidats);
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("estat", estat);
        model.addAttribute("tipus", tipus);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPagines", totalPagines);
        model.addAttribute("totalRegistres", totalRegistres);
        return "APRequest/mylist";
    }
    // =========================================================
    // USUARI OVI — add / update / delete
    // =========================================================

    /**
     * Shows the add form for a new request.
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addAPRequest(Model model, HttpSession session) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) return "redirect:/login";

        APRequest request = new APRequest();
        request.setEstado("En revisión");
        request.setIdUsuarioOvi(usuari.getIdUsuario());
        model.addAttribute("apRequest", request);
        model.addAttribute("provincias", getListaProvincias());
        return "APRequest/add";
    }

    /**
     * Processes a new request submission.
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(
            @ModelAttribute("apRequest") APRequest apRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        int nextId = apRequestDao.getAPRequests().stream()
                .mapToInt(APRequest::getIdSolicitud).max().orElse(0) + 1;
        apRequest.setIdSolicitud(nextId);
        apRequestValidator.validate(apRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("provincias", getListaProvincias());
            return "APRequest/add";
        }
        apRequestDao.addAPRequest(apRequest);
        redirectAttributes.addFlashAttribute("msgOk",
                "La sol·licitud ha sigut creada correctament.");
        return "redirect:/APRequest/mylist";
    }

    /**
     * Shows the edit form for an existing request.
     */
    @RequestMapping(value = "/update/{idSolicitud}", method = RequestMethod.GET)
    public String editAPRequest(Model model, @PathVariable int idSolicitud) {
        model.addAttribute("apRequest", apRequestDao.getAPRequest(idSolicitud));
        model.addAttribute("provincias", getListaProvincias());
        return "APRequest/update";
    }

    /**
     * Processes an updated request submission.
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("apRequest") APRequest apRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        apRequestValidator.validate(apRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("provincias", getListaProvincias());
            return "APRequest/update";
        }
        apRequestDao.updateAPRequest(apRequest);
        redirectAttributes.addFlashAttribute("msgOk",
                "La sol·licitud ha sigut actualitzada correctament.");
        return "redirect:/APRequest/mylist";
    }

    /**
     * Deletes a request.
     */
    @RequestMapping(value = "/delete/{idSolicitud}", method = RequestMethod.GET)
    public String processDelete(@PathVariable int idSolicitud,
                                RedirectAttributes redirectAttributes) {
        apRequestDao.deleteAPRequestPorId(idSolicitud);
        redirectAttributes.addFlashAttribute("msgOk",
                "La sol·licitud ha sigut eliminada correctament.");
        return "redirect:/APRequest/mylist";
    }

    /**
     * Confirm delete page for mylist.
     */
    @RequestMapping(value = "/confirmDeleteMylist/{id}", method = RequestMethod.GET)
    public String confirmDeleteMylist(@PathVariable int id,
                                      Model model, HttpSession session) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) return "redirect:/login";
        model.addAttribute("idSolicitud", id);
        return "APRequest/confirmDeleteMylist";
    }

    // =========================================================
    // USUARI OVI — elecció de candidats (nou flux)
    // =========================================================

    /**
     * Shows all approved assistants. Already-selected ones are excluded.
     */
    @RequestMapping(value = "/mylistChooseAP/{id}", method = RequestMethod.GET)
    public String mylistChooseAP(
            @PathVariable int id,
            @RequestParam(defaultValue = "") String busqueda,
            @RequestParam(defaultValue = "0") int pagina,
            Model model, HttpSession session) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) return "redirect:/login";

        APRequest request = apRequestDao.getAPRequest(id);
        if (request == null) return "redirect:/APRequest/mylist";

        int tamanyPagina = 10;

        List<Selection> seleccionesActuales =
                selectionDao.getSelectionsBySolicitud(id);
        List<String> idsYaSeleccionados = new ArrayList<>();
        for (Selection s : seleccionesActuales) {
            idsYaSeleccionados.add(s.getIdAsistente());
        }

        List<AssistentPersonal> tots =
                assistentPersonalDao.getAssistentsPersonalsByEstado("Aceptado");
        List<AssistentPersonal> disponibles = new ArrayList<>();
        for (AssistentPersonal ap : tots) {
            if (!idsYaSeleccionados.contains(ap.getIdAsistente())) {
                String nombre = (ap.getNombre() + " "
                        + ap.getApellidos()).toLowerCase();
                boolean coincideix = busqueda.isEmpty()
                        || nombre.contains(busqueda.toLowerCase());
                if (coincideix) disponibles.add(ap);
            }
        }

        int totalRegistres = disponibles.size();
        int totalPagines = (int) Math.ceil((double) totalRegistres / tamanyPagina);
        if (pagina < 0) pagina = 0;
        if (pagina >= totalPagines && totalPagines > 0) pagina = totalPagines - 1;

        int inici = pagina * tamanyPagina;
        int fi = Math.min(inici + tamanyPagina, totalRegistres);
        List<AssistentPersonal> paginats = totalRegistres > 0
                ? disponibles.subList(inici, fi) : disponibles;

        model.addAttribute("request", request);
        model.addAttribute("candidatos", paginats);
        model.addAttribute("numSeleccionados", idsYaSeleccionados.size());
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPagines", totalPagines);
        model.addAttribute("totalRegistres", totalRegistres);
        return "APRequest/mylistChooseAP";
    }

    /**
     * Adds an assistant to the candidate list for this request (no contract yet).
     */
    @RequestMapping(value = "/seleccionarAssistent/{idSolicitud}/{idAsistente}",
            method = RequestMethod.GET)
    public String seleccionarAssistent(
            @PathVariable int idSolicitud,
            @PathVariable String idAsistente,
            HttpSession session) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) return "redirect:/login";

        // Comprobar que no esté ya seleccionado
        for (Selection s : selectionDao.getSelectionsBySolicitud(idSolicitud)) {
            if (s.getIdAsistente().equals(idAsistente)) {
                return "redirect:/APRequest/mylistChooseAP/" + idSolicitud;
            }
        }

        int nextId = selectionDao.getSelections().stream()
                .mapToInt(Selection::getIdSeleccion).max().orElse(0) + 1;
        Selection sel = new Selection();
        sel.setIdSeleccion(nextId);
        sel.setIdSolicitud(idSolicitud);
        sel.setIdAsistente(idAsistente);
        selectionDao.addSelection(sel);

        return "redirect:/APRequest/mylistChooseAP/" + idSolicitud;
    }

    /**
     * Shows the list of chosen candidates for a request.
     */
    @RequestMapping(value = "/candidatsEscollits/{id}", method = RequestMethod.GET)
    public String candidatsEscollits(@PathVariable int id, Model model,
                                     HttpSession session) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) return "redirect:/login";

        APRequest request = apRequestDao.getAPRequest(id);
        if (request == null) return "redirect:/APRequest/mylist";

        List<Selection> selecciones = selectionDao.getSelectionsBySolicitud(id);
        List<AssistentPersonal> candidats = new ArrayList<>();
        for (Selection s : selecciones) {
            if (registreContracteDao.getRegistreContracteBySeleccion(
                    s.getIdSeleccion()) == null) {
                AssistentPersonal ap =
                        assistentPersonalDao.getAssistentPersonal(s.getIdAsistente());
                if (ap != null) candidats.add(ap);
            }
        }

        model.addAttribute("request", request);
        model.addAttribute("candidats", candidats);
        return "APRequest/candidatsEscollits";
    }

    /**
     * Shows the details of a candidate (read-only).
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
            return "redirect:/APRequest/candidatsEscollits/" + idSolicitud;

        boolean teContracte = false;
        for (Selection s : selectionDao.getSelectionsBySolicitud(idSolicitud)) {
            if (registreContracteDao.getRegistreContracteBySeleccion(
                    s.getIdSeleccion()) != null) {
                teContracte = true;
                break;
            }
        }

        model.addAttribute("assistent", assistent);
        model.addAttribute("idSolicitud", idSolicitud);
        model.addAttribute("teContracte", teContracte);
        return "APRequest/mylistChooseAPDetails";
    }

    /**
     * Shows the contract creation form with date fields.
     */
    @RequestMapping(value = "/crearContracte/{idSolicitud}/{idAsistente}",
            method = RequestMethod.GET)
    public String mostrarCrearContracte(
            @PathVariable int idSolicitud,
            @PathVariable String idAsistente,
            Model model, HttpSession session) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) return "redirect:/login";

        AssistentPersonal assistent =
                assistentPersonalDao.getAssistentPersonal(idAsistente);
        model.addAttribute("assistent", assistent);
        model.addAttribute("idSolicitud", idSolicitud);
        model.addAttribute("idAsistente", idAsistente);
        return "APRequest/crearContracte";
    }

    /**
     * Processes contract creation with dates chosen by the user.
     */
    @RequestMapping(value = "/crearContracte", method = RequestMethod.POST)
    public String processarCrearContracte(
            @RequestParam int idSolicitud,
            @RequestParam String idAsistente,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) return "redirect:/login";

        java.time.LocalDate inici = java.time.LocalDate.parse(fechaInicio);
        java.time.LocalDate fi = java.time.LocalDate.parse(fechaFin);
        java.time.LocalDate avui = java.time.LocalDate.now();

        if (inici.isBefore(avui)) {
            redirectAttributes.addFlashAttribute("errorDates",
                    "La data d'inici no pot ser anterior a avui.");
            return "redirect:/APRequest/crearContracte/" + idSolicitud
                    + "/" + idAsistente;
        }
        if (!fi.isAfter(inici)) {
            redirectAttributes.addFlashAttribute("errorDates",
                    "La data de fi ha de ser posterior a la data d'inici.");
            return "redirect:/APRequest/crearContracte/" + idSolicitud
                    + "/" + idAsistente;
        }

        Selection seleccionElegida = null;
        for (Selection s : selectionDao.getSelectionsBySolicitud(idSolicitud)) {
            if (s.getIdAsistente().equals(idAsistente)) {
                seleccionElegida = s;
                break;
            }
        }

        if (seleccionElegida == null) {
            int nextIdSel = selectionDao.getSelections().stream()
                    .mapToInt(Selection::getIdSeleccion).max().orElse(0) + 1;
            seleccionElegida = new Selection();
            seleccionElegida.setIdSeleccion(nextIdSel);
            seleccionElegida.setIdSolicitud(idSolicitud);
            seleccionElegida.setIdAsistente(idAsistente);
            selectionDao.addSelection(seleccionElegida);
        }

        int nextIdContrato = registreContracteDao.getRegistresContractes().stream()
                .mapToInt(RegistreContracte::getIdContrato).max().orElse(0) + 1;

        RegistreContracte contracte = new RegistreContracte();
        contracte.setIdContrato(nextIdContrato);
        contracte.setIdSeleccion(seleccionElegida.getIdSeleccion());
        contracte.setFechaInicio(inici);
        contracte.setFechaFin(fi);
        registreContracteDao.addRegistreContracte(contracte);

        // Cerrar la solicitud — las demás selecciones se quedan,
        // los otros asistentes las verán como "tancat"
        apRequestDao.updateEstado(idSolicitud, "Cerrada con contrato");

        redirectAttributes.addFlashAttribute("msgOk",
                "Contracte creat correctament.");
        return "redirect:/APRequest/mylist";
    }

    // =========================================================
    // ADMIN — aprobar / addAdmin / updateAdmin / confirmDelete
    // =========================================================

    /**
     * Shows the approval form for a request.
     */
    @RequestMapping(value = "/aprobar/{id}", method = RequestMethod.GET)
    public String mostrarAprobar(Model model, @PathVariable int id) {
        APRequest solicitud = apRequestDao.getAPRequest(id);
        model.addAttribute("solicitud", solicitud);
        model.addAttribute("usuari",
                usuariOVIDao.getUsuariOVI(solicitud.getIdUsuarioOvi()));
        return "APRequest/aprobar";
    }

    /**
     * Processes request approval.
     */
    @RequestMapping(value = "/aprobar", method = RequestMethod.POST)
    public String processAprobar(@RequestParam int idSolicitud,
                                 @RequestParam String estado,
                                 RedirectAttributes redirectAttributes) {
        apRequestDao.updateEstado(idSolicitud, estado);
        redirectAttributes.addFlashAttribute("msgOk",
                "L'estat de la sol·licitud ha sigut actualitzat.");
        return "redirect:/APRequest/list";
    }

    /**
     * Confirm delete page for admin list.
     */
    @RequestMapping(value = "/confirmDeleteAdmin/{id}", method = RequestMethod.GET)
    public String confirmDeleteAdmin(@PathVariable int id,
                                     Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/login";
        model.addAttribute("idSolicitud", id);
        return "APRequest/confirmDeleteAdmin";
    }

    /**
     * Shows the admin add form.
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
     * Processes admin add form.
     */
    @RequestMapping(value = "/addAdmin", method = RequestMethod.POST)
    public String processAddAdmin(
            @ModelAttribute("apRequest") APRequest apRequest,
            BindingResult br, Model model) {
        int nextId = apRequestDao.getAPRequests().stream()
                .mapToInt(APRequest::getIdSolicitud).max().orElse(0) + 1;
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
     * Shows the admin edit form.
     */
    @RequestMapping(value = "/updateAdmin/{idSolicitud}", method = RequestMethod.GET)
    public String editAPRequestAdmin(Model model, @PathVariable int idSolicitud) {
        model.addAttribute("apRequest", apRequestDao.getAPRequest(idSolicitud));
        model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
        model.addAttribute("provincias", getListaProvincias());
        return "APRequest/updateAdmin";
    }

    /**
     * Processes admin edit form.
     */
    @RequestMapping(value = "/updateAdmin", method = RequestMethod.POST)
    public String processUpdateAdmin(
            @ModelAttribute("apRequest") APRequest apRequest,
            BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("usuariosOvi", usuariOVIDao.getUsuariosOVI());
            return "APRequest/updateAdmin";
        }
        apRequestDao.updateAPRequest(apRequest);
        return "redirect:/APRequest/list";
    }

    // =========================================================
    // PDF — contrato usuariOVI
    // =========================================================

    /**
     * Generates the PDF contract for the logged-in OVI user.
     */
    @RequestMapping(value = "/contrato/{idSolicitud}", method = RequestMethod.GET)
    public void verContrato(@PathVariable int idSolicitud,
                            HttpSession session,
                            jakarta.servlet.http.HttpServletResponse response) throws Exception {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) { response.sendRedirect("/login"); return; }

        List<Selection> selecciones =
                selectionDao.getSelectionsBySolicitud(idSolicitud);
        Selection seleccion = null;
        for (Selection s : selecciones) {
            if (registreContracteDao.getRegistreContracteBySeleccion(
                    s.getIdSeleccion()) != null) {
                seleccion = s; break;
            }
        }
        if (seleccion == null) { response.sendRedirect("/APRequest/mylist"); return; }

        RegistreContracte contracte = registreContracteDao
                .getRegistreContracteBySeleccion(seleccion.getIdSeleccion());
        APRequest solicitud = apRequestDao.getAPRequest(idSolicitud);
        AssistentPersonal assistent = assistentPersonalDao
                .getAssistentPersonal(seleccion.getIdAsistente());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "inline; filename=\"contrato_" + idSolicitud + ".pdf\"");

        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        com.itextpdf.text.pdf.PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();
        generarContenidoPdf(doc, contracte, usuari.getNombre(),
                usuari.getApellidos(), usuari.getEmail(), usuari.getTelefono(),
                assistent.getNombre(), assistent.getApellidos(),
                assistent.getEmail(), assistent.getTelefono(),
                solicitud.getTipoAsistencia(), solicitud.getProximidad(),
                solicitud.getPreferencias());
        doc.close();
    }

    // =========================================================
    // PDF — contrato admin
    // =========================================================

    /**
     * Generates the PDF contract for admin view.
     */
    @RequestMapping(value = "/contratoPdf/{idContrato}", method = RequestMethod.GET)
    public void contratoPdfAdmin(@PathVariable int idContrato,
                                 HttpSession session,
                                 jakarta.servlet.http.HttpServletResponse response) throws Exception {
        if (session.getAttribute("admin") == null) {
            response.sendRedirect("/login"); return;
        }

        RegistreContracte contracte =
                registreContracteDao.getRegistreContracte(idContrato);
        if (contracte == null) {
            response.sendRedirect("/APRequest/contratos"); return;
        }

        Selection seleccion = null;
        for (Selection s : selectionDao.getSelections()) {
            if (s.getIdSeleccion() == contracte.getIdSeleccion()) {
                seleccion = s; break;
            }
        }

        APRequest solicitud = seleccion != null
                ? apRequestDao.getAPRequest(seleccion.getIdSolicitud()) : null;
        AssistentPersonal assistent = seleccion != null
                ? assistentPersonalDao.getAssistentPersonal(
                seleccion.getIdAsistente()) : null;
        UsuariOVI usuari = solicitud != null
                ? usuariOVIDao.getUsuariOVI(solicitud.getIdUsuarioOvi()) : null;

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "inline; filename=\"contrato_" + idContrato + ".pdf\"");

        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        com.itextpdf.text.pdf.PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();
        generarContenidoPdf(doc, contracte,
                usuari != null ? usuari.getNombre() : "—",
                usuari != null ? usuari.getApellidos() : "",
                usuari != null ? usuari.getEmail() : "—",
                usuari != null ? usuari.getTelefono() : "—",
                assistent != null ? assistent.getNombre() : "—",
                assistent != null ? assistent.getApellidos() : "",
                assistent != null ? assistent.getEmail() : "—",
                assistent != null ? assistent.getTelefono() : "—",
                solicitud != null ? solicitud.getTipoAsistencia() : "—",
                solicitud != null ? solicitud.getProximidad() : "—",
                solicitud != null ? solicitud.getPreferencias() : null);
        doc.close();
    }

    // =========================================================
    // ADMIN — contratos list
    // =========================================================

    /**
     * Lists all contracts for admin.
     */
    @RequestMapping(value = "/contratos", method = RequestMethod.GET)
    public String listarContratos(
            @RequestParam(defaultValue = "") String busqueda,
            @RequestParam(defaultValue = "0") int pagina,
            Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/login";

        int tamanyPagina = 10;
        List<RegistreContracte> contratos =
                registreContracteDao.getRegistresContractes();
        List<java.util.Map<String, Object>> todos = new ArrayList<>();

        for (RegistreContracte c : contratos) {
            Selection sel = null;
            for (Selection s : selectionDao.getSelections()) {
                if (s.getIdSeleccion() == c.getIdSeleccion()) { sel = s; break; }
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
            todos.add(row);
        }

        List<java.util.Map<String, Object>> filtrats = new ArrayList<>();
        for (java.util.Map<String, Object> row : todos) {
            if (busqueda.isEmpty()) {
                filtrats.add(row);
            } else {
                UsuariOVI u = (UsuariOVI) row.get("usuari");
                AssistentPersonal a = (AssistentPersonal) row.get("assistent");
                String noms = "";
                if (u != null) noms += u.getNombre() + " " + u.getApellidos();
                if (a != null) noms += " " + a.getNombre() + " " + a.getApellidos();
                if (noms.toLowerCase().contains(busqueda.toLowerCase()))
                    filtrats.add(row);
            }
        }

        int totalRegistres = filtrats.size();
        int totalPagines = (int) Math.ceil((double) totalRegistres / tamanyPagina);
        if (pagina < 0) pagina = 0;
        if (pagina >= totalPagines && totalPagines > 0) pagina = totalPagines - 1;

        int inici = pagina * tamanyPagina;
        int fi = Math.min(inici + tamanyPagina, totalRegistres);
        List<java.util.Map<String, Object>> paginats = totalRegistres > 0
                ? filtrats.subList(inici, fi) : filtrats;

        model.addAttribute("contratos", paginats);
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPagines", totalPagines);
        model.addAttribute("totalRegistres", totalRegistres);
        return "APRequest/contratos";
    }

    // =========================================================
    // HELPER privat — generació de contingut PDF
    // =========================================================

    /**
     * Shared PDF content generation used by both OVI user and admin endpoints.
     */
    private void generarContenidoPdf(
            com.itextpdf.text.Document doc,
            RegistreContracte contracte,
            String oviNom, String oviCognoms,
            String oviEmail, String oviTelefon,
            String apNom, String apCognoms,
            String apEmail, String apTelefon,
            String tipus, String provincia,
            String preferencies) throws Exception {

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
                "Nom: " + oviNom + " " + oviCognoms, normalFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Email: " + oviEmail, normalFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Telèfon: " + oviTelefon, normalFont));
        doc.add(new com.itextpdf.text.Paragraph("\n"));

        doc.add(new com.itextpdf.text.Paragraph("ASSISTENT PERSONAL", boldFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Nom: " + apNom + " " + apCognoms, normalFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Email: " + apEmail, normalFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Telèfon: " + apTelefon, normalFont));
        doc.add(new com.itextpdf.text.Paragraph("\n"));

        doc.add(new com.itextpdf.text.Paragraph("SERVEI", boldFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Tipus: " + tipus, normalFont));
        doc.add(new com.itextpdf.text.Paragraph(
                "Província: " + provincia, normalFont));
        if (preferencies != null && !preferencies.isEmpty()) {
            doc.add(new com.itextpdf.text.Paragraph(
                    "Preferències: " + preferencies, normalFont));
        }
    }
}