package com.rahimovjavlon1212.musicplayer.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.models.MusicData;

import static com.rahimovjavlon1212.musicplayer.utils.Utils.CURRENT_MUSIC_ARTIST;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.CURRENT_MUSIC_ID;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.CURRENT_MUSIC_IS_FOCUSED;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.CURRENT_MUSIC_KEY;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.CURRENT_MUSIC_PATH;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.CURRENT_MUSIC_TITLE;

public class CurrentMusicCache {
    private static CurrentMusicCache currentMusicCache;
    private static SharedPreferences currentMusicPreferences;

    private CurrentMusicCache(Context context) {
        currentMusicPreferences = context.getSharedPreferences(CURRENT_MUSIC_KEY, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (currentMusicCache == null) {
            currentMusicCache = new CurrentMusicCache(context);
        }
    }

    public static CurrentMusicCache getCurrentMusicCache() {
        return currentMusicCache;
    }

    public void writeCurrentMusic(MusicData musicData) {
        SharedPreferences.Editor editor = currentMusicPreferences.edit();
        editor.putString(CURRENT_MUSIC_ID, musicData.getMusicId());
        editor.putString(CURRENT_MUSIC_TITLE, musicData.getTitle());
        editor.putString(CURRENT_MUSIC_ARTIST, musicData.getArtist());
        editor.putString(CURRENT_MUSIC_PATH, musicData.getMusicPath());
        editor.putBoolean(CURRENT_MUSIC_IS_FOCUSED, musicData.isFocused());
        editor.apply();
    }

    public MusicData getCurrentMusic() {
        String musicId = currentMusicPreferences.getString(CURRENT_MUSIC_ID, "");
        String musicTitle = currentMusicPreferences.getString(CURRENT_MUSIC_TITLE, "");
        String musicArtist = currentMusicPreferences.getString(CURRENT_MUSIC_ARTIST, "");
        String musicPath = currentMusicPreferences.getString(CURRENT_MUSIC_PATH, "");
        boolean isFocused = currentMusicPreferences.getBoolean(CURRENT_MUSIC_IS_FOCUSED, false);

        return new MusicData(musicId, musicTitle, musicArtist, musicPath, R.drawable.image, isFocused);
    }
}
