package com.rahimovjavlon1212.musicplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.adapters.PlaylistAdapter;
import com.rahimovjavlon1212.musicplayer.databases.PlayerDatabase;
import com.rahimovjavlon1212.musicplayer.dialogs.CreatePlaylistDialog;
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;

import java.util.Collections;
import java.util.List;


public class PlaylistsFragment extends Fragment {

    private PlaylistAdapter mAdapter;
    private List<PlaylistData> mList;
    private List<MusicData> baseList;

    public PlaylistsFragment(List<PlaylistData> mList, List<MusicData> baseList) {
       this.mList = mList;
       this.baseList = baseList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);

        PlayerDatabase.onDataChanged = () -> {
            List<PlaylistData> data = PlayerDatabase.getPlayerDatabase().getAllPlaylists();
            Collections.reverse(data);
            mAdapter.setData(data);
        };
        mAdapter = new PlaylistAdapter(getContext(),mList,false);
        mAdapter.onCreateClicked = () -> {
            CreatePlaylistDialog dialog = new CreatePlaylistDialog(getContext());
            dialog.onResultListener = namePlaylist -> {
               if (PlayerDatabase.getPlayerDatabase().createPlaylist(new PlaylistData(-1,namePlaylist,""))){
                   Toast.makeText(getContext(), getResources().getString(R.string.playlist_created), Toast.LENGTH_SHORT).show();
                   dialog.cancel();
               }else {
                   Toast.makeText(getContext(), getResources().getString(R.string.already_have_playlist_retry), Toast.LENGTH_SHORT).show();
               }
            };
        };
        mAdapter.onItemClicked = playlistData -> {
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().add(R.id.containerForDetails,new PlaylistDetails(playlistData,baseList)).addToBackStack("Details").commit();
            }
        };
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerViewPlaylistsFragment);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
