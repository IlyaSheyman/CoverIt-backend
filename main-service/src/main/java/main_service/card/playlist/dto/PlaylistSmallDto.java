package main_service.card.playlist.dto;

import lombok.*;
import main_service.card.track.dto.TrackDto;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistSmallDto {
    private String title;
    private ArrayList<TrackDto> tracks;
}