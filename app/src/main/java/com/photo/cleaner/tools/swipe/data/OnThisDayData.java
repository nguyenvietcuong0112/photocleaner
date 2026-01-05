package com.photo.cleaner.tools.swipe.data;

import java.util.Date;

public class OnThisDayData {
    private final int yearsAgo;
    private final Date date;
    private final int photoCount;

    public OnThisDayData(int yearsAgo, Date date, int photoCount) {
        this.yearsAgo = yearsAgo;
        this.date = date;
        this.photoCount = photoCount;
    }

    public int getYearsAgo() {
        return yearsAgo;
    }

    public Date getDate() {
        return date;
    }

    public int getPhotoCount() {
        return photoCount;
    }
}

