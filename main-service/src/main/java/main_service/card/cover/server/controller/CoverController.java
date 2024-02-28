package main_service.card.cover.server.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.card.cover.server.service.CoverService;
import main_service.constants.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cover")
public class CoverController {

    private final CoverService service;


    @GetMapping
    public void getPlaylistWithCover(@RequestBody String url,
                                      @RequestParam(name = "vibe", required = false) Constants.Vibe vibe,
                                      @RequestParam(name = "is_private", required = true) @NotNull Boolean isPrivate,
                                      @RequestHeader(value = "X-User-Id") int userId) {
        log.info("[MAIN_SERVER] get cover by playlist URL {}", url);

        service.getCover(userId, url, vibe, isPrivate);
    }
}