package com.rahimovjavlon1212.musicplayer.singeltons;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.cache.CurrentMusicCache;
import com.rahimovjavlon1212.musicplayer.models.MusicData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyPlayer extends MediaPlayer {
    private static MyPlayer myPlayer;
    private List<MusicData> playlist;
    public OnChangeListenerLibraryActivity onChangeListenerLibraryActivity;
    public OnChangeListenerAllFragment onChangeListenerAllFragment;

    private MyPlayer(Context context) {
        ContentResolver contentResolver = Objects.requireNonNull(context.getContentResolver());
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = contentResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int musicIdColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int musicTitleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int musicArtistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int musicPathColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            playlist = new ArrayList<>();
            do {
                String musicId = musicCursor.getString(musicIdColumn);
                String musicTitle = musicCursor.getString(musicTitleColumn);
                String musicArtist = musicCursor.getString(musicArtistColumn);
                String musicPath = musicCursor.getString(musicPathColumn);
                playlist.add(new MusicData(musicId, musicTitle, musicArtist, musicPath, R.drawable.image, false));
            } while (musicCursor.moveToNext());
        }
        if (musicCursor != null) {
            musicCursor.close();
        }
    }

    public static MyPlayer getPlayer() {
        return myPlayer;
    }

    public static void init(Context context) {
        if (myPlayer == null) {
            myPlayer = new MyPlayer(context);
        }
        myPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
               MyPlayer.myPlayer.nextMusic();
            }
        });
    }

    public void start(MusicData musicData) {
        myPlayer.start();
        musicData.setFocused(true);
        CurrentMusicCache.getCurrentMusicCache().writeCurrentMusic(musicData);
        onChangeListenerLibraryActivity.onChange();
        onChangeListenerAllFragment.onChange();
    }

    public void nextMusic() {
        if (prevNextCurrent(1)){
            Log.d("HELLO", "Worked next");
            onChangeListenerLibraryActivity.onChange();
            onChangeListenerAllFragment.onChange();
        }
    }

    public void prevMusic() {
        if (prevNextCurrent(-1)){
            Log.d("HELLO", "Worked prev");
            onChangeListenerLibraryActivity.onChange();
            onChangeListenerAllFragment.onChange();
        }
    }

    private boolean prevNextCurrent(int a) {
        int index = getMusicIndex(a);
        if (index >= 0 && index < playlist.size()) {
            MyPlayer.getPlayer().reset();
            try {
                MyPlayer.getPlayer().setDataSource(playlist.get(index).getMusicPath());
                MyPlayer.getPlayer().prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MyPlayer.getPlayer().start(playlist.get(index));
            return true;
        }else {
            return false;
        }
    }

    private int getMusicIndex(int a) {
        int i;
        for (i = 0; i < playlist.size(); i++) {
            if (playlist.get(i).getMusicId().equals(CurrentMusicCache.getCurrentMusicCache().getCurrentMusic().getMusicId())) {
                return i + a;
            }
        }
        return i - 1;
    }

    public int getCurrentIndex() {
        int i;
        for (i = 0; i < playlist.size(); i++) {
            if (playlist.get(i).getMusicId().equals(CurrentMusicCache.getCurrentMusicCache().getCurrentMusic().getMusicId())) {
                return i;
            }
        }
        return i-1;
    }

    public List<MusicData> getPlaylist(){
        return playlist;
    }

    public void setPlaylist(int playlistId){

    }

    public interface OnChangeListenerLibraryActivity {
        void onChange();
    }
    public interface OnChangeListenerAllFragment {
        void onChange();
    }
}
