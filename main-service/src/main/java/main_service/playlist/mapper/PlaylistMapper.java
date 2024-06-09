package main_service.playlist.mapper;

import main_service.playlist.dto.*;
import main_service.playlist.entity.Playlist;
import org.mapstruct.Mapper;
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
    PlaylistShareDto toPlaylistGetDto(Playlist playlistById);
    PlaylistLogsDto toPlaylistLogsDto(Playlist newPlaylist);
}