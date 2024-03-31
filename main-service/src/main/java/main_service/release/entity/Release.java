package main_service.release.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import main_service.constants.Constants;
import main_service.playlist_card.cover.entity.Cover;
import main_service.playlist_card.track.entity.Track;
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
public class Release {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = MIN_RELEASE_TITLE_LENGTH,
            max = MAX_RELEASE_TITLE_LENGTH)
    private String title;

    private int generations;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "created_at")
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime createdAt;
}