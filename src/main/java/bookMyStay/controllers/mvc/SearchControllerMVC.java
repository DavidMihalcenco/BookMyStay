package bookMyStay.controllers.mvc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping({"/search", "/search/"})
public class SearchControllerMVC {

    @GetMapping
    public String getSearchPage(Model model) {
        model.addAttribute("search", true);
        model.addAttribute("scriptSrc", "search.js");

        return "generic";
    }
}
