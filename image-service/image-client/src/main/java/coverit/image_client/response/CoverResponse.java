package coverit.image_client.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoverResponse {
    private String url;
    private String prompt;
}
