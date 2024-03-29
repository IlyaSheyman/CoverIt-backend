package main_service.card.playlist.dto;

import lombok.*;
import main_service.card.cover.entity.Cover;
import main_service.card.track.entity.Track;
import main_service.constants.Constants;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistMyCollectionDto {
    private int id;
    private String title;
    private String url;
    private Constants.Vibe vibe;
    private Boolean isPrivate;
    private List<Track> tracks;
    private Cover cover;
    private Boolean isLiked;
}
