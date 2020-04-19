package com.rahimovjavlon1212.musicplayer.models;


public class MusicData {
    private String musicId;
    private String title;
    private String artist;
    private String musicPath;
    private int imagePath;
    private boolean isFocused;

    public MusicData(String musicId, String title, String artist, String musicPath, int imagePath, boolean isFocused) {
        this.musicId = musicId;
        this.title = title;
        this.artist = artist;
        this.musicPath = musicPath;
        this.imagePath = imagePath;
        this.isFocused = isFocused;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getImagePath() {
        return imagePath;
    }

    public void setImagePath(int imagePath) {
        this.imagePath = imagePath;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public boolean isFocused() {
        return isFocused;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }
}
