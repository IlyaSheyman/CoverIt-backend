package main_service.user.client;

import main_service.user.dto.PatronDto;
import main_service.user.dto.PatronSmallDto;
import main_service.user.patreon_response.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;


@Component
public class PatreonClient {
    private final String campaignId;
    private final String accessToken;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final RestTemplate restTemplate;

    public PatreonClient(@Value(value = "${patreon.access.token}") String accessToken,
                         @Value(value = "${patreon.campaign.id}") String campaignId,
                         @Value(value = "${patreon.client.id}") String clientId,
                         @Value(value = "${patreon.client.secret}") String clientSecret,
                         @Value(value = "${patreon.client.redirect.uri}") String redirectUri) {
        this.campaignId = campaignId;
        this.accessToken = accessToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.restTemplate = new RestTemplate();
    }


    public String getAccessToken(String code) {
        String tokenUrl = "https://www.patreon.com/api/oauth2/token";;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("code", code)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("redirect_uri", redirectUri);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<PatreonTokenResponse> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                PatreonTokenResponse.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PatreonTokenResponse tokenResponse = responseEntity.getBody();
            return tokenResponse.getAccessToken();
        } else {
            throw new RuntimeException("Failed to obtain access token");
        }
    }

    public PatronDto getPatron(String token) {
        String apiUrl = "https://www.patreon.com/api/oauth2/v2/identity?fields[user]=email,full_name&include=memberships&fields[member]=full_name,is_follower,last_charge_date,last_charge_status,lifetime_support_cents,currently_entitled_amount_cents,patron_status";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<PatreonResponse> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, PatreonResponse.class);

        PatreonResponse response = responseEntity.getBody();
        PatreonData data = response.getData();
        PatreonAttributes attributes = data.getAttributes();

        String email = attributes.getEmail();
        String fullName = attributes.getFullName();

        return new PatronDto(email, fullName);
    }

    public List<String> getPatronsNames() {
        String apiUrl = "https://www.patreon.com/api/oauth2/v2/campaigns/" + campaignId + "/members?fields[member]=full_name,is_follower,last_charge_date,last_charge_status,lifetime_support_cents,currently_entitled_amount_cents,patron_status";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<PatreonMembersResponse> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, PatreonMembersResponse.class);
        PatreonMembersResponse response = responseEntity.getBody();

        List<PatronSmallDto> patrons = new ArrayList<>();
        if (response != null && response.getData() != null) {
            for (PatreonMembersResponse.Data data : response.getData()) {
                String fullName = data.getAttributes().getFullName();
                String status = data.getAttributes().getPatronStatus();
                patrons.add(new PatronSmallDto(fullName, status));
            }
        }

        return patrons.stream()
                .filter(patronSmallDto -> patronSmallDto.getStatus().equals("active_patron"))
                .map(PatronSmallDto::getFullName)
                .toList();
    }
}