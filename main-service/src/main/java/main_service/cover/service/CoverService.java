package main_service.cover.service;

import main_service.constants.Constants;
import main_service.playlist.dto.PlaylistNewDto;
import main_service.playlist.dto.PlaylistSaveDto;
import main_service.playlist.dto.PlaylistUpdateDto;
import main_service.release.dto.ReleaseNewDto;
import main_service.release.request.ReleaseRequest;

public interface CoverService {
    PlaylistNewDto createPlaylistCover(String userId, UrlDto url, Constants.Vibe vibe, Boolean isAbstract, Boolean isLoFi);

    PlaylistSaveDto savePlaylist(int playlistId, int coverId, Boolean isPrivate, String userToken);

    PlaylistUpdateDto updatePlaylistCover(Constants.Vibe vibe, Boolean isAbstract, int playlistId, String userToken, Boolean isLoFi);

    ReleaseNewDto createReleaseCover(String userToken, ReleaseRequest request);

}