package es.uji.ei1027.ei102725gbgs.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Shows the home page.
     * @param session session data
     * @return the home view or a redirect to the dashboard
     */
    @GetMapping("/")
    public String home(HttpSession session) {
        if (session.getAttribute("usuariOVI") != null) {
            return "redirect:/dashboard/usuariOVI";
        }
        if (session.getAttribute("assistentPersonal") != null) {
            return "redirect:/dashboard/assistentPersonal";
        }
        if (Boolean.TRUE.equals(session.getAttribute("admin"))) {
            return "redirect:/dashboard/admin";
        }

        return "home";
    }
}
