package com.rahimovjavlon1212.musicplayer.player;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.cache.PlayerCache;
import com.rahimovjavlon1212.musicplayer.databases.PlayerDatabase;
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MyPlayer extends MediaPlayer {
    private static MyPlayer myPlayer;
    private List<MusicData> playlist;
    private List<MusicData> currentPlaylist;
    public OnChangeListenerLibraryActivity onChangeListenerLibraryActivity;
    public OnChangeListenerAllFragment onChangeListenerAllFragment;
    public OnChangeListenerNowPlaying onChangeListenerNowPlaying;
    public OnChangeListenerFragmentDetails onChangeListenerFragmentDetails;
    public OnChangeListenerFavouritesFragment onChangeListenerFavouritesFragment;
    public OnChangeListenerFragmentInNowPlaying onChangeListenerFragmentInNowPlaying;
    private Context context;


    private MyPlayer(Context context) {
        this.context = context;
    }

    public static MyPlayer getPlayer() {
        return myPlayer;
    }

    public static void init(Context context) {
        if (myPlayer == null) {
            myPlayer = new MyPlayer(context);
        }
        myPlayer.setOnCompletionListener(mp -> MyPlayer.myPlayer.nextMusic(false));
    }

    public void start(MusicData musicData, PlaylistData playlistData) {
        if (!playlistData.getName().isEmpty()) {
            currentPlaylist = playlistData.getPlaylist(getPlaylist());
            PlayerCache.getPlayerCache().setCurrentPlaylist(playlistData);
        } else {
            currentPlaylist = playlist;
            PlayerCache.getPlayerCache().setCurrentPlaylist(playlistData);
        }
        if (musicData != null) {
            try {
                MyPlayer.getPlayer().reset();
                MyPlayer.getPlayer().setDataSource(musicData.getMusicPath());
                MyPlayer.getPlayer().prepare();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, context.getResources().getString(R.string.cannot_play), Toast.LENGTH_SHORT).show();
                return;
            }
            PlayerCache.getPlayerCache().writeCurrentMusic(musicData);
        }
        myPlayer.start();
        if (onChangeListenerLibraryActivity != null) {
            onChangeListenerLibraryActivity.onChange();
        }
        if (onChangeListenerAllFragment != null) {
            onChangeListenerAllFragment.onChange();
        }
        if (onChangeListenerNowPlaying != null) {
            onChangeListenerNowPlaying.onChange();
        }
        if (onChangeListenerFragmentInNowPlaying != null) {
            onChangeListenerFragmentInNowPlaying.onChange();
        }
        if (onChangeListenerFavouritesFragment != null) {
            onChangeListenerFavouritesFragment.onChange();
        }
        if (onChangeListenerFragmentDetails != null) {
            onChangeListenerFragmentDetails.onChange();
        }
    }

    public void nextMusic(boolean fromUser) {
        if (fromUser || !PlayerCache.getPlayerCache().getLoopMode()) {
            prevNextCurrent(1);
        } else {
            prevNextCurrent(0);
        }
    }

    public void prevMusic() {
        prevNextCurrent(-1);
    }

    private void prevNextCurrent(int a) {
        int index = getMusicIndex(a);
        MyPlayer.getPlayer().reset();
        MyPlayer.getPlayer().start(currentPlaylist.get(index), PlayerDatabase.getPlayerDatabase().getPlaylist(PlayerCache.getPlayerCache().getCurrentPlaylistName()));
    }

    private int getMusicIndex(int a) {
        int i;

        if (PlayerCache.getPlayerCache().getCurrentMusic().getMusicId().equals("null")) {
            return 0;
        }

        if (PlayerCache.getPlayerCache().getShuffleMode() && a != 0) {
            return new Random().nextInt(currentPlaylist.size());
        }
        for (i = 0; i < currentPlaylist.size(); i++) {
            if (currentPlaylist.get(i).getMusicId().equals(PlayerCache.getPlayerCache().getCurrentMusic().getMusicId())) {
                if (i == 0 && a == -1) {
                    return currentPlaylist.size() - 1;
                }
                if ((i == currentPlaylist.size() - 1 && a == 1)) {
                    return 0;
                }
                return i + a;
            }
        }
        return i - 1;
    }

    public List<MusicData> getPlaylist() {
        return playlist;
    }

    public boolean setPlaylist(Context context) {
        ContentResolver contentResolver = Objects.requireNonNull(context.getContentResolver());
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
        Cursor musicCursor = contentResolver.query(musicUri, null, where, null, null);

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
                long albumId = musicCursor.getLong(musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                Uri songArtworkUri = Uri.parse("content://media//external//audio//albumart");
                Uri albumArtUri = ContentUris.withAppendedId(songArtworkUri, albumId);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), albumArtUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MusicData musicData = new MusicData(musicId, musicTitle, musicArtist, musicPath, albumArtUri, false);
                musicData.setBitmap(bitmap);
                playlist.add(musicData);
            } while (musicCursor.moveToNext());
        }
        Collections.reverse(playlist);
        if (PlayerDatabase.getPlayerDatabase().getPlaylist(PlayerCache.getPlayerCache().getCurrentPlaylistName()).getData().isEmpty()) {
            currentPlaylist = playlist;
        } else {
            currentPlaylist = PlayerDatabase.getPlayerDatabase().getPlaylist(PlayerCache.getPlayerCache().getCurrentPlaylistName()).getPlaylist(playlist);
        }
        if (musicCursor != null) {
            musicCursor.close();
        }
        return true;
    }

    public List<MusicData> getCurrentPlaylist() {
        return currentPlaylist;
    }

    public interface OnChangeListenerLibraryActivity {
        void onChange();
    }

    public interface OnChangeListenerAllFragment {
        void onChange();
    }

    public interface OnChangeListenerFragmentDetails {
        void onChange();
    }

    public interface OnChangeListenerNowPlaying {
        void onChange();
    }

    public interface OnChangeListenerFavouritesFragment {
        void onChange();
    }

    public interface OnChangeListenerFragmentInNowPlaying {
        void onChange();
    }

}
