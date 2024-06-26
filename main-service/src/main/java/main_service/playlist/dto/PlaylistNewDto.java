package main_service.playlist.dto;

import lombok.*;
import main_service.constants.Constants;
import main_service.cover.dto.CoverPlaylistDto;
import main_service.playlist.track.dto.TrackInPlaylistDto;
import main_service.user.dto.UserSmallDto;

import java.util.ArrayList;
import java.util.List;

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
    private List<CoverPlaylistDto> covers;
    private Integer hiFiGenerationsLeft;
    private Integer loFiGenerationsLeft;
}