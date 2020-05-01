package com.rahimovjavlon1212.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.musicplayer.adapters.SelectableAdapter;
import com.rahimovjavlon1212.musicplayer.databases.PlayerDatabase;
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;
import com.rahimovjavlon1212.musicplayer.player.MyPlayer;

import java.util.List;
import java.util.Objects;

public class SelectMusic extends AppCompatActivity {

    private String selectedMusics = "";
    private int selectedCount = 0;
    public static String SELECTED_TRACKS = "selected_tracks";
    public static String PLAYLIST_NAME = "playlist_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_music);

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
            Objects.requireNonNull(getSupportActionBar()).setTitle(selectedCount + getResources().getString(R.string.selected));
        }
        SelectableAdapter mAdapter = new SelectableAdapter(this, data);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSelectActivity);
        recyclerView.setAdapter(mAdapter);

        mAdapter.onItemClicked = (musicData, position) -> {
            if (musicData.isSelected()) {
                selectedCount++;
                Objects.requireNonNull(getSupportActionBar()).setTitle(selectedCount + getResources().getString(R.string.selected));
                selectedMusics += "#" + musicData.getMusicId();
            } else {
                selectedCount--;
                if (selectedCount == 0) {
                    Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.select_tracks));
                } else {
                    Objects.requireNonNull(getSupportActionBar()).setTitle(selectedCount + getResources().getString(R.string.selected));
                }
                selectedMusics = selectedMusics.replace("#" + musicData.getMusicId(), "");
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.done_menu) {
            Intent intent = new Intent();
            intent.putExtra(SELECTED_TRACKS, selectedMusics);
            setResult(SelectMusic.RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
