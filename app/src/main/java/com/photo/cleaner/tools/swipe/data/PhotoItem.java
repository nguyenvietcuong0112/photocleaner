package com.photo.cleaner.tools.swipe.data;

import android.net.Uri;
import java.util.Date;

public class PhotoItem {
    private final String id;
    private final Uri uri;
    private final Date dateTime;
    private final long size;
    private final MediaType type;
    private PhotoDecision decision;

    public PhotoItem(String id, Uri uri, Date dateTime, long size, MediaType type) {
        this.id = id;
        this.uri = uri;
        this.dateTime = dateTime;
        this.size = size;
        this.type = type;
        this.decision = PhotoDecision.UNDECIDED;
    }

    public PhotoItem(String id, Uri uri, Date dateTime, long size, MediaType type, PhotoDecision decision) {
        this.id = id;
        this.uri = uri;
        this.dateTime = dateTime;
        this.size = size;
        this.type = type;
        this.decision = decision;
    }

    public String getId() {
        return id;
    }

    public Uri getUri() {
        return uri;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public long getSize() {
        return size;
    }

    public MediaType getType() {
        return type;
    }

    public PhotoDecision getDecision() {
        return decision;
    }

    public void setDecision(PhotoDecision decision) {
        this.decision = decision;
    }

    public PhotoItem copyWithDecision(PhotoDecision newDecision) {
        return new PhotoItem(id, uri, dateTime, size, type, newDecision);
    }
}

