package main_service.cover.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoverReleaseDto {
    private String link;
    private Boolean isLoFi;
    private String prompt;
}