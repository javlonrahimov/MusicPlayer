package com.rahimovjavlon1212.musicplayer.application;

import android.app.Application;
import com.rahimovjavlon1212.musicplayer.cache.PlayerCache;
import com.rahimovjavlon1212.musicplayer.databases.PlayerDatabase;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;
import com.rahimovjavlon1212.musicplayer.player.MyPlayer;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PlayerDatabase.init(getApplicationContext());
        PlayerDatabase.getPlayerDatabase().createPlaylist(new PlaylistData(-1, "Favourites", ""));
        PlayerCache.init(getApplicationContext());
        MyPlayer.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        PlayerDatabase.getPlayerDatabase().close();
        MyPlayer.getPlayer().release();
        super.onTerminate();
    }
}
