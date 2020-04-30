package com.rahimovjavlon1212.musicplayer.models;


import android.graphics.Bitmap;
import android.net.Uri;

public class MusicData {
    private String musicId;
    private String title;
    private String artist;
    private String musicPath;
    private Bitmap bitmap;
    private Uri imageUri;
    private boolean isFocused;
    private boolean isSelected;

    public MusicData(String musicId, String title, String artist, String musicPath, Uri imageUri, boolean isFocused) {
        this.musicId = musicId;
        this.title = title;
        this.artist = artist;
        this.musicPath = musicPath;
        this.isFocused = isFocused;
        this.imageUri = imageUri;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public String getMusicId() {
        return musicId;
    }

    public boolean isFocused() {
        return isFocused;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}
