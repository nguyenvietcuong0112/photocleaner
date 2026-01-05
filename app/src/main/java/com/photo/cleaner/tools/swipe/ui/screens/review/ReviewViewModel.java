package com.photo.cleaner.tools.swipe.ui.screens.review;

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
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReviewViewModel extends ViewModel {
    private final PhotoRepository photoRepository;
    private final String sourceType;
    private final String sourceId;
    private final MutableLiveData<ReviewUiState> _uiState = new MutableLiveData<>(new ReviewUiState());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ReviewViewModel(PhotoRepository photoRepository, String sourceType, String sourceId) {
        this.photoRepository = photoRepository;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
    }

    public LiveData<ReviewUiState> getUiState() {
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

    public void loadReviewData() {
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

            List<PhotoItem> photosWithDecisions = photos.stream()
                .map(photo -> {
                    PhotoDecision decision = decisions.getOrDefault(photo.getId(), PhotoDecision.UNDECIDED);
                    return photo.copyWithDecision(decision);
                })
                .collect(Collectors.toList());

            List<PhotoItem> keepPhotos = photosWithDecisions.stream()
                .filter(photo -> photo.getDecision() == PhotoDecision.KEEP)
                .collect(Collectors.toList());

            List<PhotoItem> deletePhotos = photosWithDecisions.stream()
                .filter(photo -> photo.getDecision() == PhotoDecision.DELETE)
                .collect(Collectors.toList());

            long storageSaved = deletePhotos.stream()
                .mapToLong(PhotoItem::getSize)
                .sum();

            int totalReviewed = (int) photosWithDecisions.stream()
                .filter(photo -> photo.getDecision() != PhotoDecision.UNDECIDED)
                .count();

            ReviewUiState newState = new ReviewUiState(
                photos,
                keepPhotos,
                deletePhotos,
                decisions,
                totalReviewed,
                storageSaved
            );
            _uiState.postValue(newState);
        });
    }

    public void changeDecision(String photoId, PhotoDecision newDecision) {
        executor.execute(() -> {
            ReviewUiState currentState = _uiState.getValue();
            Map<String, PhotoDecision> newDecisions = new HashMap<>(currentState.getDecisions());
            newDecisions.put(photoId, newDecision);

            List<PhotoItem> photosWithDecisions = currentState.getPhotos().stream()
                .map(photo -> {
                    PhotoDecision decision = newDecisions.getOrDefault(photo.getId(), PhotoDecision.UNDECIDED);
                    return photo.copyWithDecision(decision);
                })
                .collect(Collectors.toList());

            List<PhotoItem> keepPhotos = photosWithDecisions.stream()
                .filter(photo -> photo.getDecision() == PhotoDecision.KEEP)
                .collect(Collectors.toList());

            List<PhotoItem> deletePhotos = photosWithDecisions.stream()
                .filter(photo -> photo.getDecision() == PhotoDecision.DELETE)
                .collect(Collectors.toList());

            long storageSaved = deletePhotos.stream()
                .mapToLong(PhotoItem::getSize)
                .sum();

            int totalReviewed = (int) photosWithDecisions.stream()
                .filter(photo -> photo.getDecision() != PhotoDecision.UNDECIDED)
                .count();

            ReviewUiState newState = currentState.copy(
                currentState.getPhotos(),
                keepPhotos,
                deletePhotos,
                newDecisions,
                totalReviewed,
                storageSaved
            );
            _uiState.postValue(newState);

            Map<String, String> decisionsMap = newDecisions.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().name()
                ));
            photoRepository.saveProgress(getSessionId(), decisionsMap);
        });
    }

    public void finishAndDelete(Runnable onFinish) {
        executor.execute(() -> {
            ReviewUiState state = _uiState.getValue();
            List<String> urisToDelete = state.getDeletePhotos().stream()
                .map(photo -> photo.getUri().toString())
                .collect(Collectors.toList());

            boolean success = photoRepository.deletePhotos(urisToDelete);
            if (success) {
                onFinish.run();
            }
        });
    }

    public static String formatFileSize(long bytes) {
        double kb = bytes / 1024.0;
        double mb = kb / 1024.0;
        double gb = mb / 1024.0;

        if (gb >= 1) {
            return String.format("%.2f GB", gb);
        } else if (mb >= 1) {
            return String.format("%.2f MB", mb);
        } else if (kb >= 1) {
            return String.format("%.2f KB", kb);
        } else {
            return bytes + " B";
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}

