package main_service.card.playlist.mapper;

import main_service.card.playlist.dto.*;
import main_service.card.playlist.entity.Playlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface PlaylistMapper {

    PlaylistNewDto toPlaylistNewDto(Playlist playlist);
    PlaylistUpdateDto toPlaylistUpdateDto(Playlist playlist);
    PlaylistSaveDto toPlaylistSaveDto(Playlist playlist);
    PlaylistMyCollectionDto toPlaylistMyCollectionDto(Playlist playlist);
    PlaylistUserCollectionDto toPlaylistUserCollectionDto(Playlist playlist);

    PlaylistArchiveDto toPlaylistArchiveDto(Playlist playlist);
}
