package bookMyStay.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LogoutDto(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken) {
}
