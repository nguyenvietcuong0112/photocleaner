package com.photo.cleaner.tools.swipe.data.repository;

import com.photo.cleaner.tools.swipe.data.PhotoItem;

import java.time.YearMonth;
import java.util.List;

public interface PhotoLoader {
    List<PhotoItem> loadAllPhotos();
    List<PhotoItem> loadPhotosByMonth(YearMonth yearMonth);
    List<PhotoItem> loadOnThisDayPhotos(int day, int month);
    boolean deletePhotos(List<String> photoUris);
}

