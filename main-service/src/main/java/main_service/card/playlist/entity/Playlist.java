package main_service.card.playlist.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main_service.card.cover.entity.Cover;
import main_service.card.track.entity.Track;
import main_service.constants.Constants;
import main_service.user.entity.User;

import java.util.ArrayList;

import static main_service.constants.Constants.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = MIN_PLAYLIST_TITLE_LENGTH,
        max = MAX_PLAYLIST_TITLE_LENGTH)
    private String title;

    @Size(min = MIN_LINK_LENGTH,
        max = MAX_LINK_LENGTH)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "vibe")
    private Constants.Vibe vibe;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @Column(name = "is_saved")
    private Boolean isSaved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany
    @JoinColumn(name = "track_id")
    private ArrayList<Track> tracks;

    @OneToOne
    @JoinColumn(name = "cover_id")
    private Cover cover;

    private int generations;
}