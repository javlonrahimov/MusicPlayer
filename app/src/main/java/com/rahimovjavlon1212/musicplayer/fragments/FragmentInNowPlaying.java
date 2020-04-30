package com.rahimovjavlon1212.musicplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.adapters.LibraryAdapter;
import com.rahimovjavlon1212.musicplayer.adapters.PlaylistAdapter;
import com.rahimovjavlon1212.musicplayer.cache.PlayerCache;
import com.rahimovjavlon1212.musicplayer.databases.PlayerDatabase;
import com.rahimovjavlon1212.musicplayer.player.MyPlayer;

import java.util.Objects;


public class FragmentInNowPlaying extends Fragment {

    private boolean hasAddClicked;
    private LibraryAdapter musicAdapter;

    public FragmentInNowPlaying(boolean hasAddClicked) {
        this.hasAddClicked = hasAddClicked;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_now_playing, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFragmentNowPlaying);
        if (hasAddClicked) {
            PlaylistAdapter playlistAdapter = new PlaylistAdapter(getContext(), PlayerDatabase.getPlayerDatabase().getAllPlaylists(), true);
            playlistAdapter.onItemClicked = playlistData -> {
                if (playlistData.addMusicDAta(PlayerCache.getPlayerCache().getCurrentMusic())) {
                    PlayerDatabase.getPlayerDatabase().updatePlaylist(playlistData);
                    Toast.makeText(getContext(), R.string.added_to + playlistData.getName(), Toast.LENGTH_SHORT).show();
                    Objects.requireNonNull(getFragmentManager()).popBackStackImmediate();
                } else {
                    Toast.makeText(getContext(), R.string.already_have_song, Toast.LENGTH_SHORT).show();
                }
            };
            recyclerView.setAdapter(playlistAdapter);
        } else {
            MyPlayer.getPlayer().onChangeListenerFragmentInNowPlaying = () -> musicAdapter.setFocus(PlayerCache.getPlayerCache().getCurrentMusic().getMusicId());
            musicAdapter = new LibraryAdapter(getActivity(), MyPlayer.getPlayer().getCurrentPlaylist());
            musicAdapter.setFocus(PlayerCache.getPlayerCache().getCurrentMusic().getMusicId());
            musicAdapter.onItemClicked = musicData -> MyPlayer.getPlayer().start(musicData, PlayerDatabase.getPlayerDatabase().getPlaylist(PlayerCache.getPlayerCache().getCurrentPlaylistName()));
            recyclerView.setAdapter(musicAdapter);
        }

        return view;
    }
}
