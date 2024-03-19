package main_service.card.cover.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

import static main_service.constants.Constants.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Cover {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_at")
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime created;

    @Size(min = MIN_LINK_LENGTH, max = MAX_LINK_LENGTH)
    private String link;

    @Column(name = "is_abstract")
    private Boolean isAbstract;
}
