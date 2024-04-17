package main_service.playlist.track.dto;

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