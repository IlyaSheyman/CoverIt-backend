package main_service.card.cover.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.card.cover.server.service.CoverService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cover")
public class CoverController {
    private final CoverService service;

//    @GetMapping
//    public void getCoverByPlaylistURL(@RequestBody String url,
//                                      @RequestHeader(value = "X-User-Id") int userId) {
//        log.info("[MAIN_SERVER] get cover by playlist URL {}", url);
//        service.getCover(url, userId);
//    }

}