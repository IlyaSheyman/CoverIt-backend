package main_service.user.patreon_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatreonAttributes {
    private String email;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("patron_status")
    private String patronStatus;
}