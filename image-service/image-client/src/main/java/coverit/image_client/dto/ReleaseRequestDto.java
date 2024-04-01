package coverit.image_client.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReleaseRequestDto {
    private List<String> mood;
    private String object;
    private String surrounding;
    private Boolean isLoFi;
    private List<String> coverDescription;
}
