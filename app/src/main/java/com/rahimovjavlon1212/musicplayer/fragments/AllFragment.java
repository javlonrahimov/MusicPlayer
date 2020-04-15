package com.rahimovjavlon1212.musicplayer.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.adapters.LibraryAdapter;
import com.rahimovjavlon1212.musicplayer.models.MusicData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFragment extends Fragment {

    private LibraryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<MusicData> mList;

    public AllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        getMusic();
        mAdapter = new LibraryAdapter(getActivity(), mList);
        mAdapter.onItemClicked = new LibraryAdapter.OnItemClicked() {
            @Override
            public void onClick(String path) {
                MediaPlayer player = MediaPlayer.create(getActivity(), Uri.parse(path));
                if (player != null) {
                    player.start();
                } else {
                    Toast.makeText(getContext(), path, Toast.LENGTH_SHORT).show();
                }
            }
        };
        mRecyclerView = view.findViewById(R.id.recyclerViewLibraryActivity);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private void getMusic() {
        ContentResolver contentResolver = Objects.requireNonNull(getActivity()).getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = contentResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int musicTitleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int musicArtistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int musicPathColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            mList = new ArrayList<>();
            do {
                String musicTitle = musicCursor.getString(musicTitleColumn);
                String musicArtist = musicCursor.getString(musicArtistColumn);
                String musicPath = musicCursor.getString(musicPathColumn);
                mList.add(new MusicData(musicTitle, musicArtist, musicPath, R.drawable.image));
            } while (musicCursor.moveToNext());
        }
    }
}
