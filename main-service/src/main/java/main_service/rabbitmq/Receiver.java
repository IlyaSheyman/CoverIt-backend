package main_service.rabbitmq;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.cover.client.CoverClient;
import main_service.cover.entity.Cover;
import main_service.cover.service.UrlDto;
import main_service.cover.storage.CoverRepository;
import main_service.cover.storage.ReleaseCoverRepository;
import main_service.exception.model.NotFoundException;
import main_service.logs.service.TelegramLogsService;
import main_service.playlist.entity.Playlist;
import main_service.playlist.storage.PlaylistRepository;
import main_service.release.entity.Release;
import main_service.release.storage.ReleaseRepository;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class Receiver {

    private final PlaylistRepository playlistRepository;
    private final CoverRepository coverRepository;
    private final ReleaseCoverRepository releaseCoverRepository; //TODO прочекать необходимость
    private final ReleaseRepository releaseRepository;

    private final TelegramLogsService logsService;
    private final CoverClient coverClient;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "cover.check.queue"),
            exchange = @Exchange(value = "cover.exchange", delayed = "true"), key = "cover.check.queue"),
            errorHandler = "rabbitListenerErrorHandler")
    @Transactional
    public void listen(String message) {
        int coverId = parseMessage(message);
        Cover cover = getCoverById(coverId);
        log.info("Message to check cover with id " + coverId + " received");

        if (!cover.getIsSaved()) {
            Playlist playlistSource = playlistRepository.findByCoversContains(cover);
            Release releaseSource = releaseRepository.findByCoversContains(cover);

            if (playlistSource != null) {
                playlistSource.getCovers().remove(cover);
                playlistRepository.save(playlistSource);
            } else if (releaseSource != null) {
                releaseSource.getCovers().remove(cover);
                releaseRepository.save(releaseSource);
            }

            deleteCover(cover);

            logsService.info("Cover deleted",
                    String.format("Cover with id %d is deleted (async)", coverId),
                    null,
                    null);
        }
    }

    private Cover getCoverById(int coverId) {
        return coverRepository.findById(coverId)
                .orElseThrow(() -> new NotFoundException("Cover with id " + coverId + " not found"));
    }

    public void deleteCover(Cover cover) {
        coverRepository.delete(cover);
        coverClient.deleteCover(UrlDto.builder().link(cover.getLink()).build());
    }

    private int parseMessage(String message) {
        // "Check cover saved status for cover ID {coverId} in 2 days"
        String[] parts = message.split(" ");
        return Integer.parseInt(parts[7]);
    }
}
