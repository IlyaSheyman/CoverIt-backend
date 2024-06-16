package main_service.cover.dto;

import lombok.*;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeletedCacheDto {
    private HashMap<Integer, String> playlists;
    private HashMap<Integer, String> releases;
    private List<Integer> covers;
}
