package com.rahimovjavlon1212.musicplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.adapters.LibraryAdapter;
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.player.MyPlayer;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFragment extends Fragment {

    private LibraryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<MusicData> mList;

    public AllFragment(List<MusicData> mList) {
        this.mList = mList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        mAdapter = new LibraryAdapter(getActivity(), mList);
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
        MyPlayer.getPlayer().onChangeListenerAllFragment = new MyPlayer.OnChangeListenerAllFragment(){
            @Override
            public void onChange() {
                mAdapter.setFocus(MyPlayer.getPlayer().getCurrentId());
            }
        };
        mRecyclerView = view.findViewById(R.id.recyclerViewLibraryActivity);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
