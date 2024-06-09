package main_service.release.service;

import main_service.release.dto.ReleaseCollectionDto;
import main_service.release.dto.ReleaseNewDto;

import java.util.List;

public interface ReleaseService {
    List<ReleaseCollectionDto> getMyReleases(String userToken, int page, int size, String search);

    ReleaseNewDto getReleaseById(String userToken, int releaseId);
}
