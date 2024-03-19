package main_service.card.track.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackDto {
    private List<String> authors;
    private String title;
}