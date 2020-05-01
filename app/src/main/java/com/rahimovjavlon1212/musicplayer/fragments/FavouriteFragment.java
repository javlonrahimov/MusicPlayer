package com.rahimovjavlon1212.musicplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.adapters.LibraryAdapter;
import com.rahimovjavlon1212.musicplayer.cache.PlayerCache;
import com.rahimovjavlon1212.musicplayer.databases.PlayerDatabase;
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;
import com.rahimovjavlon1212.musicplayer.player.MyPlayer;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private LibraryAdapter mAdapter;
    private List<MusicData> mList;

    public FavouriteFragment(List<MusicData> mList) {
        this.mList = mList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        mAdapter = new LibraryAdapter(getContext(), new ArrayList<>());
        PlaylistData favourites = PlayerDatabase.getPlayerDatabase().getPlaylist("favourites");
        List<MusicData> favouritesList = favourites.getPlaylist(mList);
        if (favouritesList.size() == 0){
            view.findViewById(R.id.hintFavFragment).setVisibility(View.VISIBLE);
        }else {
            view.findViewById(R.id.hintFavFragment).setVisibility(View.INVISIBLE);
        }
        mAdapter.setData(favouritesList);
        mAdapter.setFocus(PlayerCache.getPlayerCache().getCurrentMusic().getMusicId());
        mAdapter.onItemClicked = musicData -> MyPlayer.getPlayer().start(musicData, PlayerDatabase.getPlayerDatabase().getPlaylist("Favourites"));
        MyPlayer.getPlayer().onChangeListenerFavouritesFragment = () -> {
            mAdapter.setFocus(PlayerCache.getPlayerCache().getCurrentMusic().getMusicId());
            mAdapter.notifyDataSetChanged();
        };

        PlayerDatabase.onFavButtonClicked = () -> {
            PlaylistData favourites1 = PlayerDatabase.getPlayerDatabase().getPlaylist("favourites");
            mAdapter.setData(favourites1.getPlaylist(mList));
            if (favourites1.getPlaylist(mList).size() == 0){
                view.findViewById(R.id.hintFavFragment).setVisibility(View.VISIBLE);
            }else {
                view.findViewById(R.id.hintFavFragment).setVisibility(View.INVISIBLE);
            }
        };

        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerViewFavouriteFragment);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
