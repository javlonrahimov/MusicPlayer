package com.rahimovjavlon1212.musicplayer.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.rahimovjavlon1212.musicplayer.fragments.AllFragment;
import com.rahimovjavlon1212.musicplayer.fragments.FavouriteFragment;
import com.rahimovjavlon1212.musicplayer.fragments.PlaylistsFragment;
import com.rahimovjavlon1212.musicplayer.models.MusicData;

import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private List<MusicData> mList;

    public MyPagerAdapter(FragmentManager fm, List<MusicData> list) {
        super(fm);
        this.mList = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                AllFragment allFragment = new AllFragment(mList);
                return allFragment;
            }
            case 1:
                return new FavouriteFragment();
            default:
                return new PlaylistsFragment();
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
                return "Tracks";
            case 1:
                return "Favourite";
            case 2:
                return "Playlists";
            default:
                return null;
        }
    }
}
