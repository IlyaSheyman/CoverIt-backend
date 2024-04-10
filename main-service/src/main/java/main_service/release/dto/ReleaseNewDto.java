package main_service.release.dto;

import lombok.*;
import main_service.playlist_card.cover.dto.CoverSmallDto;
import main_service.user.dto.UserReleaseDto;
import main_service.user.dto.UserSmallDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReleaseNewDto {
    private int id;
    private String title;
    private UserReleaseDto author;
    private CoverSmallDto cover;
    private LocalDateTime createdAt;
}