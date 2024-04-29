package main_service.release.storage;

import main_service.release.entity.Release;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReleaseRepository extends JpaRepository<Release, Integer> {
    List<Release> findAllByCreatedAtBefore(LocalDateTime expiration);
}