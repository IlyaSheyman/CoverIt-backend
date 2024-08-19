package coverit.image_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import coverit.image_client.client.ImageClient;
import coverit.image_client.constants.Constants;
import coverit.image_client.dto.PlaylistDto;
import coverit.image_client.dto.ReleaseRequestDto;
import coverit.image_client.dto.TrackDto;
import coverit.image_client.dto.UrlDto;
import coverit.image_client.exception.BadRequestException;
import coverit.image_client.response.CoverResponse;
import coverit.image_service.storage.config.CloudinaryConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static coverit.image_client.constants.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
@ComponentScan("coverit.image_client.client")
public class ImageServiceImpl implements ImageService {

    private final ImageClient client;
    private final CloudinaryConfig cloudinary;

    @Override
    public CoverResponse getPlaylistCoverUrl(String url, Vibe vibe, Boolean isAbstract, Boolean isLoFi) throws ExecutionException, InterruptedException {
//        if (mood == null
//                && coverDescription == null
//                && object == null
//                && surrounding == null) {
//            response = client.createPlaylist(urlDto, vibe, isAbstract, isLoFi);
//        } else if (mood != null
//                && coverDescription != null
//                && object == null
//                && surrounding == null) {
//            response = client.createPlaylistMiddle(urlDto, mood, coverDescription, isLoFi);
//        } else if (mood != null
//                && coverDescription != null
//                && object != null
//                && surrounding != null) {
//            response = client.createPlaylistAdvanced(urlDto, mood, coverDescription, object, surrounding, isLoFi);
//        }
        //TODO сделать эту логику
        PlaylistDto playlistDto = getPlayListByUrl(url);
        log.info("playlist name: " + playlistDto.getTitle());

        String prompt = getPromptByPlaylist(playlistDto, vibe, isAbstract);

        String imageUrl = getCoverByPrompt(prompt, isLoFi);
        log.info("image url: " + imageUrl);

        String cloudUrl = uploadImage(imageUrl);
        log.info("cloud url: " + cloudUrl);

        return CoverResponse.builder()
                .prompt(prompt)
                .url(cloudUrl)
                .build();
    }

    @Override
    public String uploadImage(String imageUrl) {
        try {
            Cloudinary cloud = cloudinary.cloudinaryConfig();
            Map uploadResult = cloud.uploader().upload(imageUrl, ObjectUtils.emptyMap());

            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException("exception while uploading image: " + e.getMessage());
        }
    }

    @Override
    public String getPromptByPlaylist(PlaylistDto playlistDto, Constants.Vibe vibe, Boolean isAbstract) {
        if (isAbstract == null) {
            throw new BadRequestException("parameter isAbstract is not present");
        }

        if (playlistDto.getTracks().size() > MAX_PLAYLIST_SIZE) {
            selectTracksFromPlaylist(playlistDto);
        }
        String gptResult = client.getPromptByPlaylist(playlistDto, vibe, isAbstract);

        StringBuilder prompt = new StringBuilder();
        prompt.append("An image depicting: ");
        prompt.append(gptResult).append(" ");

        if (vibe == null) {
            prompt.append(NONE_VIBE_DALLE_INSTRUCTION);
        } else {
            switch (vibe) {
                case DANCING_FLOOR:
                    prompt.append(DANCING_FLOOR_INSTRUCTION);
                    break;
                case NATURE_DOES_NOT_CARE:
                    prompt.append(NATURE_INSTRUCTION);
                    break;
                case BREAKING_DOWN:
                    prompt.append(BREAKING_DOWN_INSTRUCTION);
                    break;
                case CAMPFIRE_CALMNESS:
                    prompt.append(CAMPFIRE_CALMNESS_INSTRUCTION);
                    break;
                case TOUGH_AND_STRAIGHT:
                    prompt.append(TOUGH_AND_STRAIGHT_INSTRUCTION);
                    break;
                case ENDLESS_JOY:
                    prompt.append(ENDLESS_JOY_INSTRUCTION);
                    break;
                default:
                    throw new BadRequestException("this vibe is not present");
            }
        }

        log.info("prompt for DALLE: " + prompt);

        return prompt.toString();
    }

    @Override
    public void selectTracksFromPlaylist(PlaylistDto playlistDto) {
        List<TrackDto> tracks = playlistDto.getTracks();
        ArrayList<TrackDto> selectedTracks = new ArrayList<>();

        int step = tracks.size() / (MAX_PLAYLIST_SIZE + 1);

        for (int i = step; i < tracks.size() && selectedTracks.size() < MAX_PLAYLIST_SIZE; i += step) {
            selectedTracks.add(tracks.get(i));
        }

        playlistDto.getTracks().removeAll(tracks);
        playlistDto.setTracks(selectedTracks);

        log.info(MAX_PLAYLIST_SIZE + " tracks were selected from the playlist. current playlist size: "
                + playlistDto.getTracks().size());
    }

    @Override
    public CoverResponse getReleaseCoverUrl(ReleaseRequestDto request) {
        CoverResponse response = client.getReleaseCoverUrl(request);

        String cloudUrl = uploadImage(response.getUrl());
        log.info("cloud url: " + cloudUrl);

        response.setUrl(cloudUrl);

        return response;
    }

    @Override
    public PlaylistDto getPlayListByUrl(String url) throws ExecutionException, InterruptedException {
        return client.getPlayListByUrl(url);
    }

    @Override
    public String getCoverByPrompt(String prompt, Boolean isLoFi) {
        return client.getCoverByPrompt(prompt, isLoFi);
    }

    @Override
    public String generateImage(String prompt) {
        return client.generateImage(prompt);
    }

    @Override
    public String chatGpt(String text) {
        return client.chatGpt(text);
    }

    @Override
    public void deleteImage(UrlDto urlDto) {
        String imageId = extractImageId(urlDto);
        deleteById(imageId);
        log.info("image was deleted from storage successfully");
    }

    private void deleteById(String imageId) {
        Cloudinary cloud = cloudinary.cloudinaryConfig();
        try {
            cloud.uploader().destroy(imageId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("error while deleting image: " + e.getMessage());
        }
    }

    private String extractImageId(UrlDto urlDto) {
        Pattern PUBLIC_ID_PATTERN = Pattern.compile("/v(\\d+)/([^\\.]+)\\.");
        String link = urlDto.getLink();

        Matcher matcher = PUBLIC_ID_PATTERN.matcher(link);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            throw new BadRequestException("image id is not found in the url");
        }
    }
}