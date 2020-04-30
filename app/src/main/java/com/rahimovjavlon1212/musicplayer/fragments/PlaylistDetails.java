package com.rahimovjavlon1212.musicplayer.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.SelectMusic;
import com.rahimovjavlon1212.musicplayer.adapters.LibraryAdapter;
import com.rahimovjavlon1212.musicplayer.cache.PlayerCache;
import com.rahimovjavlon1212.musicplayer.databases.PlayerDatabase;
import com.rahimovjavlon1212.musicplayer.dialogs.CreatePlaylistDialog;
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;
import com.rahimovjavlon1212.musicplayer.player.MyPlayer;

import java.util.List;
import java.util.Objects;

public class PlaylistDetails extends Fragment {
    private LibraryAdapter mAdapter;
    private List<MusicData> mList;
    private PlaylistData playlistData;
    private static final int REQUEST_CODE = 706;
    private List<MusicData> baseList;


    PlaylistDetails(PlaylistData playlistData, List<MusicData> baseList) {
        this.playlistData = playlistData;
        this.mList = playlistData.getPlaylist(baseList);
        this.baseList = baseList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_details, container, false);
        TextView playlistTitle = view.findViewById(R.id.titlePlaylistDetails);
        playlistTitle.setText(playlistData.getName());
        view.findViewById(R.id.backButtonPlaylistDetails).setOnClickListener(v -> Objects.requireNonNull(getFragmentManager()).popBackStackImmediate());

        view.findViewById(R.id.moreButtonPlaylistDetails).setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(getContext(), view.findViewById(R.id.moreButtonPlaylistDetails));
            menu.inflate(R.menu.menu_more_button);
            menu.show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                menu.setForceShowIcon(true);
            }
            menu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.delete_more_menu: {
                        PlayerDatabase.getPlayerDatabase().deletePlaylist(playlistData);
                        Objects.requireNonNull(getFragmentManager()).popBackStackImmediate();
                        break;
                    }
                    case R.id.rename_more_menu: {
                        CreatePlaylistDialog dialog = new CreatePlaylistDialog(getContext());
                        dialog.setPlaylistName(playlistData.getName());
                        dialog.setButtonText(getResources().getString(R.string.edit));
                        dialog.onResultListener = namePlaylist -> {
                            String oldName = playlistData.getName();
                            playlistData.setName(namePlaylist);
                            if (PlayerDatabase.getPlayerDatabase().updatePlaylist(playlistData)){
                                if (oldName.equals(PlayerCache.getPlayerCache().getCurrentPlaylistName())){
                                    PlayerCache.getPlayerCache().setCurrentPlaylist(playlistData);
                                }
                                playlistTitle.setText(playlistData.getName());
                                dialog.cancel();
                            }else {
                                Toast.makeText(getContext(),getResources().getString(R.string.already_have_playlist), Toast.LENGTH_SHORT).show();
                            }
                        };
                        break;
                    }
                }
                return false;
            });
        });

        view.findViewById(R.id.addButtonPlaylistDetails).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SelectMusic.class);
            intent.putExtra(SelectMusic.PLAYLIST_NAME, playlistData.getName());
            startActivityForResult(intent, REQUEST_CODE);
        });

        mAdapter = new LibraryAdapter(getActivity(), mList);
        mAdapter.setFocus(PlayerCache.getPlayerCache().getCurrentMusic().getMusicId());
        mAdapter.onItemClicked = musicData -> MyPlayer.getPlayer().start(musicData, playlistData);
        MyPlayer.getPlayer().onChangeListenerFragmentDetails = () -> mAdapter.setFocus(PlayerCache.getPlayerCache().getCurrentMusic().getMusicId());
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerViewPlaylistDetails);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            playlistData.setData(Objects.requireNonNull(data.getStringExtra(SelectMusic.SELECTED_TRACKS)));
            PlayerDatabase.getPlayerDatabase().updatePlaylist(playlistData);
            mAdapter.setData(playlistData.getPlaylist(baseList));
        }
    }
}
