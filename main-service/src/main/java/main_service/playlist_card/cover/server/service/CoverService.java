package main_service.playlist_card.cover.server.service;

import main_service.playlist_card.playlist.dto.PlaylistNewDto;
import main_service.playlist_card.playlist.dto.PlaylistSaveDto;
import main_service.playlist_card.playlist.dto.PlaylistUpdateDto;
import main_service.constants.Constants;

public interface CoverService {
    PlaylistNewDto getCover(String userId, UrlDto url, Constants.Vibe vibe, Boolean isAbstract);

    PlaylistSaveDto savePlaylist(int playlistId, Boolean isPrivate, String userToken);

    PlaylistUpdateDto updateCover(Constants.Vibe vibe, Boolean isAbstract, int playlistId, String userToken);

    void getMyPlaylists(String userToken);
}
