package main_service.card.playlist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import main_service.card.cover.entity.Cover;
import main_service.card.track.entity.Track;
import main_service.constants.Constants;
import main_service.user.dto.UserSmallDto;
import main_service.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static main_service.constants.Constants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistSaveDto {
    private int id;
    private Boolean isPrivate;
    private UserSmallDto author;
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime savedAt;
    private int generations;
}
