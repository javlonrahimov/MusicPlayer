package com.rahimovjavlon1212.musicplayer.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaylistData {
    private int id;
    private String name;
    private String data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean addMusicDAta(MusicData musicData) {
        if (this.data.contains(musicData.getMusicId())) {
            return false;
        } else {
            this.data += "#" + musicData.getMusicId();
            return true;
        }
    }

    public void minusMusicData(MusicData musicData) {
        this.data = this.data.replace("#" + musicData.getMusicId(), "");
    }

    public PlaylistData(int id, String name, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    public List<MusicData> getPlaylist(List<MusicData> list) {
        List<MusicData> musicDataList = new ArrayList<>();
        List<String> playlistString = Arrays.asList(this.data.split("#"));
        for (int i = 0; i < playlistString.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (playlistString.get(i).equals(list.get(j).getMusicId())) {
                    musicDataList.add(list.get(j));
                    break;
                }
            }
        }
        return musicDataList;
    }
}
