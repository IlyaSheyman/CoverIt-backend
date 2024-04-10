package main_service.playlist.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoverResponse {
    private String url;
    private String prompt;
}
