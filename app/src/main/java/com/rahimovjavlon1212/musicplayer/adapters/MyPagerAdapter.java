package com.rahimovjavlon1212.musicplayer.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.rahimovjavlon1212.musicplayer.fragments.AllFragment;
import com.rahimovjavlon1212.musicplayer.fragments.FavouriteFragment;
import com.rahimovjavlon1212.musicplayer.fragments.PlaylistsFragment;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AllFragment();
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
                return "All";
            case 1:
                return "Favourite";
            case 2:
                return "Playlists";
            default:
                return null;
        }
    }
}
