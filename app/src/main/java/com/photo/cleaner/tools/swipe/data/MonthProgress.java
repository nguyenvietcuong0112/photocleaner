package com.photo.cleaner.tools.swipe.data;

import java.time.YearMonth;

public class MonthProgress {
    private final YearMonth yearMonth;
    private final MonthStatus status;
    private final int totalPhotos;
    private final int reviewedPhotos;
    private final int currentIndex;

    public MonthProgress(YearMonth yearMonth, MonthStatus status, int totalPhotos, int reviewedPhotos) {
        this(yearMonth, status, totalPhotos, reviewedPhotos, 0);
    }

    public MonthProgress(YearMonth yearMonth, MonthStatus status, int totalPhotos, int reviewedPhotos, int currentIndex) {
        this.yearMonth = yearMonth;
        this.status = status;
        this.totalPhotos = totalPhotos;
        this.reviewedPhotos = reviewedPhotos;
        this.currentIndex = currentIndex;
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public MonthStatus getStatus() {
        return status;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public int getReviewedPhotos() {
        return reviewedPhotos;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}

