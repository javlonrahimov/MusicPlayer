package com.rahimovjavlon1212.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.musicplayer.adapters.SelectableAdapter;
import com.rahimovjavlon1212.musicplayer.databases.PlayerDatabase;
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;
import com.rahimovjavlon1212.musicplayer.player.MyPlayer;

import java.util.List;

public class SelectMusic extends AppCompatActivity {

    private SelectableAdapter mAdapter;
    private String selectedMusics = "";
    private TextView selectedTrackCount;
    private Button done;
    private int selectedCount = 0;
    public static String SELECTED_TRACKS = "selected_tracks";
    public static String PLAYLIST_NAME = "playlist_name";
    private boolean hasAllChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_music);

        selectedTrackCount = findViewById(R.id.selectedTrackCount);
        done = findViewById(R.id.doneButtonSelectMusicActivity);

        List<MusicData> data = MyPlayer.getPlayer().getPlaylist();
        PlaylistData playlist =
                PlayerDatabase.getPlayerDatabase().getPlaylist(getIntent().getStringExtra(PLAYLIST_NAME));
        for (int i = 0; i < data.size(); i++) {
            if (playlist.getData().contains(data.get(i).getMusicId())) {
                data.get(i).setSelected(true);
            } else {
                data.get(i).setSelected(false);
            }
        }


        selectedMusics = playlist.getData();
        selectedCount = selectedMusics.split("#").length - 1;
        if (selectedCount != 0) {
            selectedTrackCount.setText(selectedCount + getResources().getString(R.string.selected));
            done.setTextColor(getResources().getColor(android.R.color.black));
        }
        hasAllChecked = data.size() == selectedCount;
        if (hasAllChecked) {
            ((ImageView) findViewById(R.id.selectAllImageSelectActivity)).setImageResource(R.drawable.ic_check_circle_black_24dp);
            done.setTextColor(getResources().getColor(android.R.color.black));
        }
        mAdapter = new SelectableAdapter(this, data);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSelectActivity);
        recyclerView.setAdapter(mAdapter);

        mAdapter.onItemClicked = (musicData, position) -> {
            if (musicData.isSelected()) {
                selectedCount++;
                selectedTrackCount.setText(selectedCount + getResources().getString(R.string.selected));
                done.setTextColor(getResources().getColor(android.R.color.black));
                selectedMusics += "#" + musicData.getMusicId();
            } else {
                selectedCount--;
                if (selectedCount == 0) {
                    selectedTrackCount.setText(getResources().getString(R.string.select_tracks));
                    done.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    selectedTrackCount.setText(selectedCount + getResources().getString(R.string.selected));
                }
                selectedMusics = selectedMusics.replace("#" + musicData.getMusicId(), "");
            }
            hasAllChecked = data.size() == selectedCount;
            if (hasAllChecked) {
                ((ImageView) findViewById(R.id.selectAllImageSelectActivity)).setImageResource(R.drawable.ic_check_circle_black_24dp);
            }
        };

        done.setOnClickListener(v -> {
            if (selectedCount != 0) {
                Intent intent = new Intent();
                intent.putExtra(SELECTED_TRACKS, selectedMusics);
                setResult(SelectMusic.RESULT_OK, intent);
                finish();
            }
        });

    }

    public void onSelectAllPressed(View view) {
        List<MusicData> data = MyPlayer.getPlayer().getPlaylist();

        if (hasAllChecked) {
            selectedMusics = "";
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelected(false);
            }
            selectedCount = 0;
            hasAllChecked = false;
        } else {
            selectedMusics = "";
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelected(true);
                selectedMusics += ("#" + data.get(i).getMusicId());
            }
            hasAllChecked = true;
            selectedCount = data.size();
        }
        selectedTrackCount.setText(selectedCount + getResources().getString(R.string.selected));
        mAdapter.setData(data);
        hasAllChecked = data.size() == selectedCount;
        if (hasAllChecked) {
            ((ImageView) findViewById(R.id.selectAllImageSelectActivity)).setImageResource(R.drawable.ic_check_circle_black_24dp);
            done.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            ((ImageView) findViewById(R.id.selectAllImageSelectActivity)).setImageResource(R.drawable.ic_panorama_fish_eye_black_24dp);
            done.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }
}
