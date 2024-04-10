package main_service.playlist_card.playlist.dto;

import lombok.*;
import main_service.playlist_card.cover.entity.Cover;
import main_service.playlist_card.track.dto.TrackInPlaylistDto;
import main_service.constants.Constants;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistArchiveDto {
    private int id;
    private String title;
    private String url;
    private Constants.Vibe vibe;
    private List<TrackInPlaylistDto> tracks;
    private Cover cover;
    private Boolean isLiked;
}