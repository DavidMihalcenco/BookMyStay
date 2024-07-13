package bookMyStay.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hotels")
public class HotelControllerMVC {

    @GetMapping
    public String getHotels(Model model) {
        model.addAttribute("hotels", true);
        model.addAttribute("scriptSrc", "hotels.js");
        return "generic";
    }

    @GetMapping("/new")
    public String addHotel(Model model) {
        model.addAttribute("hotels", true);
        model.addAttribute("scriptSrc", "addHotel.js");
        return "generic";
    }

    @GetMapping("/{id}")
    public String viewHotel(Model model, @PathVariable Integer id) {
        model.addAttribute("hotels", true);
        model.addAttribute("scriptSrc", "viewHotel.js");
        return "generic";
    }
}
