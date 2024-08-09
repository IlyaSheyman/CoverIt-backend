package main_service.admin.service;

import main_service.admin.dto.StatisticsDto;

public interface AdminService {
    StatisticsDto getStatistics(String userToken, String password);
}
