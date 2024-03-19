package coverit.ImageClient.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;

import static coverit.ImageClient.constants.Constants.MAX_PLAYLIST_TITLE_LENGTH;
import static coverit.ImageClient.constants.Constants.MIN_PLAYLIST_TITLE_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistDto {
    @Size(min = MIN_PLAYLIST_TITLE_LENGTH,
    max = MAX_PLAYLIST_TITLE_LENGTH)
    private String title;
    @NotNull
    private ArrayList<TrackDto> tracks;

    @Override
    public String toString() {
        return  "title='" + title + '\'' +
                ", tracks=" + tracks;
    }
}
