package main_service.cover.entity;

import jakarta.persistence.*;
import lombok.*;
import main_service.config.converter.StringListConverter;
import main_service.constants.Constants;
import org.jetbrains.annotations.Nullable;

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
                        List<String> coverDescription,
                        @Nullable String object,
                        String surrounding) {
        super(id,
                created,
                link,
                prompt,
                isAbstract,
                isLoFi,
                isSaved,
                vibe,
                mood,
                coverDescription,
                object,
                surrounding);
    }
}
