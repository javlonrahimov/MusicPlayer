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
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;
import com.rahimovjavlon1212.musicplayer.player.MyPlayer;

import java.util.List;

public class AllFragment extends Fragment {

    private LibraryAdapter mAdapter;
    private List<MusicData> mList;

    public AllFragment(List<MusicData> mList) {
        this.mList = mList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        mAdapter = new LibraryAdapter(getActivity(), mList);
        mAdapter.setFocus(PlayerCache.getPlayerCache().getCurrentMusic().getMusicId());
        mAdapter.onItemClicked = musicData -> MyPlayer.getPlayer().start(musicData, new PlaylistData(-1, "", ""));
        MyPlayer.getPlayer().onChangeListenerAllFragment = () -> mAdapter.setFocus(PlayerCache.getPlayerCache().getCurrentMusic().getMusicId());
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerViewLibraryActivity);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
