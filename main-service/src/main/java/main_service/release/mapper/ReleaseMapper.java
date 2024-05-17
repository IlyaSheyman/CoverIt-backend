package main_service.release.mapper;

import main_service.release.dto.*;
import main_service.release.entity.Release;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ReleaseMapper {
    ReleaseNewDto toReleaseNewDto(Release newRelease);

    ReleaseUpdateDto toReleaseUpdateDto(Release release);

    ReleaseSaveDto toReleaseSaveDto(Release release);

    ReleaseCollectionDto toReleaseCollectionDto(Release release);

    ReleaseLogsDto toReleaseLogsDto(Release release);
}
