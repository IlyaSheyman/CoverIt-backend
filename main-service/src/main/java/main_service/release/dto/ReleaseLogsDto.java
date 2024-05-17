package main_service.release.dto;

import lombok.*;
import main_service.user.dto.UserReleaseDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReleaseLogsDto {
    private int id;
    private String title;
    private UserReleaseDto author;
}
