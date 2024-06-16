package main_service.playlist.storage;

import main_service.cover.entity.Cover;
import main_service.playlist.entity.Playlist;
import main_service.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    boolean existsByUrlAndIsSavedTrue(String url);
    List<Playlist> findByAuthor(User user);
    List<Playlist> findAllByIsSavedFalseAndCreatedAtBefore(LocalDateTime expiration);
    Playlist findByCoversContains(Cover cover);
}
