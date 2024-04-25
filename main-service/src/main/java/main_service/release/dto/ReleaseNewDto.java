package main_service.release.dto;

import lombok.*;
import main_service.cover.dto.CoverReleaseDto;
import main_service.user.dto.UserReleaseDto;

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
    private CoverReleaseDto cover;
    private LocalDateTime createdAt;
}