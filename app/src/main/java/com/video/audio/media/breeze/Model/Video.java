package com.video.audio.media.breeze.Model;

import android.net.Uri;

public class Video {

    public Uri uri;
    public String name;
    public int duration;
    public int size;
    public String dateAdded;

    public Video(Uri uri, String name, int duration, int size, String dateAdded) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.dateAdded = dateAdded;
    }

    public Uri getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getSize() {
        return size;
    }

    public String getDateAdded() {
        return dateAdded;
    }
}
