package coverit.ImageClient.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistDto {
    private String title;
    private ArrayList<TrackDto> tracks;
}
