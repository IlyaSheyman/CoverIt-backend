package main_service.user.client;

import com.patreon.PatreonAPI;
import com.patreon.resources.Pledge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class PatreonClient {
    private final String campaignId;
    private PatreonAPI patreonAPI;

    public PatreonClient(@Value(value = "${patreon.access.token}") String accessToken,
                         @Value(value = "${patreon.campaign.id}") String campaignId) {
        this.campaignId = campaignId;
        this.patreonAPI = new PatreonAPI(accessToken);
    }

    public List<String> getPatreonEmails() {
        try {
            List<Pledge> pledges = patreonAPI.fetchAllPledges(campaignId);
            return pledges.parallelStream().filter(n -> n.getDeclinedSince() == null) // only paying Patreons
                    .map(n -> n.getPatron().getEmail()).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}