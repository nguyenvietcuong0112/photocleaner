package com.photo.cleaner.tools.swipe.ui.screens.swipe;

import com.photo.cleaner.tools.swipe.data.PhotoDecision;
import com.photo.cleaner.tools.swipe.data.PhotoItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwipeUiState {
    private List<PhotoItem> photos;
    private int currentIndex;
    private Map<String, PhotoDecision> decisions;
    private List<Pair<String, PhotoDecision>> undoStack;

    public SwipeUiState() {
        this(new ArrayList<>(), 0, new HashMap<>(), new ArrayList<>());
    }

    public SwipeUiState(List<PhotoItem> photos, int currentIndex, 
                       Map<String, PhotoDecision> decisions, 
                       List<Pair<String, PhotoDecision>> undoStack) {
        this.photos = photos != null ? photos : new ArrayList<>();
        this.currentIndex = currentIndex;
        this.decisions = decisions != null ? decisions : new HashMap<>();
        this.undoStack = undoStack != null ? undoStack : new ArrayList<>();
    }

    public List<PhotoItem> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoItem> photos) {
        this.photos = photos;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public Map<String, PhotoDecision> getDecisions() {
        return decisions;
    }

    public void setDecisions(Map<String, PhotoDecision> decisions) {
        this.decisions = decisions;
    }

    public List<Pair<String, PhotoDecision>> getUndoStack() {
        return undoStack;
    }

    public void setUndoStack(List<Pair<String, PhotoDecision>> undoStack) {
        this.undoStack = undoStack;
    }

    public SwipeUiState copy(List<PhotoItem> photos, int currentIndex,
                            Map<String, PhotoDecision> decisions,
                            List<Pair<String, PhotoDecision>> undoStack) {
        return new SwipeUiState(photos, currentIndex, decisions, undoStack);
    }

    public static class Pair<A, B> {
        public final A first;
        public final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }
    }
}

