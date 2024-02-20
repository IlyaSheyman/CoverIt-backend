package main_service.card.cover.server.service;

import main_service.constants.Constants;

public interface CoverService {
    void getCover(int userId, String url, Constants.Vibe vibe, Boolean isPrivate);
}
