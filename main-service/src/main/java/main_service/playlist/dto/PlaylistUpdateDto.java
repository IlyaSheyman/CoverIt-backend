package main_service.playlist.dto;

import lombok.*;
import main_service.cover.entity.Cover;
import main_service.user.dto.UserSmallDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistUpdateDto {
    private int id;
    private Cover cover;
    private int generations;
    private UserSmallDto author;
}