package com.rahimovjavlon1212.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rahimovjavlon1212.musicplayer.adapters.MyPagerAdapter;
import com.rahimovjavlon1212.musicplayer.cache.CurrentMusicCache;
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.singeltons.MyPlayer;

import java.io.IOException;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 9;
    private List<MusicData> mList;
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

        MyPlayer.getPlayer().onChangeListenerLibraryActivity = new MyPlayer.OnChangeListenerLibraryActivity() {
            @Override
            public void onChange() {
                int index = MyPlayer.getPlayer().getCurrentIndex();
                setCurrentMusic(mList.get(index));
            }
        };

        if (ContextCompat.checkSelfPermission(LibraryActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LibraryActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(LibraryActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(LibraryActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST);
            }
        } else {
            doStuff();
        }
    }

    public void doStuff() {
        mList = MyPlayer.getPlayer().getPlaylist();
        loadListeners(CurrentMusicCache.getCurrentMusicCache().getCurrentMusic());
        ViewPager viewPager = findViewById(R.id.viewPagerLibraryActivity);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mList);
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
                    tabTextView.setTextSize(22);
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
                        ((TextView) tabViewChild).setTextSize(22);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(LibraryActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    doStuff();
                }
            } else {
                Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void setCurrentMusic(MusicData musicData) {
        nowPlayingImage.setImageResource(musicData.getImagePath());
        nowPlayingArtist.setText(musicData.getArtist());
        nowPlayingTitle.setText(musicData.getTitle());
        if (MyPlayer.getPlayer().isPlaying()) {
            pausePlayButton.setImageResource(R.drawable.ic_pause_black_24dp);
        } else {
            try {
                MyPlayer.getPlayer().setDataSource(musicData.getMusicPath());
                MyPlayer.getPlayer().prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pausePlayButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }

    private void loadListeners(MusicData musicData) {
        setCurrentMusic(musicData);
        pausePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyPlayer.getPlayer().isPlaying()) {
                    MyPlayer.getPlayer().pause();
                    pausePlayButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } else {
                    MyPlayer.getPlayer().start();
                    pausePlayButton.setImageResource(R.drawable.ic_pause_black_24dp);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPlayer.getPlayer().nextMusic();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPlayer.getPlayer().prevMusic();
            }
        });
        ConstraintLayout nowPlayingMusic = findViewById(R.id.nowPlayingMusic);
        nowPlayingMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NowPlayingActivity.class));
            }
        });
    }
}
