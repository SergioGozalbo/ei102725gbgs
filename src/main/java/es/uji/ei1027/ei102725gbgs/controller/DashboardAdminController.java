package es.uji.ei1027.ei102725gbgs.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardAdminController {

    /**
     * Shows the admin dashboard.
     * @param session session data
     * @param model model for the view
     * @return the dashboard view or a redirect to login
     */
    @GetMapping("/admin")
    public String dashboardAdmin(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        return "dashboard/admin";
    }
}
