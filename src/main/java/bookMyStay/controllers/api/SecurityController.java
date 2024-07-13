package bookMyStay.controllers.api;

import bookMyStay.dtos.AccessTokenDto;
import bookMyStay.dtos.GetUserDto;
import bookMyStay.dtos.LogoutDto;
import bookMyStay.services.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SecurityController {


    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/user")
    public GetUserDto getUserInfo() {
        return securityService.getUserInfo();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> userLogout(@RequestBody LogoutDto token) {
        securityService.userLogout(token);
        return ResponseEntity.ok().body("success");
    }
}
