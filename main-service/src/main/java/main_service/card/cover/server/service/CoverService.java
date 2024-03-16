package main_service.card.cover.server.service;

import main_service.card.playlist.dto.PlaylistNewDto;
import main_service.card.playlist.dto.PlaylistUpdateDto;
import main_service.constants.Constants;

public interface CoverService {
    PlaylistNewDto getCover(String userId, UrlDto url, Constants.Vibe vibe, Boolean isAbstract);

    void savePlaylist(int playlistId, Boolean isPrivate, String userToken);

    PlaylistUpdateDto updateCover(Constants.Vibe vibe, Boolean isAbstract, int playlistId);
}
