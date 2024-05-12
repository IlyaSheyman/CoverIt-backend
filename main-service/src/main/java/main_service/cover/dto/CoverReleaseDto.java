package main_service.cover.dto;

import lombok.*;

import java.time.LocalDateTime;

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
}