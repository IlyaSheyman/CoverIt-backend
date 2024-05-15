package main_service.cover.dto;

import lombok.*;

import java.time.LocalDateTime;

import static main_service.constants.Constants.Vibe;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoverPlaylistDto {
    private int id;
    private LocalDateTime created;
    private String link;
    private String prompt;
    private Boolean isAbstract;
    private Boolean isLoFi;
    private Boolean isSaved;
    private Vibe vibe;
}
