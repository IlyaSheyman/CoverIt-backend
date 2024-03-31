package main_service.playlist_card.playlist.dto;

import lombok.*;
import main_service.playlist_card.cover.entity.Cover;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistUpdateDto {
    private int id;
    private Cover cover;
    private int generations;
}