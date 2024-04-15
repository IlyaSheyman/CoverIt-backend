package main_service.playlist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import main_service.user.dto.UserSmallDto;

import java.time.LocalDateTime;

import static main_service.constants.Constants.DATE_FORMAT;

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
}
