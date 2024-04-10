package main_service.playlist_card.playlist.dto;

import lombok.*;
import main_service.playlist_card.track.dto.TrackDto;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistDto {
    private String title;
    private ArrayList<TrackDto> tracks;
}