package bookMyStay.services;

import bookMyStay.dtos.ViewRequest;
import bookMyStay.entities.Status;
import bookMyStay.entities.User;
import bookMyStay.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RequestService requestService;

    public void saveUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent())
            return;

        userRepository.save(new User(null, email));
    }

    @SneakyThrows
    public void saveUserJWT(@JsonProperty("access_token") String str) {
        JWT jwt = JWTParser.parse(str);
        String email = (String) jwt.getJWTClaimsSet().getClaims().get("email");
        if (email != null)
            saveUser(email);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No user with email: " + email));
    }

    public List<ViewRequest> getPendingRequestsByModerator() {
        User user = getCurrentUser();
        return requestService.getPendingRequestsByModerator(user);
    }

    public User getCurrentUser() {
        String email = getClaim("email");
        return findUserByEmail(email);
    }

    public String getClaim(String claim) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();

        return jwt.getClaim(claim);
    }

    public List<ViewRequest> getProcessedRequestByModerator(Status status) {
        User user = getCurrentUser();
        return requestService.getProcessedRequestByModerator(user, status);
    }

    public List<ViewRequest> getProcessedRequestByGuest(Status status) {
        User user = getCurrentUser();
        return requestService.getProcessedRequestByGuest(user, status);
    }

    public List<ViewRequest> getPendingRequestsByGuest() {
        User user = getCurrentUser();
        return requestService.getPendingRequestsByGuest(user);
    }
}
