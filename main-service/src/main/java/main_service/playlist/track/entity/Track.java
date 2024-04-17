package main_service.playlist.track.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.*;

import static main_service.constants.Constants.MAX_TRACK_TITLE_LENGTH;
import static main_service.constants.Constants.MIN_TRACK_TITLE_LENGTH;

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

    @Size(min = MIN_TRACK_TITLE_LENGTH, max = MAX_TRACK_TITLE_LENGTH)
    private String title;

    private String authors;

}