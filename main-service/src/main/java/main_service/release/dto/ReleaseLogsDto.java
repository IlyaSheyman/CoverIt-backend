package main_service.release.dto;

import lombok.*;
import main_service.cover.dto.CoverReleaseDto;
import main_service.user.dto.UserReleaseDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReleaseLogsDto {
    private int id;
    private String title;
    private UserReleaseDto author;
    private List<CoverReleaseDto> covers;
}
