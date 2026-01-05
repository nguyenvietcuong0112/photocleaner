package com.photo.cleaner.tools.swipe.data.repository;

import android.os.Build;
import com.photo.cleaner.tools.swipe.data.MonthProgress;
import com.photo.cleaner.tools.swipe.data.MonthStatus;
import com.photo.cleaner.tools.swipe.data.OnThisDayData;
import com.photo.cleaner.tools.swipe.data.PhotoItem;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class PhotoRepositoryImpl implements PhotoRepository {
    private final PhotoLoader photoLoader;
    private final ProgressStorage progressStorage;
    
    public PhotoRepositoryImpl(PhotoLoader photoLoader, ProgressStorage progressStorage) {
        this.photoLoader = photoLoader;
        this.progressStorage = progressStorage;
    }
    
    @Override
    public List<PhotoItem> getAllPhotos() {
        return photoLoader.loadAllPhotos();
    }
    
    @Override
    public List<PhotoItem> getPhotosByMonth(YearMonth yearMonth) {
        return photoLoader.loadPhotosByMonth(yearMonth);
    }
    
    @Override
    public List<PhotoItem> getOnThisDayPhotos(int day, int month) {
        return photoLoader.loadOnThisDayPhotos(day, month);
    }
    
    @Override
    public OnThisDayData getOnThisDayData(int day, int month) {
        List<PhotoItem> photos = getOnThisDayPhotos(day, month);
        if (photos.isEmpty()) {
            return null;
        }
        
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        
        PhotoItem oldestPhoto = photos.stream()
            .min(Comparator.comparing(PhotoItem::getDateTime))
            .orElse(null);
        
        if (oldestPhoto == null) {
            return null;
        }
        
        Calendar oldestCal = Calendar.getInstance();
        oldestCal.setTime(oldestPhoto.getDateTime());
        int oldestYear = oldestCal.get(Calendar.YEAR);
        
        int yearsAgo = currentYear - oldestYear;
        
        return new OnThisDayData(
            yearsAgo,
            photos.get(0).getDateTime(),
            photos.size()
        );
    }
    
    @Override
    public List<MonthProgress> getMonthlyProgress() {
        List<PhotoItem> allPhotos = getAllPhotos();
        Map<YearMonth, List<PhotoItem>> photosByMonth = allPhotos.stream()
            .collect(Collectors.groupingBy(photo -> {
                Calendar cal = Calendar.getInstance();
                cal.setTime(photo.getDateTime());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return YearMonth.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
                }
                return null;
            }));
        
        List<MonthProgress> progressList = new ArrayList<>();
        for (Map.Entry<YearMonth, List<PhotoItem>> entry : photosByMonth.entrySet()) {
            YearMonth yearMonth = entry.getKey();
            List<PhotoItem> photos = entry.getValue();
            
            String sessionId = "month_" + yearMonth;
            Map<String, String> progress = progressStorage.getProgress(sessionId);
            int reviewedCount = progress.size();
            int totalCount = photos.size();
            
            MonthStatus status;
            if (reviewedCount == 0) {
                status = MonthStatus.NOT_STARTED;
            } else if (reviewedCount < totalCount) {
                status = MonthStatus.IN_PROGRESS;
            } else {
                status = MonthStatus.COMPLETED;
            }
            
            int currentIndex = progress.values().stream()
                .mapToInt(v -> {
                    try {
                        return Integer.parseInt(v);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);
            
            progressList.add(new MonthProgress(
                yearMonth,
                status,
                totalCount,
                reviewedCount,
                currentIndex
            ));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            progressList.sort((a, b) -> b.getYearMonth().compareTo(a.getYearMonth()));
        }
        return progressList;
    }
    
    @Override
    public void saveProgress(String sessionId, Map<String, String> photoDecisions) {
        progressStorage.saveProgress(sessionId, photoDecisions);
    }
    
    @Override
    public Map<String, String> getProgress(String sessionId) {
        return progressStorage.getProgress(sessionId);
    }
    
    @Override
    public boolean deletePhotos(List<String> photoUris) {
        return photoLoader.deletePhotos(photoUris);
    }
}

