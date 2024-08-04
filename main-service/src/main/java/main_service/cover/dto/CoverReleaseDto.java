package main_service.cover.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoverReleaseDto {
    private int id;
    private LocalDateTime created;
    private String link;
    private Boolean isLoFi;
    private String prompt;
    private Boolean isSaved;
    private List<String> mood;
    private String object;
    private String surrounding;
    private List<String> coverDescription;
}