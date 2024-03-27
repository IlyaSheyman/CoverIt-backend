package image_client.dto;

import image_client.constants.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistDto {
    @Size(min = Constants.MIN_PLAYLIST_TITLE_LENGTH,
    max = Constants.MAX_PLAYLIST_TITLE_LENGTH)
    private String title;
    @NotNull
    private ArrayList<TrackDto> tracks;

    @Override
    public String toString() {
        return  "title='" + title + '\'' +
                ", tracks=" + tracks;
    }
}
