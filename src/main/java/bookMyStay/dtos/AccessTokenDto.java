package bookMyStay.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenDto(
        @JsonProperty("access_token")
        String accessToken,
          @JsonProperty("expires_in")
          Integer expiresIn,
          @JsonProperty("refresh_expires_in")
          Integer refreshExpiresIn,
          @JsonProperty("refresh_token")
          String refreshToken,
          String tokenType,
          String idToken,
          Integer notBeforePolicy,
          String sessionState,
          String scope) {
}
