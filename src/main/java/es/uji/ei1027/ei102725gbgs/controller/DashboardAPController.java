package es.uji.ei1027.ei102725gbgs.controller;

import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardAPController {

    /**
     * Shows the assistant dashboard.
     * @param session session data
     * @param model model for the view
     * @return the dashboard view or a redirect to login
     */
    @GetMapping("/assistentPersonal")
    public String dashboardAP(HttpSession session, Model model) {
        AssistentPersonal assistent =
                (AssistentPersonal) session.getAttribute("assistentPersonal");
        if (assistent == null) {
            return "redirect:/login";
        }

        model.addAttribute("assistent", assistent);
        return "dashboard/assistentPersonal";
    }
}
