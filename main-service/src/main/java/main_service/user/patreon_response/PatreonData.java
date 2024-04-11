package main_service.user.patreon_response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatreonData {
    private PatreonAttributes attributes;
}
