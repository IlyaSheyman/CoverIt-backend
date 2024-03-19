package main_service.card.playlist.dto;

import lombok.*;
import main_service.card.cover.entity.Cover;
import main_service.card.track.entity.Track;
import main_service.constants.Constants;
import main_service.user.entity.User;

import java.util.ArrayList;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistUpdateDto {
    private int id;
    private String title;
    private String url;
    private Constants.Vibe vibe;
    private Boolean isPrivate;
    private Boolean isSaved;
    private User author;
    private ArrayList<Track> tracks;
    private Cover cover;
    private int generations;
}