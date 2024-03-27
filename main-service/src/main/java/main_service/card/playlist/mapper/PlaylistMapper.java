package main_service.card.playlist.mapper;

import main_service.card.playlist.dto.PlaylistNewDto;
import main_service.card.playlist.dto.PlaylistUpdateDto;
import main_service.card.playlist.entity.Playlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface PlaylistMapper {

    PlaylistNewDto toPlaylistNewDto(Playlist playlist);
    PlaylistUpdateDto toPlaylistUpdateDto(Playlist playlist);
}
