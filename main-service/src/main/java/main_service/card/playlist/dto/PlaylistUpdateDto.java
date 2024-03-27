package main_service.card.playlist.dto;

import lombok.*;
import main_service.card.cover.entity.Cover;
import main_service.card.track.dto.TrackInPlaylistDto;
import main_service.card.track.entity.Track;
import main_service.constants.Constants;
import main_service.user.entity.User;

import java.util.ArrayList;
import java.util.List;

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