package main_service.playlist.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import main_service.constants.Constants;
import main_service.cover.dto.CoverPlaylistDto;
import main_service.user.dto.UserSmallDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistUpdateDto {
    private int id;
    private List<CoverPlaylistDto> covers;
    @Enumerated(EnumType.STRING)
    private Constants.Vibe vibe;
    private UserSmallDto author;
    private Integer hiFiGenerationsLeft;
    private Integer loFiGenerationsLeft;
}