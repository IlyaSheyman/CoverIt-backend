package main_service.release.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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

    @Schema(description = "Title of track/album",
            example = "Slippery rock")
    @Size(min = MIN_RELEASE_TITLE_LENGTH,
            max = MAX_RELEASE_TITLE_LENGTH)
    private String title;

    @Schema(description = "List of adjectives / short phrases",
            example = "Foggy, Slow, Nostalgic")
    @Size(min = MIN_MOOD_SIZE,
            max = MAX_MOOD_SIZE)
    private List<String> mood;

    @Schema(description = "Object / action that we see on the cover",
            example = "wet stone covered with moss, glows a little")
    @Size(min = MIN_OBJECT_SIZE,
            max = MAX_OBJECT_SIZE)
    private String object;

    @Schema(description = "Description of surrounding of an object or action",
            example = "forest edge, the sun breaks through the bark of the trees")
    @Size(min = MIN_SURROUNDING_SIZE,
            max = MAX_SURROUNDING_SIZE)
    private String surrounding;

    @NotNull
    @Schema(description = "Model of visual AI: dalle-2 or dalle-3")
    private Boolean isLoFi;

    @Size(min = MIN_COVER_DESCRIPTION_SIZE,
            max = MAX_COVER_DESCRIPTION_SIZE)
    @Schema(description = "List of adjectives / short phrases that describe cover's style",
            example = "Blurred, 3D")
    private List<String> coverDescription;
}