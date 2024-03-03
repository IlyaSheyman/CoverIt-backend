package main_service.card.cover.server.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import static main_service.constants.Constants.MAX_LINK_LENGTH;
import static main_service.constants.Constants.MIN_LINK_LENGTH;
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
