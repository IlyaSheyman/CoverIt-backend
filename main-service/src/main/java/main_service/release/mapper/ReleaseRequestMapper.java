package main_service.release.mapper;

import main_service.release.dto.ReleaseRequestDto;
import main_service.release.request.ReleaseRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReleaseRequestMapper {
    ReleaseRequestDto toReleaseRequestDto(ReleaseRequest request);
}
