package main_service.user.patreon_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatreonMembersResponse {
    public List<Data> data;
    public Meta meta;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        public Attributes attributes;
        public String id;
        public String type;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        public Pagination pagination;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pagination {
        public Cursors cursors;
        public int total;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cursors {
        public String next;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attributes {
        @JsonProperty("currently_entitled_amount_cents")
        public int currentlyEntitledAmountCents;
        @JsonProperty("full_name")
        public String fullName;
        @JsonProperty("is_follower")
        public boolean isFollower;
        @JsonProperty("last_charge_date")
        public String lastChargeDate;
        @JsonProperty("last_charge_status")
        public String lastChargeStatus;
        @JsonProperty("lifetime_support_cents")
        public int lifetimeSupportCents;
        @JsonProperty("patron_status")
        public String patronStatus;
    }
}
