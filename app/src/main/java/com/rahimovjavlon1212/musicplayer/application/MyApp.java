package com.rahimovjavlon1212.musicplayer.application;

import android.app.Application;

import com.rahimovjavlon1212.musicplayer.cache.CurrentMusicCache;
import com.rahimovjavlon1212.musicplayer.singeltons.MyPlayer;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MyPlayer.init(getApplicationContext());
        CurrentMusicCache.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        MyPlayer.getPlayer().release();
    }
}
