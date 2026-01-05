package com.photo.cleaner.tools.swipe.data.repository;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import com.photo.cleaner.tools.swipe.data.MediaType;
import com.photo.cleaner.tools.swipe.data.PhotoItem;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PhotoLoaderImpl implements PhotoLoader {
    private final Context context;
    
    public PhotoLoaderImpl(Context context) {
        this.context = context;
    }
    
    @Override
    public List<PhotoItem> loadAllPhotos() {
        List<PhotoItem> photos = new ArrayList<>();
        
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        
        String[] projection = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };
        
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        
        try (Cursor cursor = context.getContentResolver().query(
                collection,
                projection,
                null,
                null,
                sortOrder)) {
            
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
                int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
                int bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    long dateTaken = cursor.getLong(dateColumn);
                    long size = cursor.getLong(sizeColumn);
                    String bucketName = cursor.getString(bucketColumn);
                    
                    Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    );
                    
                    MediaType type;
                    if (bucketName != null && bucketName.toLowerCase().contains("screenshot")) {
                        type = MediaType.SCREENSHOT;
                    } else {
                        type = MediaType.PHOTO;
                    }
                    
                    photos.add(new PhotoItem(
                        String.valueOf(id),
                        contentUri,
                        new Date(dateTaken),
                        size,
                        type
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return photos;
    }
    
    @Override
    public List<PhotoItem> loadPhotosByMonth(YearMonth yearMonth) {
        List<PhotoItem> allPhotos = loadAllPhotos();
        List<PhotoItem> filtered = new ArrayList<>();
        
        for (PhotoItem photo : allPhotos) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(photo.getDateTime());
            YearMonth photoYearMonth = YearMonth.of(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1
            );
            
            if (photoYearMonth.equals(yearMonth)) {
                filtered.add(photo);
            }
        }
        
        return filtered;
    }
    
    @Override
    public List<PhotoItem> loadOnThisDayPhotos(int day, int month) {
        List<PhotoItem> allPhotos = loadAllPhotos();
        List<PhotoItem> filtered = new ArrayList<>();
        
        for (PhotoItem photo : allPhotos) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(photo.getDateTime());
            int photoDay = cal.get(Calendar.DAY_OF_MONTH);
            int photoMonth = cal.get(Calendar.MONTH) + 1;
            
            if (photoDay == day && photoMonth == month) {
                filtered.add(photo);
            }
        }
        
        return filtered;
    }
    
    @Override
    public boolean deletePhotos(List<String> photoUris) {
        try {
            for (String uriString : photoUris) {
                Uri uri = Uri.parse(uriString);
                context.getContentResolver().delete(uri, null, null);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

