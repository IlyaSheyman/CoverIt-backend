package main_service.card.cover.server.service;

import lombok.RequiredArgsConstructor;
import main_service.card.cover.client.CoverClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoverServiceImpl implements CoverService {
    private final CoverClient client;

    @Override
    public void getCover(String url, int userId) {
    }
}
