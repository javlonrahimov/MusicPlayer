package com.rahimovjavlon1212.musicplayer.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.databases.PlayerDatabase;
import com.rahimovjavlon1212.musicplayer.fragments.AllFragment;
import com.rahimovjavlon1212.musicplayer.fragments.FavouriteFragment;
import com.rahimovjavlon1212.musicplayer.fragments.PlaylistsFragment;
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;

import java.util.Collections;
import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private List<MusicData> mList;
    private Context context;

    public MyPagerAdapter(Context context, FragmentManager fm, List<MusicData> list) {
        super(fm);
        this.context = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                return new AllFragment(mList);
            }
            case 1: {
                return new FavouriteFragment(mList);
            }
            default: {
                List<PlaylistData> playlistDataList = PlayerDatabase.getPlayerDatabase().getAllPlaylists();
                Collections.reverse(playlistDataList);
                return new PlaylistsFragment(playlistDataList, mList);
            }
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.tracksTitle);
            case 1:
                return context.getResources().getString(R.string.favourites);
            case 2:
                return context.getResources().getString(R.string.playlists);
            default:
                return null;
        }
    }
}
