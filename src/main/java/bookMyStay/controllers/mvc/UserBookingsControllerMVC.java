package bookMyStay.controllers.mvc;

import bookMyStay.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/myBookings")
public class UserBookingsControllerMVC {

    @GetMapping
    public String getUserBookings(Model model) {
        model.addAttribute("myBookings", true);
        model.addAttribute("scriptSrc", "userBookings.js");
        return "generic";
    }
}
