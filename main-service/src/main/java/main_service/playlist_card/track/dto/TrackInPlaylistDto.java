package main_service.playlist_card.track.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackInPlaylistDto {
    private String title;
    private String authors;
}