package com.photo.cleaner.tools.swipe.data.repository;

import com.photo.cleaner.tools.swipe.data.MonthProgress;
import com.photo.cleaner.tools.swipe.data.OnThisDayData;
import com.photo.cleaner.tools.swipe.data.PhotoItem;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface PhotoRepository {
    List<PhotoItem> getAllPhotos();
    List<PhotoItem> getPhotosByMonth(YearMonth yearMonth);
    List<PhotoItem> getOnThisDayPhotos(int day, int month);
    OnThisDayData getOnThisDayData(int day, int month);
    List<MonthProgress> getMonthlyProgress();
    void saveProgress(String sessionId, Map<String, String> photoDecisions);
    Map<String, String> getProgress(String sessionId);
    boolean deletePhotos(List<String> photoUris);
}

