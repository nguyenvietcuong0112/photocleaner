package com.photo.cleaner.tools.swipe.ui.screens.review;

import com.photo.cleaner.tools.swipe.data.PhotoDecision;
import com.photo.cleaner.tools.swipe.data.PhotoItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewUiState {
    private List<PhotoItem> photos;
    private List<PhotoItem> keepPhotos;
    private List<PhotoItem> deletePhotos;
    private Map<String, PhotoDecision> decisions;
    private int totalReviewed;
    private long storageSaved;

    public ReviewUiState() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 
             new HashMap<>(), 0, 0L);
    }

    public ReviewUiState(List<PhotoItem> photos, List<PhotoItem> keepPhotos,
                        List<PhotoItem> deletePhotos, Map<String, PhotoDecision> decisions,
                        int totalReviewed, long storageSaved) {
        this.photos = photos != null ? photos : new ArrayList<>();
        this.keepPhotos = keepPhotos != null ? keepPhotos : new ArrayList<>();
        this.deletePhotos = deletePhotos != null ? deletePhotos : new ArrayList<>();
        this.decisions = decisions != null ? decisions : new HashMap<>();
        this.totalReviewed = totalReviewed;
        this.storageSaved = storageSaved;
    }

    public List<PhotoItem> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoItem> photos) {
        this.photos = photos;
    }

    public List<PhotoItem> getKeepPhotos() {
        return keepPhotos;
    }

    public void setKeepPhotos(List<PhotoItem> keepPhotos) {
        this.keepPhotos = keepPhotos;
    }

    public List<PhotoItem> getDeletePhotos() {
        return deletePhotos;
    }

    public void setDeletePhotos(List<PhotoItem> deletePhotos) {
        this.deletePhotos = deletePhotos;
    }

    public Map<String, PhotoDecision> getDecisions() {
        return decisions;
    }

    public void setDecisions(Map<String, PhotoDecision> decisions) {
        this.decisions = decisions;
    }

    public int getTotalReviewed() {
        return totalReviewed;
    }

    public void setTotalReviewed(int totalReviewed) {
        this.totalReviewed = totalReviewed;
    }

    public long getStorageSaved() {
        return storageSaved;
    }

    public void setStorageSaved(long storageSaved) {
        this.storageSaved = storageSaved;
    }

    public ReviewUiState copy(List<PhotoItem> photos, List<PhotoItem> keepPhotos,
                             List<PhotoItem> deletePhotos, Map<String, PhotoDecision> decisions,
                             int totalReviewed, long storageSaved) {
        return new ReviewUiState(photos, keepPhotos, deletePhotos, decisions, 
                                totalReviewed, storageSaved);
    }
}

