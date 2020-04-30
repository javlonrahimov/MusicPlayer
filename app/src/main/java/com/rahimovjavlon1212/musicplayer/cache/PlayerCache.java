package com.rahimovjavlon1212.musicplayer.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;

import java.io.IOException;

import static com.rahimovjavlon1212.musicplayer.utils.Utils.CURRENT_MUSIC_ARTIST;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.CURRENT_MUSIC_ID;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.CURRENT_MUSIC_IS_FOCUSED;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.CURRENT_MUSIC_KEY;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.CURRENT_MUSIC_PATH;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.CURRENT_MUSIC_TITLE;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.CURRENT_PLAYLIST_NAME;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.IS_LOOP_MODE;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.IS_SHUFFLE_MODE;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.PLAYER_PREFERENCES_KEY;

public class PlayerCache {
    private static PlayerCache playerCache;
    private static SharedPreferences currentMusicPreferences;
    private static SharedPreferences playerPreferences;
    private Context context;

    private PlayerCache(Context context) {
        this.context = context;
        currentMusicPreferences = context.getSharedPreferences(CURRENT_MUSIC_KEY, Context.MODE_PRIVATE);
        playerPreferences = context.getSharedPreferences(PLAYER_PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (playerCache == null) {
            playerCache = new PlayerCache(context);
        }
    }

    public static PlayerCache getPlayerCache() {
        return playerCache;
    }

    public void writeShuffleMode(boolean isShuffleMode) {
        SharedPreferences.Editor editor = playerPreferences.edit();
        editor.putBoolean(IS_SHUFFLE_MODE, isShuffleMode);
        editor.apply();
    }

    public void writeLoopMode(boolean isLoopMode) {
        SharedPreferences.Editor editor = playerPreferences.edit();
        editor.putBoolean(IS_LOOP_MODE, isLoopMode);
        editor.apply();
    }

    public void setCurrentPlaylist(PlaylistData playlist) {
        SharedPreferences.Editor editor = playerPreferences.edit();
        editor.putString(CURRENT_PLAYLIST_NAME, playlist.getName());
        editor.apply();
    }

    public String getCurrentPlaylistName() {
        return playerPreferences.getString(CURRENT_PLAYLIST_NAME, "");
    }

    public boolean getShuffleMode() {
        return playerPreferences.getBoolean(IS_SHUFFLE_MODE, false);
    }

    public boolean getLoopMode() {
        return playerPreferences.getBoolean(IS_LOOP_MODE, false);
    }

    public void writeCurrentMusic(MusicData musicData) {
        SharedPreferences.Editor editor = currentMusicPreferences.edit();
        editor.putString(CURRENT_MUSIC_ID, musicData.getMusicId());
        editor.putString(CURRENT_MUSIC_TITLE, musicData.getTitle());
        editor.putString(CURRENT_MUSIC_ARTIST, musicData.getArtist());
        editor.putString(CURRENT_MUSIC_PATH, musicData.getMusicPath());
        editor.putBoolean(CURRENT_MUSIC_IS_FOCUSED, musicData.isFocused());
        editor.putString("music_art", musicData.getImageUri().toString());
        editor.apply();
    }

    public MusicData getCurrentMusic() {
        String musicId = currentMusicPreferences.getString(CURRENT_MUSIC_ID, "null");
        String musicTitle = currentMusicPreferences.getString(CURRENT_MUSIC_TITLE, "");
        String musicArtist = currentMusicPreferences.getString(CURRENT_MUSIC_ARTIST, "");
        String musicPath = currentMusicPreferences.getString(CURRENT_MUSIC_PATH, "");
        Uri imageUri = Uri.parse(currentMusicPreferences.getString("music_art", ""));
        boolean isFocused = currentMusicPreferences.getBoolean(CURRENT_MUSIC_IS_FOCUSED, false);

        return new MusicData(musicId, musicTitle, musicArtist, musicPath, imageUri, isFocused);
    }

    public Bitmap getCurrentBitmap() {
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), getCurrentMusic().getImageUri());
        } catch (IOException e) {
            e.printStackTrace();
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.image);
        }
    }
}
