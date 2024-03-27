package main_service.card.playlist.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import main_service.card.cover.entity.Cover;
import main_service.card.track.dto.TrackDto;
import main_service.card.track.dto.TrackInPlaylistDto;
import main_service.card.track.entity.Track;
import main_service.constants.Constants;
import main_service.user.entity.User;

import java.util.ArrayList;

import static main_service.constants.Constants.*;
import static main_service.constants.Constants.MAX_LINK_LENGTH;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistNewDto {
    private int id;
    private String title;
    private String url;
    private Constants.Vibe vibe;
    private Boolean isPrivate;
    private Boolean isSaved;
    private User author;
    private ArrayList<TrackInPlaylistDto> tracks;
    private Cover cover;
    private int generations;
}