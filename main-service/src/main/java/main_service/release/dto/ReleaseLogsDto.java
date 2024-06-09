package main_service.release.dto;

import lombok.*;
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
    private List<String> mood;
    private String object;
    private String surrounding;
    private List<String> coverDescription;
}
