package main_service.release.storage;

import main_service.release.entity.Release;
import main_service.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReleaseRepository extends JpaRepository<Release, Integer> {
    List<Release> findAllBySavedFalseAndCreatedAtBefore(LocalDateTime expiration);
    List<Release> findAllByAuthor(User user);
}