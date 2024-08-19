package main_service.playlist.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import main_service.cover.service.UrlDto;

import java.util.List;

import static main_service.constants.Constants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistRequest {

    @Schema(description = "List of adjectives / short phrases",
            example = "Foggy, Slow, Nostalgic")
    @Nullable
    @Size(min = MIN_MOOD_SIZE,
            max = MAX_MOOD_SIZE)
    private List<String> mood;

    @Schema(description = "Object / action that we see on the cover",
            example = "wet stone covered with moss, glows a little")
    @Nullable
    @Size(min = MIN_OBJECT_SIZE,
            max = MAX_OBJECT_SIZE)
    private String object;

    @Schema(description = "Description of surrounding of an object or action",
            example = "forest edge, the sun breaks through the bark of the trees")
    @Nullable
    @Size(min = MIN_SURROUNDING_SIZE,
            max = MAX_SURROUNDING_SIZE)
    private String surrounding;

    @Size(min = MIN_COVER_DESCRIPTION_SIZE,
            max = MAX_COVER_DESCRIPTION_SIZE)
    @Nullable
    @Schema(description = "List of adjectives / short phrases that describe cover's style",
            example = "Blurred, 3D")
    private List<String> coverDescription;

    @Nullable
    private Vibe vibe;

    @NotNull
    @Schema(description = "Abstract or realistic parameter")
    private Boolean isAbstract;

    @NotNull
    @Schema(description = "Model of visual AI: dalle-2 or dalle-3")
    private Boolean isLoFi;

    @NotNull
    @Valid
    private UrlDto urlDto;
}
