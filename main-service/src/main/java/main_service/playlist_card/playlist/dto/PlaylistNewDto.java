package main_service.playlist_card.playlist.dto;

import lombok.*;
import main_service.playlist_card.cover.entity.Cover;
import main_service.playlist_card.track.dto.TrackInPlaylistDto;
import main_service.constants.Constants;
import main_service.user.dto.UserSmallDto;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistNewDto {
    private int id;
    private String title;
    private String url;
    private Constants.Vibe vibe;
    private Boolean isPrivate;
    private Boolean isSaved;
    private UserSmallDto author;
    private ArrayList<TrackInPlaylistDto> tracks;
    private Cover cover;
    private int generations;
}