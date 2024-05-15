package main_service.release.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import main_service.cover.entity.Cover;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToMany
    @JoinTable(
            name = "release_cover",
            joinColumns = @JoinColumn(name = "release_id"),
            inverseJoinColumns = @JoinColumn(name = "cover_id")
    )
    private List<Cover> covers;

    @Column(name = "created_at")
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime createdAt;

    @Column(name = "is_saved")
    private boolean saved;

    @Column(name = "saved_at")
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime savedAt;
}