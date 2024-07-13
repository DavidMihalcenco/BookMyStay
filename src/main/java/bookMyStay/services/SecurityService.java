package bookMyStay.services;

import bookMyStay.dtos.AccessTokenDto;
import bookMyStay.dtos.GetUserDto;
import bookMyStay.dtos.LogoutDto;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;

@Service
public class SecurityService {

    private final String REDIRECT_URI = System.getenv("redirect_uri");
    private final String AUTH_URL = "https://ip-keycloack.azurewebsites.net/auth/realms/BookMyStay/protocol/openid-connect/token";
    private final String LOGOUT_URL = "https://ip-keycloack.azurewebsites.net/auth/realms/BookMyStay/protocol/openid-connect/logout";

    private final UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public AccessTokenDto authUser(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "book-my-stay");
        map.add("client_secret", System.getenv("client_secret"));
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", REDIRECT_URI + "auth");
        map.add("scope", "openid");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<AccessTokenDto> response = restTemplate
                .exchange(AUTH_URL,
                        HttpMethod.POST,
                        entity,
                        AccessTokenDto.class);

        userService.saveUserJWT(response.getBody().accessToken());

        return response.getBody();
    }

    public GetUserDto getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isModerator = authentication.getAuthorities()
                .stream()
                .filter(auth -> auth.getAuthority().equalsIgnoreCase("role_moderator"))
                .count() != 0;
        return new GetUserDto(isModerator, authentication.getName());
    }

    public void userLogout(LogoutDto token) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Bearer " + token.accessToken());

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "book-my-stay");
        map.add("client_secret", System.getenv("client_secret"));
        map.add("refresh_token", token.refreshToken());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        restTemplate.exchange(LOGOUT_URL,
                        HttpMethod.POST,
                        entity,
                        AccessTokenDto.class);
    }

    public String getClaim(String claim) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();

        return jwt.getClaim(claim);
    }
}
