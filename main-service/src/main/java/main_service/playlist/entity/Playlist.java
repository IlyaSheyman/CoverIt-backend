package main_service.playlist.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import main_service.constants.Constants;
import main_service.cover.entity.Cover;
import main_service.playlist.track.entity.Track;
import main_service.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToMany
    @JoinTable(
            name = "playlist_track",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private List<Track> tracks;

    @ManyToMany
    @JoinTable(
            name = "playlist_cover",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "cover_id")
    )
    private List<Cover> covers;

    @Column(name = "saved_at")
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime savedAt;

    @Column(name = "created_at")
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime createdAt;

    @Column(name = "hifi_generations_left")
    private Integer hiFiGenerationsLeft;

    @Column(name = "lofi_generations_left")
    private Integer loFiGenerationsLeft;
}