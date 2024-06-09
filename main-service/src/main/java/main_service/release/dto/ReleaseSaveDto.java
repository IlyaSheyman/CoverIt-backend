package main_service.release.dto;

import lombok.*;
import main_service.cover.dto.CoverReleaseDto;
import main_service.user.dto.UserReleaseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReleaseSaveDto {
    private int id;
    private String title;
    private UserReleaseDto author;
    private List<CoverReleaseDto> covers;
    private boolean saved;
    private LocalDateTime savedAt;
    private List<String> mood;
    private String object;
    private String surrounding;
    private List<String> coverDescription;
}