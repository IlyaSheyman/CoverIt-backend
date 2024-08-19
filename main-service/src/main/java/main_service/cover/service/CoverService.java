package main_service.cover.service;

import main_service.constants.Constants;
import main_service.cover.entity.Cover;
import main_service.playlist.dto.PlaylistNewDto;
import main_service.playlist.dto.PlaylistSaveDto;
import main_service.playlist.dto.PlaylistUpdateDto;
import main_service.playlist.request.PlaylistRequest;
import main_service.release.dto.ReleaseNewDto;
import main_service.release.dto.ReleaseSaveDto;
import main_service.release.dto.ReleaseUpdateDto;
import main_service.release.request.ReleaseRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CoverService {
    ReleaseUpdateDto updateReleaseCover(String userToken, int releaseId, ReleaseRequest request);

    ReleaseSaveDto saveRelease(String userToken, int releaseId, int coverId);

    PlaylistNewDto createPlaylistCover(String userId, PlaylistRequest request);

    PlaylistSaveDto savePlaylist(int playlistId, int coverId, Boolean isPrivate, String userToken);

    PlaylistUpdateDto updatePlaylistCover(Constants.Vibe vibe, Boolean isAbstract, int playlistId, String userToken, Boolean isLoFi);

    @Transactional
    ReleaseNewDto createReleaseCover(String userToken, ReleaseRequest request);

    List<String> getMusicData(String dataType);

    @Transactional
    void deleteCache(String userToken,
                     String password);

    void deleteCover(Cover cover);

}