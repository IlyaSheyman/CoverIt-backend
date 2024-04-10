package main_service.playlist.track.storage;

import main_service.playlist.track.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Integer> {
    Track findByTitleAndAuthors(String title, String authors);
}
