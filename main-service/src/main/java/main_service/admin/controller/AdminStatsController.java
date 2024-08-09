package main_service.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.admin.dto.StatisticsDto;
import main_service.admin.service.AdminService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin stats controller", description = "Private controller with protected endpoints for getting simple statistics ")
public class AdminStatsController {

    private final AdminService service;

    @Operation(summary = "stats endpoint")
    @DeleteMapping("/delete/cache")
    @Transactional
    public StatisticsDto getStatistics(@RequestHeader(name = "Authorization") String userToken,
                                       @RequestHeader(name = "Password") String password) {
        log.info("[MAIN_SERVER] get statistics for admin");
        return service.getStatistics(userToken, password);
    }
}
