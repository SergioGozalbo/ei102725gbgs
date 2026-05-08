package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardOVIController {

    /**
     * Shows the OVI user dashboard.
     * @param session session data
     * @param model model for the view
     * @return the dashboard view or a redirect to login
     */
    @GetMapping("/usuariOVI")
    public String dashboardOVI(HttpSession session, Model model) {
        UsuariOVI usuari = (UsuariOVI) session.getAttribute("usuariOVI");
        if (usuari == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuari", usuari);
        return "dashboard/usuariOVI";
    }
}
