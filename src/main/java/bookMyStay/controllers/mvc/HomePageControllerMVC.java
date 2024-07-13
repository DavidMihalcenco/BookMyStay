package bookMyStay.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageControllerMVC {

    @GetMapping({"/", ""})
    public String getStartPage(Model model) {
        model.addAttribute("home", true);
        model.addAttribute("scriptSrc", "home.js");
        return "generic";
    }
}
