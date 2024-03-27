package main_service.card.playlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaylistServiceImpl {
    public void getMyPlaylists(String userToken, int page, int size) {
    }

    public void getArchive(int page, int size, String sort) {
    }

    public void like(String userToken, int playlistId) {
    }

    public void unlikePlaylist(String userToken, int playlistId) {
    }

    public void getUserPlaylists(String requesterToken, int userId, int page, int size) {
    }
}
