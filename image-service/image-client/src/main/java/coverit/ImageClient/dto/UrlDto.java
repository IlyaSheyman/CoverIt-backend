package coverit.ImageClient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import static coverit.ImageClient.constants.Constants.MAX_LINK_LENGTH;
import static coverit.ImageClient.constants.Constants.MIN_LINK_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlDto {
    @NotBlank
    @URL
    @Size(min = MIN_LINK_LENGTH,
        max = MAX_LINK_LENGTH)
    private String link;
}
