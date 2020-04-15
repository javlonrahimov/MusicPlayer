package com.rahimovjavlon1212.musicplayer.models;


public class MusicData {
    private String title;
    private String artist;
    private String musicPath;
    private int imagePath;

    public MusicData(String title, String artist, String musicPath, int imagePath) {
        this.title = title;
        this.artist = artist;
        this.musicPath = musicPath;
        this.imagePath = imagePath;
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
}
