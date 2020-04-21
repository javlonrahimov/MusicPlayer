package com.rahimovjavlon1212.musicplayer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.adapters.LibraryAdapter;
import com.rahimovjavlon1212.musicplayer.databases.PlayerDatabase;
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;
import com.rahimovjavlon1212.musicplayer.player.MyPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {

    private LibraryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<MusicData> mList;

    public FavouriteFragment(List<MusicData> mList) {
        this.mList = mList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        mAdapter = new LibraryAdapter(getActivity(), new ArrayList<>());
        PlaylistData favourites = PlayerDatabase.getPlayerDatabase().getPlaylist("favourites");
        mAdapter.setData(favourites.getPlaylist(mList));
        mAdapter.setFocus(MyPlayer.getPlayer().getCurrentId());
        mAdapter.onItemClicked = new LibraryAdapter.OnItemClicked() {
            @Override
            public void onClick(MusicData musicData, int position) {
                try {
                    MyPlayer.getPlayer().reset();
                    MyPlayer.getPlayer().setDataSource(musicData.getMusicPath());
                    MyPlayer.getPlayer().prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MyPlayer.getPlayer().start(musicData);
            }
        };
        MyPlayer.getPlayer().onChangeListenerFavouritesFragment = new MyPlayer.OnChangeListenerFavouritesFragment(){
            @Override
            public void onChange() {
                mAdapter.setFocus(MyPlayer.getPlayer().getCurrentId());
            }
        };

        PlayerDatabase.onFavButtonClicked = new PlayerDatabase.OnFavButtonClicked() {
            @Override
            public void onClick() {
                PlaylistData favourites = PlayerDatabase.getPlayerDatabase().getPlaylist("favourites");
               mAdapter.setData(favourites.getPlaylist(mList));
            }
        };

        mRecyclerView = view.findViewById(R.id.recyclerViewFavouriteFragment);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
