package main_service.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main_service.admin.dto.StatisticsDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Override
    public StatisticsDto getStatistics(String userToken, String password) {
        return null;
    }


}
