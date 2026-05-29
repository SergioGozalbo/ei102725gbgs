package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.dao.APRequestDaoImpl;
import es.uji.ei1027.ei102725gbgs.dao.MensajeDaoImpl;
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

import java.util.List;

/** Handles the chat between an OVI user and their assigned assistant. */
@Controller
@RequestMapping("/chat")
public class ChatController {

    private final MensajeDaoImpl mensajeDao;
    private final APRequestDaoImpl apRequestDao;

    @Autowired
    public ChatController(MensajeDaoImpl mensajeDao,
                          APRequestDaoImpl apRequestDao) {
        this.mensajeDao = mensajeDao;
        this.apRequestDao = apRequestDao;
    }

    /**
     * Shows the chat for a given request.
     * Works for both OVI users and assistants.
     * @param idSolicitud request identifier
     * @param model model for the view
     * @param session session data
     * @return chat view or redirect to login
     */
    @RequestMapping(value = "/{idSolicitud}", method = RequestMethod.GET)
    public String showChat(@PathVariable int idSolicitud,
                           Model model, HttpSession session) {

        String remitenteType = getRemitenteType(session);
        if (remitenteType == null) return "redirect:/login";

        APRequest solicitud = apRequestDao.getAPRequest(idSolicitud);
        if (solicitud == null) return "redirect:/login";

        List<Mensaje> mensajes = mensajeDao.getMensajesBySolicitud(idSolicitud);

        model.addAttribute("mensajes", mensajes);
        model.addAttribute("idSolicitud", idSolicitud);
        model.addAttribute("remitenteType", remitenteType);
        model.addAttribute("solicitud", solicitud);
        return "chat/chat";
    }

    /**
     * Sends a message and redirects back to the chat.
     * @param idSolicitud request identifier
     * @param contenido message text
     * @param session session data
     * @return redirect to chat
     */
    @RequestMapping(value = "/{idSolicitud}/send", method = RequestMethod.POST)
    public String sendMessage(@PathVariable int idSolicitud,
                              @RequestParam String contenido,
                              HttpSession session) {

        String remitenteType = getRemitenteType(session);
        if (remitenteType == null) return "redirect:/login";

        String remitenteId = getRemitenteId(session, remitenteType);

        if (contenido != null && !contenido.trim().isEmpty()) {
            Mensaje m = new Mensaje();
            m.setIdSolicitud(idSolicitud);
            m.setRemitenteType(remitenteType);
            m.setRemitenteId(remitenteId);
            m.setContenido(contenido.trim());
            mensajeDao.addMensaje(m);
        }

        return "redirect:/chat/" + idSolicitud;
    }

    /** Returns 'OVI', 'ASISTENT', or null if nobody is logged in. */
    private String getRemitenteType(HttpSession session) {
        if (session.getAttribute("usuariOVI") != null) return "OVI";
        if (session.getAttribute("assistentPersonal") != null) return "ASISTENT";
        return null;
    }

    /** Returns the ID of whoever is logged in. */
    private String getRemitenteId(HttpSession session, String type) {
        if ("OVI".equals(type)) {
            return ((UsuariOVI) session.getAttribute("usuariOVI")).getIdUsuario();
        }
        return ((AssistentPersonal) session.getAttribute(
                "assistentPersonal")).getIdAsistente();
    }
}