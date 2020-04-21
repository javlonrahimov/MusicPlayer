package com.rahimovjavlon1212.musicplayer.player;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.cache.PlayerCache;
import com.rahimovjavlon1212.musicplayer.models.MusicData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MyPlayer extends MediaPlayer {
    private static MyPlayer myPlayer;
    private List<MusicData> playlist;
    public OnChangeListenerLibraryActivity onChangeListenerLibraryActivity;
    public OnChangeListenerAllFragment onChangeListenerAllFragment;
    public OnChangeListenerNowPlaying onChangeListenerNowPlaying;
    public OnChangeListenerFavouritesFragment onChangeListenerFavouritesFragment;

    private MyPlayer(Context context) {
       setPlaylist(context);
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
                MyPlayer.myPlayer.nextMusic(false);
            }
        });
    }

    public void start(MusicData musicData) {
        myPlayer.start();
        musicData.setFocused(true);
        PlayerCache.getPlayerCache().writeCurrentMusic(musicData);
        onChangeListenerLibraryActivity.onChange();
        onChangeListenerAllFragment.onChange();
        if (onChangeListenerNowPlaying != null) {
            onChangeListenerNowPlaying.onChange();
        }
        if (onChangeListenerFavouritesFragment != null) {
            onChangeListenerFavouritesFragment.onChange();
        }
    }

    public void nextMusic(boolean fromUser) {
        if (fromUser || !PlayerCache.getPlayerCache().getLoopMode()) {
            if (prevNextCurrent(1)) {
                onChangeListenerLibraryActivity.onChange();
                if (onChangeListenerAllFragment != null) {
                    onChangeListenerAllFragment.onChange();
                }
                if (onChangeListenerNowPlaying != null) {
                    onChangeListenerNowPlaying.onChange();
                }
                if (onChangeListenerFavouritesFragment != null) {
                    onChangeListenerFavouritesFragment.onChange();
                }
            }
        } else {
            if (prevNextCurrent(0)) {
                onChangeListenerLibraryActivity.onChange();
                if (onChangeListenerAllFragment != null) {
                    onChangeListenerAllFragment.onChange();
                }
                if (onChangeListenerNowPlaying != null) {
                    onChangeListenerNowPlaying.onChange();
                }
                if (onChangeListenerFavouritesFragment != null) {
                    onChangeListenerFavouritesFragment.onChange();
                }
            }
        }
    }

    public void prevMusic() {
        if (prevNextCurrent(-1)) {
            onChangeListenerLibraryActivity.onChange();
            onChangeListenerAllFragment.onChange();
            if (onChangeListenerNowPlaying != null) {
                onChangeListenerNowPlaying.onChange();
            }
            if (onChangeListenerFavouritesFragment != null) {
                onChangeListenerFavouritesFragment.onChange();
            }
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
        } else {
            return false;
        }
    }

    private int getMusicIndex(int a) {
        int i;

        if (PlayerCache.getPlayerCache().getCurrentMusic().getMusicId().equals("null")){
            return 0;
        }

        if (PlayerCache.getPlayerCache().getShuffleMode() && a != 0) {
            return new Random().nextInt(playlist.size());
        }
        for (i = 0; i < playlist.size(); i++) {
            if (playlist.get(i).getMusicId().equals(PlayerCache.getPlayerCache().getCurrentMusic().getMusicId())) {
                if (i == 0 && a == -1) {
                    return playlist.size() - 1;
                }
                if ((i == playlist.size() - 1 && a == 1)) {
                    return 0;
                }
                return i + a;
            }
        }
        return i - 1;
    }

    public String getCurrentId() {
        int i;
        for (i = 0; i < playlist.size(); i++) {
            if (playlist.get(i).getMusicId().equals(PlayerCache.getPlayerCache().getCurrentMusic().getMusicId())) {
                return PlayerCache.getPlayerCache().getCurrentMusic().getMusicId();
            }
        }
        return i - 1+"";
    }

    public List<MusicData> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Context context) {
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

    public interface OnChangeListenerLibraryActivity {
        void onChange();
    }

    public interface OnChangeListenerAllFragment {
        void onChange();
    }

    public interface OnChangeListenerNowPlaying {
        void onChange();
    }
    public interface OnChangeListenerFavouritesFragment {
        void onChange();
    }
}
