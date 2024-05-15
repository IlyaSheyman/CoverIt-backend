package coverit.image_service.service;

import coverit.image_client.constants.Constants;
import coverit.image_client.dto.PlaylistDto;
import coverit.image_client.dto.ReleaseRequestDto;
import coverit.image_client.dto.UrlDto;
import coverit.image_client.response.CoverResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface ImageService {
    CoverResponse getPlaylistCoverUrl(String url, Constants.Vibe vibe, Boolean isAbstract, Boolean isLoFi) throws ExecutionException, InterruptedException;

    String uploadImage(String imageUrl);

    String getPromptByPlaylist(PlaylistDto playlistDto, Constants.Vibe vibe, Boolean isAbstract);

    void selectTracksFromPlaylist(PlaylistDto playlistDto);

    CoverResponse getReleaseCoverUrl(ReleaseRequestDto request);

    PlaylistDto getPlayListByUrl(String url) throws ExecutionException, InterruptedException;

    String getCoverByPrompt(String prompt, Boolean isLoFi);

    String generateImage(String prompt);

    String chatGpt(String text);

    void deleteImage(UrlDto urlDto);
}
