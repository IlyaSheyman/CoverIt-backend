package main_service.cover.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import main_service.config.converter.StringListConverter;
import main_service.constants.Constants;

import java.time.LocalDateTime;
import java.util.List;

import static main_service.constants.Constants.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Inheritance
@DiscriminatorColumn(name = "cover_type")
@DiscriminatorValue("PLAYLIST_COVER")
public class Cover {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_at")
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime created;

    @Size(min = MIN_LINK_LENGTH, max = MAX_LINK_LENGTH)
    private String link;

    private String prompt;

    @Column(name = "is_abstract")
    private Boolean isAbstract;

    @Column(name = "is_lofi")
    private Boolean isLoFi;

    @Column(name = "is_saved")
    private Boolean isSaved;

    @Enumerated(EnumType.STRING)
    @Column(name = "vibe")
    private Constants.Vibe vibe;

    @Convert(converter = StringListConverter.class)
    @Column(name = "mood")
    private List<String> mood;

    @Convert(converter = StringListConverter.class)
    @Column(name = "cover_description")
    private List<String> coverDescription;

    @Column(name = "object")
    @Nullable
    private String object;

    @Column(name = "surrounding")
    private String surrounding;

    @Builder
    public Cover(int id,
                 LocalDateTime created,
                 String link,
                 String prompt,
                 Boolean isAbstract,
                 Boolean isLoFi,
                 Boolean isSaved,
                 Vibe vibe,
                 List<String> mood,
                 List<String> coverDescription,
                 @Nullable String object,
                 String surrounding) {
        this.id = id;
        this.created = created;
        this.link = link;
        this.prompt = prompt;
        this.isAbstract = isAbstract;
        this.isLoFi = isLoFi;
        this.isSaved = isSaved;
        this.vibe = vibe;
        this.mood = mood;
        this.coverDescription = coverDescription;
        this.object = object;
        this.surrounding = surrounding;
    }
}
