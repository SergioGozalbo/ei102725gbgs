package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.APRequestDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.MensajeDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.SelectionDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.APRequest;
import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
import es.uji.ei1027.ei102725gbgs.model.Mensaje;
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final MensajeDaoImpl mensajeDao;
    private final APRequestDaoImpl apRequestDao;
    private final SelectionDaoImpl selectionDao;

    @Autowired
    public ChatController(MensajeDaoImpl mensajeDao,
                          APRequestDaoImpl apRequestDao,
                          SelectionDaoImpl selectionDao) {
        this.mensajeDao = mensajeDao;
        this.apRequestDao = apRequestDao;
        this.selectionDao = selectionDao;
    }

    /**
     * Shows the chat between a specific OVI user and a specific assistant.
     * OVI messages are stored with remitenteId = "idOVI:idAsistente"
     * so each pair has a private conversation.
     */
    @RequestMapping(value = "/{idSolicitud}", method = RequestMethod.GET)
    public String showChat(@PathVariable int idSolicitud,
                           @RequestParam(required = false) String idAsistente,
                           Model model, HttpSession session) {

        String remitenteType = getRemitenteType(session);
        if (remitenteType == null) return "redirect:/login";

        APRequest solicitud = apRequestDao.getAPRequest(idSolicitud);
        if (solicitud == null) return "redirect:/login";

        String idAsistenteConversacion;
        if ("ASISTENT".equals(remitenteType)) {
            AssistentPersonal ap =
                    (AssistentPersonal) session.getAttribute("assistentPersonal");
            idAsistenteConversacion = ap.getIdAsistente();
        } else {
            if (idAsistente == null || idAsistente.trim().isEmpty()) {
                return "redirect:/APRequest/mylist";
            }
            idAsistenteConversacion = idAsistente;
        }

        String idOVI = solicitud.getIdUsuarioOvi();
        String claveOVI = idOVI + ":" + idAsistenteConversacion;

        List<Mensaje> todos = mensajeDao.getMensajesBySolicitud(idSolicitud);
        List<Mensaje> mensajes = new ArrayList<>();
        for (Mensaje m : todos) {
            boolean esOVI = "OVI".equals(m.getRemitenteType())
                    && claveOVI.equals(m.getRemitenteId());
            boolean esAsistent = "ASISTENT".equals(m.getRemitenteType())
                    && idAsistenteConversacion.equals(m.getRemitenteId());
            if (esOVI || esAsistent) {
                mensajes.add(m);
            }
        }

        String miRemitenteId;
        if ("ASISTENT".equals(remitenteType)) {
            miRemitenteId = idAsistenteConversacion;
        } else {
            miRemitenteId = claveOVI;
        }

        model.addAttribute("mensajes", mensajes);
        model.addAttribute("idSolicitud", idSolicitud);
        model.addAttribute("idAsistente", idAsistenteConversacion);
        model.addAttribute("remitenteType", remitenteType);
        model.addAttribute("miRemitenteId", miRemitenteId);
        model.addAttribute("solicitud", solicitud);
        return "chat/chat";
    }

    /**
     * Sends a message. OVI messages use "idOVI:idAsistente" as remitenteId
     * to keep conversations private per pair.
     */
    @RequestMapping(value = "/{idSolicitud}/send", method = RequestMethod.POST)
    public String sendMessage(@PathVariable int idSolicitud,
                              @RequestParam String contenido,
                              @RequestParam(required = false) String idAsistente,
                              HttpSession session) {

        String remitenteType = getRemitenteType(session);
        if (remitenteType == null) return "redirect:/login";

        if (contenido != null && !contenido.trim().isEmpty()) {
            String remitenteId;
            if ("OVI".equals(remitenteType)) {
                // Clave compuesta para mensajes del OVI
                UsuariOVI usuari =
                        (UsuariOVI) session.getAttribute("usuariOVI");
                remitenteId = usuari.getIdUsuario() + ":" + idAsistente;
            } else {
                AssistentPersonal ap =
                        (AssistentPersonal) session.getAttribute("assistentPersonal");
                remitenteId = ap.getIdAsistente();
            }

            Mensaje m = new Mensaje();
            m.setIdSolicitud(idSolicitud);
            m.setRemitenteType(remitenteType);
            m.setRemitenteId(remitenteId);
            m.setContenido(contenido.trim());
            mensajeDao.addMensaje(m);
        }

        if ("OVI".equals(remitenteType) && idAsistente != null) {
            return "redirect:/chat/" + idSolicitud
                    + "?idAsistente=" + idAsistente;
        }
        return "redirect:/chat/" + idSolicitud;
    }

    private String getRemitenteType(HttpSession session) {
        if (session.getAttribute("usuariOVI") != null) return "OVI";
        if (session.getAttribute("assistentPersonal") != null) return "ASISTENT";
        return null;
    }
}