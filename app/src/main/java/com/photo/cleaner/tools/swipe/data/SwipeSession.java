package com.photo.cleaner.tools.swipe.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwipeSession {
    private final String sessionId;
    private final SessionSourceType sourceType;
    private final String sourceId;
    private final List<PhotoItem> photos;
    private final int currentIndex;
    private final Map<String, PhotoDecision> decisions;

    public SwipeSession(String sessionId, SessionSourceType sourceType, String sourceId,
                       List<PhotoItem> photos, int currentIndex) {
        this(sessionId, sourceType, sourceId, photos, currentIndex, new HashMap<>());
    }

    public SwipeSession(String sessionId, SessionSourceType sourceType, String sourceId,
                       List<PhotoItem> photos, int currentIndex, Map<String, PhotoDecision> decisions) {
        this.sessionId = sessionId;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.photos = photos;
        this.currentIndex = currentIndex;
        this.decisions = new HashMap<>(decisions);
    }

    public String getSessionId() {
        return sessionId;
    }

    public SessionSourceType getSourceType() {
        return sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public List<PhotoItem> getPhotos() {
        return photos;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public Map<String, PhotoDecision> getDecisions() {
        return decisions;
    }
}

