package main_service.playlist.dto;


import lombok.*;
import main_service.constants.Constants;
import main_service.cover.dto.CoverPlaylistDto;
import main_service.playlist.track.dto.TrackInPlaylistDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistGetDto {
    private int id;
    private String title;
    private String url;
    private List<TrackInPlaylistDto> tracks;
    private List<CoverPlaylistDto> covers;
    private Boolean isLiked;
}