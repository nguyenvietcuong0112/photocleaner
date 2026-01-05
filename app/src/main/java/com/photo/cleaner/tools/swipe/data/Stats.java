package com.photo.cleaner.tools.swipe.data;

public class Stats {
    private int totalPhotosReviewed;
    private int totalPhotosDeleted;
    private long storageSavedBytes;
    private int completedMonths;
    private int swipeStreakDays;

    public Stats() {
        this(0, 0, 0L, 0, 0);
    }

    public Stats(int totalPhotosReviewed, int totalPhotosDeleted, long storageSavedBytes,
                 int completedMonths, int swipeStreakDays) {
        this.totalPhotosReviewed = totalPhotosReviewed;
        this.totalPhotosDeleted = totalPhotosDeleted;
        this.storageSavedBytes = storageSavedBytes;
        this.completedMonths = completedMonths;
        this.swipeStreakDays = swipeStreakDays;
    }

    public int getTotalPhotosReviewed() {
        return totalPhotosReviewed;
    }

    public void setTotalPhotosReviewed(int totalPhotosReviewed) {
        this.totalPhotosReviewed = totalPhotosReviewed;
    }

    public int getTotalPhotosDeleted() {
        return totalPhotosDeleted;
    }

    public void setTotalPhotosDeleted(int totalPhotosDeleted) {
        this.totalPhotosDeleted = totalPhotosDeleted;
    }

    public long getStorageSavedBytes() {
        return storageSavedBytes;
    }

    public void setStorageSavedBytes(long storageSavedBytes) {
        this.storageSavedBytes = storageSavedBytes;
    }

    public int getCompletedMonths() {
        return completedMonths;
    }

    public void setCompletedMonths(int completedMonths) {
        this.completedMonths = completedMonths;
    }

    public int getSwipeStreakDays() {
        return swipeStreakDays;
    }

    public void setSwipeStreakDays(int swipeStreakDays) {
        this.swipeStreakDays = swipeStreakDays;
    }
}

