package main_service.release.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.config.security.JwtService;
import main_service.exception.model.BadRequestException;
import main_service.exception.model.NotFoundException;
import main_service.release.dto.ReleaseCollectionDto;
import main_service.release.dto.ReleaseNewDto;
import main_service.release.entity.Release;
import main_service.release.mapper.ReleaseMapper;
import main_service.release.storage.ReleaseRepository;
import main_service.user.entity.User;
import main_service.user.storage.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReleaseServiceImpl implements ReleaseService {
    private final ReleaseRepository releaseRepository;
    private final UserRepository userRepository;
    private final ReleaseMapper mapper;
    private final JwtService jwtService;

    @Override
    public List<ReleaseCollectionDto> getMyReleases(String userToken, int page, int size, String search) {
        User user = extractUserFromToken(userToken);

        List<ReleaseCollectionDto> collection = releaseRepository
                .findAllByAuthor(user)
                .stream()
                .filter(Release::isSaved)
                .filter(release -> search == null || release.getTitle().contains(search))
                .map(mapper::toReleaseCollectionDto)
                .sorted(Comparator.comparing(ReleaseCollectionDto::getCreatedAt))
                .toList();

        int start = page * size;
        int end = Math.min((page + 1) * size, collection.size());

        return collection.subList(start, end);
    }

    private User extractUserFromToken(String userToken) {
        userToken = userToken.substring(7);
        return getUserById(jwtService.extractUserId(userToken));
    }

    private User getUserById(int id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", id)));
    }

    @Override
    public ReleaseNewDto getReleaseById(String userToken, int releaseId) {
        Release release = releaseRepository.findById(releaseId)
                .orElseThrow(() -> new NotFoundException(String.format("Release with id %d not found", releaseId)));
        User user = extractUserFromToken(userToken);

        if (release.getAuthor().getId() == user.getId()) {
            return mapper.toReleaseNewDto(release);
        } else {
            throw new BadRequestException("You should be author of release to get it");
        }
    }

}
