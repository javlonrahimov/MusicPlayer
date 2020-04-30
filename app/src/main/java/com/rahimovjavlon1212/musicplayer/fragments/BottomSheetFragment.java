package com.rahimovjavlon1212.musicplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.cache.PlayerCache;
import com.rahimovjavlon1212.musicplayer.models.MusicData;

import java.io.File;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private String duration;

    public BottomSheetFragment(String duration) {
        this.duration = duration;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        TextView musicName = view.findViewById(R.id.musicNameBottomSheet);
        TextView musicArtist = view.findViewById(R.id.musicArtistBottomSheet);
        TextView musicLength = view.findViewById(R.id.musicLengthBottomSheet);
        TextView musicFormat = view.findViewById(R.id.musicFormatBottomSheet);
        TextView musicSize = view.findViewById(R.id.musicSizeBottomSheet);
        TextView musicPath = view.findViewById(R.id.musicPathBottomSheet);

        MusicData musicData = PlayerCache.getPlayerCache().getCurrentMusic();
        File file = new File(musicData.getMusicPath());
        musicName.setText(musicData.getTitle());
        musicName.setSelected(true);
        musicArtist.setText(musicData.getArtist());
        musicArtist.setSelected(true);
        musicFormat.setText(musicData.getMusicPath().substring(musicData.getMusicPath().lastIndexOf(".")));
        musicFormat.setSelected(true);
        musicPath.setText(musicData.getMusicPath().replace("/storage/emulated/0", "/Internal storage").replace(musicData.getTitle(), "").replace(musicFormat.getText().toString(), ""));
        musicPath.setSelected(true);
        String size = String.valueOf(Float.parseFloat(String.valueOf(file.length())) / (1024 * 1024));
        musicSize.setText(size.substring(0, 4) + getResources().getString(R.string.mb));
        musicSize.setSelected(true);
        musicLength.setText(duration);
        musicLength.setSelected(true);
        return view;
    }
}
