package com.rahimovjavlon1212.musicplayer.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;

import java.util.ArrayList;
import java.util.List;

import static com.rahimovjavlon1212.musicplayer.utils.Utils.COL_PLAYLIST_DATA;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.COL_PLAYLIST_ID;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.COL_PLAYLIST_NAME;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.PLAYER_DATABASE_NAME;
import static com.rahimovjavlon1212.musicplayer.utils.Utils.TABLE_PLAYLISTS;

public class PlayerDatabase extends SQLiteOpenHelper {

    private static PlayerDatabase playerDatabase;
    public static OnFavButtonClicked onFavButtonClicked;


    private PlayerDatabase(@Nullable Context context) {
        super(context, PLAYER_DATABASE_NAME, null, 1);
    }

    public static void init(Context context) {
        if (playerDatabase == null) {
            playerDatabase = new PlayerDatabase(context);
        }
    }

    public static PlayerDatabase getPlayerDatabase() {
        return playerDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (db != null) {
            db.execSQL("CREATE TABLE " + TABLE_PLAYLISTS + " ( " + COL_PLAYLIST_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," + COL_PLAYLIST_NAME + " TEXT," + COL_PLAYLIST_DATA + " TEXT)");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db != null) {
            db.execSQL("DROP TABLE " + TABLE_PLAYLISTS);
            onCreate(db);
        }
    }

    public boolean createPlaylist(PlaylistData playlistData) {
        if (checkForUnique(playlistData)) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_PLAYLIST_NAME, playlistData.getName());
            contentValues.put(COL_PLAYLIST_DATA, playlistData.getData());
            db.insert(TABLE_PLAYLISTS, null, contentValues);
            return true;
        } else {
            return false;
        }
    }

    public boolean updatePlaylist(PlaylistData playlistData) {
        if (checkForUnique(playlistData)) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_PLAYLIST_NAME, playlistData.getName());
            contentValues.put(COL_PLAYLIST_DATA, playlistData.getData());
            db.update(TABLE_PLAYLISTS, contentValues, COL_PLAYLIST_ID + " == " + playlistData.getId(), null);
            return true;
        }else {
            return false;
        }
    }

    public void deletePlaylist(PlaylistData playlistData) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PLAYLISTS, COL_PLAYLIST_ID + " == " + playlistData.getId(), null);
    }

    public List<PlaylistData> getAllPlaylists() {
        SQLiteDatabase db = getWritableDatabase();
        List<PlaylistData> data = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_PLAYLISTS, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            data.add(new PlaylistData(
                    cursor.getInt(cursor.getColumnIndex(COL_PLAYLIST_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_PLAYLIST_NAME)),
                    cursor.getString(cursor.getColumnIndex(COL_PLAYLIST_DATA))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return data;
    }

    public PlaylistData getPlaylist(String name) {
        List<PlaylistData> data = getAllPlaylists();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equalsIgnoreCase(name)) {
                return data.get(i);
            }
        }
        return new PlaylistData(-1, "", "");
    }

    public boolean isFav(MusicData musicData) {
        PlaylistData favourites = getPlaylist("Favourites");
        return favourites.getData().contains(musicData.getMusicId());
    }

    public void addFav(MusicData musicData) {
        PlaylistData favourites = getPlaylist("Favourites");
        favourites.addMusicDAta(musicData);
        updatePlaylist(favourites);
        if (onFavButtonClicked != null) {
            onFavButtonClicked.onClick();
        }
    }

    public void minusFav(MusicData musicData) {
        PlaylistData favourites = getPlaylist("Favourites");
        favourites.minusMusicData(musicData);
        updatePlaylist(favourites);
        if (onFavButtonClicked != null) {
            onFavButtonClicked.onClick();
        }
    }

    private boolean checkForUnique(PlaylistData playlistData) {
        List<PlaylistData> list = getAllPlaylists();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == playlistData.getId()) {
                list.remove(list.get(i));
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(playlistData.getName())) {
                return false;
            }
        }
        return true;
    }

    public interface OnFavButtonClicked {
        void onClick();
    }
}
