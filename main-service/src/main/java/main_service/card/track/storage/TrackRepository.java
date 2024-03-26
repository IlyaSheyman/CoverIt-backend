package main_service.card.track.storage;

import main_service.card.track.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Integer> {
    Track findByTitleAndAuthors(String authors, String title);
}
