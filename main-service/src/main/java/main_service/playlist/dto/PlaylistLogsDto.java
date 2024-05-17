package main_service.playlist.dto;

import lombok.*;
import main_service.constants.Constants;
import main_service.cover.dto.CoverPlaylistDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistLogsDto {
    private int id;
    private String title;
    private Constants.Vibe vibe;
    private List<CoverPlaylistDto> covers;
    private String url;
}
