package main_service.playlist_card.cover.service;

import main_service.playlist_card.playlist.dto.PlaylistNewDto;
import main_service.playlist_card.playlist.dto.PlaylistSaveDto;
import main_service.playlist_card.playlist.dto.PlaylistUpdateDto;
import main_service.constants.Constants;
import main_service.release.dto.ReleaseNewDto;
import main_service.release.request.ReleaseRequest;
import main_service.release.dto.ReleaseUpdateDto;

public interface CoverService {
    PlaylistNewDto getCover(String userId, UrlDto url, Constants.Vibe vibe, Boolean isAbstract, Boolean isLoFi);

    PlaylistSaveDto savePlaylist(int playlistId, Boolean isPrivate, String userToken);

    PlaylistUpdateDto updateCover(Constants.Vibe vibe, Boolean isAbstract, int playlistId, String userToken);

    ReleaseNewDto getReleaseCover(String userToken, ReleaseRequest request);

    ReleaseUpdateDto updateReleaseCover(ReleaseRequest request, int releaseId, String userToken);
}
