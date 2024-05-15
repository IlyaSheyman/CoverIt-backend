package main_service.playlist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import main_service.constants.Constants;
import main_service.cover.dto.CoverPlaylistDto;
import main_service.user.dto.UserSmallDto;

import java.time.LocalDateTime;
import java.util.List;

import static main_service.constants.Constants.DATE_FORMAT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistSaveDto {
    private int id;
    private Boolean isPrivate;
    private UserSmallDto author;
    private Constants.Vibe vibe;
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime savedAt;
    private List<CoverPlaylistDto> covers;
}
