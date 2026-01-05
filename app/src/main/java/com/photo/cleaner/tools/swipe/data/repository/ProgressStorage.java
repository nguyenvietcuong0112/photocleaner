package com.photo.cleaner.tools.swipe.data.repository;

import java.util.Map;

public interface ProgressStorage {
    void saveProgress(String sessionId, Map<String, String> photoDecisions);
    Map<String, String> getProgress(String sessionId);
    void clearProgress(String sessionId);
}

