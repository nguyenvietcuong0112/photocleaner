package com.photo.cleaner.tools.swipe.ui.screens.swipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.photo.cleaner.tools.swipe.data.PhotoDecision;
import com.photo.cleaner.tools.swipe.data.PhotoItem;
import com.photo.cleaner.tools.swipe.data.repository.PhotoRepository;

import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class SwipeViewModel extends ViewModel {
    private final PhotoRepository photoRepository;
    private final String sourceType;
    private final String sourceId;
    private final MutableLiveData<SwipeUiState> _uiState = new MutableLiveData<>(new SwipeUiState());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SwipeViewModel(PhotoRepository photoRepository, String sourceType, String sourceId) {
        this.photoRepository = photoRepository;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
    }

    public LiveData<SwipeUiState> getUiState() {
        return _uiState;
    }

    private String getSessionId() {
        switch (sourceType) {
            case "month":
                return "month_" + sourceId;
            case "on_this_day":
                return "on_this_day_" + System.currentTimeMillis();
            default:
                return "session_" + System.currentTimeMillis();
        }
    }

    public void loadPhotos() {
        executor.execute(() -> {
            List<PhotoItem> photos;
            switch (sourceType) {
                case "month":
                    YearMonth yearMonth = YearMonth.parse(sourceId);
                    photos = photoRepository.getPhotosByMonth(yearMonth);
                    break;
                case "on_this_day":
                    Calendar calendar = Calendar.getInstance();
                    photos = photoRepository.getOnThisDayPhotos(
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH) + 1
                    );
                    break;
                default:
                    photos = new ArrayList<>();
            }

            Map<String, String> savedProgress = photoRepository.getProgress(getSessionId());
            Map<String, PhotoDecision> decisions = savedProgress.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> PhotoDecision.valueOf(e.getValue())
                ));

            int currentIndex = savedProgress.values().stream()
                .mapToInt(v -> {
                    try {
                        return Integer.parseInt(v);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);

            currentIndex = Math.min(currentIndex, photos.size() - 1);

            SwipeUiState currentState = _uiState.getValue();
            SwipeUiState newState = currentState.copy(
                photos,
                currentIndex,
                decisions,
                currentState.getUndoStack()
            );
            _uiState.postValue(newState);
        });
    }

    public void decidePhoto(String photoId, PhotoDecision decision) {
        executor.execute(() -> {
            SwipeUiState currentState = _uiState.getValue();
            PhotoDecision previousDecision = currentState.getDecisions().get(photoId);

            Map<String, PhotoDecision> newDecisions = new HashMap<>(currentState.getDecisions());
            newDecisions.put(photoId, decision);

            List<SwipeUiState.Pair<String, PhotoDecision>> newUndoStack = 
                new ArrayList<>(currentState.getUndoStack());
            newUndoStack.add(new SwipeUiState.Pair<>(photoId, previousDecision));

            SwipeUiState newState = currentState.copy(
                currentState.getPhotos(),
                currentState.getCurrentIndex(),
                newDecisions,
                newUndoStack
            );
            _uiState.postValue(newState);

            saveProgress();
        });
    }

    public void moveToNext() {
        SwipeUiState currentState = _uiState.getValue();
        if (currentState.getCurrentIndex() < currentState.getPhotos().size() - 1) {
            SwipeUiState newState = currentState.copy(
                currentState.getPhotos(),
                currentState.getCurrentIndex() + 1,
                currentState.getDecisions(),
                currentState.getUndoStack()
            );
            _uiState.postValue(newState);
        }
    }

    public void undoLastDecision() {
        executor.execute(() -> {
            SwipeUiState currentState = _uiState.getValue();
            if (!currentState.getUndoStack().isEmpty()) {
                SwipeUiState.Pair<String, PhotoDecision> lastAction = 
                    currentState.getUndoStack().get(currentState.getUndoStack().size() - 1);
                
                List<SwipeUiState.Pair<String, PhotoDecision>> newUndoStack = 
                    new ArrayList<>(currentState.getUndoStack());
                newUndoStack.remove(newUndoStack.size() - 1);
                
                Map<String, PhotoDecision> newDecisions = new HashMap<>(currentState.getDecisions());
                if (lastAction.second == null) {
                    newDecisions.remove(lastAction.first);
                } else {
                    newDecisions.put(lastAction.first, lastAction.second);
                }

                int newIndex = Math.max(currentState.getCurrentIndex() - 1, 0);

                SwipeUiState newState = currentState.copy(
                    currentState.getPhotos(),
                    newIndex,
                    newDecisions,
                    newUndoStack
                );
                _uiState.postValue(newState);

                saveProgress();
            }
        });
    }

    private void saveProgress() {
        SwipeUiState state = _uiState.getValue();
        Map<String, String> decisions = state.getDecisions().entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().name()
            ));
        photoRepository.saveProgress(getSessionId(), decisions);
    }

    public Map<String, PhotoDecision> getDecisions() {
        SwipeUiState state = _uiState.getValue();
        return state != null ? state.getDecisions() : new HashMap<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}

