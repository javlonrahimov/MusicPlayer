package com.rahimovjavlon1212.musicplayer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rahimovjavlon1212.musicplayer.adapters.MyPagerAdapter;
import com.rahimovjavlon1212.musicplayer.cache.PlayerCache;
import com.rahimovjavlon1212.musicplayer.databases.PlayerDatabase;
import com.rahimovjavlon1212.musicplayer.models.MusicData;

import java.io.IOException;
import java.util.List;

import static com.rahimovjavlon1212.musicplayer.player.MyPlayer.getPlayer;

public class LibraryActivity extends AppCompatActivity {

    private ImageView nowPlayingImage;
    private TextView nowPlayingTitle;
    private TextView nowPlayingArtist;
    private ImageButton prevButton;
    private ImageButton pausePlayButton;
    private ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        nowPlayingImage = findViewById(R.id.imageNowPlaying);
        nowPlayingTitle = findViewById(R.id.titleNowPlaying);
        nowPlayingArtist = findViewById(R.id.artistNowPlaying);
        prevButton = findViewById(R.id.prevCurrent);
        pausePlayButton = findViewById(R.id.pausePlayCurrent);
        nextButton = findViewById(R.id.nextCurrent);
        ImageButton searchButton = findViewById(R.id.searchButtonLibraryActivity);
        searchButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SearchActivity.class)));

        getPlayer().onChangeListenerLibraryActivity = () -> setCurrentMusic(PlayerCache.getPlayerCache().getCurrentMusic());
        doStuff();
    }

    public void doStuff() {
        List<MusicData> mList = getPlayer().getPlaylist();
        loadListeners(PlayerCache.getPlayerCache().getCurrentMusic());
        ViewPager viewPager = findViewById(R.id.viewPagerLibraryActivity);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getApplicationContext(), getSupportFragmentManager(), mList);
        viewPager.setAdapter(myPagerAdapter);
        final TabLayout tabLayout = findViewById(R.id.tabLayoutLibraryActivity);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {

                TextView tabTextView = new TextView(this);
                tab.setCustomView(tabTextView);

                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                tabTextView.setText(tab.getText());
                tabTextView.setTextColor(getResources().getColor(android.R.color.black));

                if (i == 0) {
                    tabTextView.setTextSize(20);
                }
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                int tabChildCount = vgTab.getChildCount();
                for (int i = 0; i < tabChildCount; i++) {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        ((TextView) tabViewChild).setTextSize(20);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                int tabChildCount = vgTab.getChildCount();
                for (int i = 0; i < tabChildCount; i++) {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        ((TextView) tabViewChild).setTextSize(14);
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setCurrentMusic(MusicData musicData) {
        nowPlayingImage.setImageBitmap(PlayerCache.getPlayerCache().getCurrentBitmap());
        nowPlayingArtist.setText(musicData.getArtist());
        nowPlayingArtist.setSelected(true);
        nowPlayingTitle.setText(musicData.getTitle());
        nowPlayingTitle.setSelected(true);
        if (getPlayer().isPlaying()) {
            pausePlayButton.setImageResource(R.drawable.ic_pause_black_24dp);
        } else {
            try {
                getPlayer().reset();
                getPlayer().setDataSource(musicData.getMusicPath());
                getPlayer().prepare();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, getResources().getString(R.string.cannot_play), Toast.LENGTH_SHORT).show();
                return;
            }
            pausePlayButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }

    private void loadListeners(MusicData musicData) {
        if (musicData.getMusicId().equals("null")) {
            musicData = getPlayer().getPlaylist().get(0);
        }
        setCurrentMusic(musicData);
        pausePlayButton.setOnClickListener(v -> {
            if (getPlayer().isPlaying()) {
                getPlayer().pause();
                pausePlayButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            } else {
                getPlayer().start(null, PlayerDatabase.getPlayerDatabase().getPlaylist(PlayerCache.getPlayerCache().getCurrentPlaylistName()));
                pausePlayButton.setImageResource(R.drawable.ic_pause_black_24dp);
            }
        });

        nextButton.setOnClickListener(v -> getPlayer().nextMusic(true));

        prevButton.setOnClickListener(v -> getPlayer().prevMusic());
        ConstraintLayout nowPlayingMusic = findViewById(R.id.nowPlayingMusic);
        nowPlayingMusic.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NowPlayingActivity.class)));
    }
}
