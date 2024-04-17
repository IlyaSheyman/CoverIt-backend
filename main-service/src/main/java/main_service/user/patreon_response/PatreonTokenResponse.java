package main_service.user.patreon_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatreonTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private long expires_in;
    @JsonProperty("token_type")
    private String tokenType;
    private String scope;
    @JsonProperty("refresh_token")
    private String refreshToken;
    private String version;
}