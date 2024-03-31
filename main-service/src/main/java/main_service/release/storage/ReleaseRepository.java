package main_service.release.storage;

import main_service.release.entity.Release;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseRepository extends JpaRepository<Release, Integer> {
}