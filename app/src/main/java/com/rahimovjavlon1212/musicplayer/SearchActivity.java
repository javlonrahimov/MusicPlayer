package com.rahimovjavlon1212.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.musicplayer.adapters.LibraryAdapter;
import com.rahimovjavlon1212.musicplayer.cache.PlayerCache;
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;
import com.rahimovjavlon1212.musicplayer.player.MyPlayer;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    SearchView searchView;
    private LibraryAdapter mAdapter;
    private List<MusicData> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchViewSearchActivity);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.setData(getResults(newText));
                return true;
            }
        });

        mList = MyPlayer.getPlayer().getPlaylist();

        mAdapter = new LibraryAdapter(this, new ArrayList<>());
        mAdapter.setFocus(PlayerCache.getPlayerCache().getCurrentMusic().getMusicId());
        mAdapter.onItemClicked = musicData -> {
            MyPlayer.getPlayer().start(musicData, new PlaylistData(-1, "", ""));
            startActivity(new Intent(getApplicationContext(), NowPlayingActivity.class));
            finish();
        };
        RecyclerView mRecyclerView = findViewById(R.id.recyclerViewSearchActivity);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<MusicData> getResults(String searchText) {
        List<MusicData> resultList = new ArrayList<>();

        if (!searchText.isEmpty())
            for (int i = 0; i < mList.size(); i++) {
                MusicData musicData = mList.get(i);
                if (musicData.getTitle().toLowerCase().contains(searchText.toLowerCase()) || musicData.getArtist().toLowerCase().contains(searchText.toLowerCase())) {
                    resultList.add(musicData);
                }
            }
        return resultList;
    }
}
