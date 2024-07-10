package main_service.cover.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import main_service.config.converter.StringListConverter;
import main_service.constants.Constants;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "release_cover")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("RELEASE_COVER")
public class ReleaseCover extends Cover {

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


    @Builder(builderMethodName = "releaseCoverBuilder")
    public ReleaseCover(int id,
                        LocalDateTime created,
                        String link,
                        String prompt,
                        Boolean isAbstract,
                        Boolean isLoFi,
                        Boolean isSaved,
                        Constants.Vibe vibe,
                        List<String> mood,
                        String object,
                        String surrounding,
                        List<String> coverDescription) {
        super(id, created, link, prompt, isAbstract, isLoFi, isSaved, vibe);
        this.mood = mood;
        this.object = object;
        this.surrounding = surrounding;
        this.coverDescription = coverDescription;
    }
}
