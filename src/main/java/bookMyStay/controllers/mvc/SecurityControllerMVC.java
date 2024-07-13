package bookMyStay.controllers.mvc;

import bookMyStay.dtos.AccessTokenDto;
import bookMyStay.services.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecurityControllerMVC {

    private final String REDIRECT_URI = System.getenv("redirect_uri");
    private final String AUTH = "https://ip-keycloack.azurewebsites.net/auth/realms/BookMyStay/protocol/openid-connect/auth?client_id=book-my-stay&response_type=code&scope=openid&redirect_uri=" + REDIRECT_URI + "auth&state=qwkejhkasldhqjkw";
    private final SecurityService securityService;

    public SecurityControllerMVC(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "redirect:" + AUTH;
    }

    @RequestMapping({"/auth"})
    public String getToken(@RequestParam String code,
                           Model model) {

        AccessTokenDto res = securityService.authUser(code);
        model.addAttribute("token", res.accessToken());
        model.addAttribute("refresh", res.refreshToken());
        model.addAttribute("scriptSrc", "auth.js");
        return "generic";
    }
}
