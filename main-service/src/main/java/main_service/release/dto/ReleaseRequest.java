package main_service.release.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import static main_service.constants.Constants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReleaseRequest {
    @Size(min = MIN_RELEASE_TITLE_LENGTH,
            max = MAX_RELEASE_TITLE_LENGTH)
    private String title;

    @Size(min = MIN_MOOD_SIZE,
            max = MAX_MOOD_SIZE)
    private List<String> mood;

    @Size(min = MIN_OBJECT_SIZE,
            max = MAX_OBJECT_SIZE)
    private String object;

    @Size(min = MIN_SURROUNDING_SIZE,
            max = MAX_SURROUNDING_SIZE)
    private String surrounding;

    private boolean isLoFi;

    @Size(min = MIN_COVER_DESCRIPTION_SIZE,
            max = MAX_COVER_DESCRIPTION_SIZE)
    private List<String> coverDescription;
}