package main_service.playlist.service;

import main_service.constants.Constants;
import main_service.playlist.dto.*;

import java.util.List;

public interface PlaylistService {
    List<PlaylistMyCollectionDto> getMyPlaylists(String userToken,
                                                 int page,
                                                 int size,
                                                 Constants.Filters filters);

    List<PlaylistArchiveDto> getArchive(int page,
                                        int size,
                                        Constants.Filters filters,
                                        String userToken);

    List<PlaylistMyCollectionDto> getLikedPlaylists(String userToken,
                                                    int page,
                                                    int size,
                                                    Constants.Filters filters);

    void like(String userToken, int playlistId);

    void unlikePlaylist(String userToken, int playlistId);

    List<PlaylistUserCollectionDto> getUserPlaylists(String userToken,
                                                     int userId,
                                                     int page,
                                                     int size,
                                                     Constants.Filters filters);

    PlaylistShareDto getPlaylistSharing(int playlistId);

    PlaylistNewDto getPlaylist(int playlistId, String userToken);

    void deletePlaylist(String userToken, int playistId);
}
