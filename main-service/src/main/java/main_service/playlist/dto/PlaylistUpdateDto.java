package main_service.playlist.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import main_service.constants.Constants;
import main_service.cover.entity.Cover;
import main_service.user.dto.UserSmallDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistUpdateDto {
    private int id;
    private Cover cover;
    @Enumerated(EnumType.STRING)
    private Constants.Vibe vibe;
    private UserSmallDto author;
    private Integer hiFiGenerationsLeft;
    private Integer loFiGenerationsLeft;
}