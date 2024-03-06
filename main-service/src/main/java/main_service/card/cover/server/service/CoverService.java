package main_service.card.cover.server.service;

import main_service.card.playlist.dto.PlaylistNewDto;
import main_service.constants.Constants;

public interface CoverService {
    PlaylistNewDto getCover(int userId, UrlDto url, Constants.Vibe vibe, Boolean isAbstract);

    void savePlaylist(int playlistId, UrlDto imageUrl, Boolean isPrivate);
}
