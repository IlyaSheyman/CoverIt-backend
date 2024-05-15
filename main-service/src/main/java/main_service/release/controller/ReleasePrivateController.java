package main_service.release.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.release.dto.ReleaseCollectionDto;
import main_service.release.service.ReleaseServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/release")
@Tag(name = "Private release controller", description = "For getting release collection")
public class ReleasePrivateController {

    private final ReleaseServiceImpl service; //TODO изменить на интерфейс

    @Operation(summary = "get my releases")
    @GetMapping("/my_collection")
    public List<ReleaseCollectionDto> getMyReleases(@RequestHeader(name = "Authorization") String userToken,
                                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("[MAIN_SERVER] get my releases for user");

        return service.getMyReleases(userToken, page, size);
    }

}
