package main_service.card.track.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static main_service.constants.Constants.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = MIN_AUTHOR_NAME_LENGTH, max = MAX_AUTHOR_NAME_LENGTH)
    private String author;

    @Size(min = MIN_TRACK_TITLE_LENGTH, max = MAX_TRACK_TITLE_LENGTH)
    private String title;
}
