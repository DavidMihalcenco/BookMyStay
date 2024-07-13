package bookMyStay.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/requests")
public class RequestsControllerMVC {

    @GetMapping
    public String getRequests(Model model) {
        model.addAttribute("requests", true);
        model.addAttribute("scriptSrc", "requests.js");
        return "generic";
    }
}
