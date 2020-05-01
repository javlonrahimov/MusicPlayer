package com.rahimovjavlon1212.musicplayer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.rahimovjavlon1212.musicplayer.cache.PlayerCache;
import com.rahimovjavlon1212.musicplayer.databases.PlayerDatabase;
import com.rahimovjavlon1212.musicplayer.fragments.BottomSheetFragment;
import com.rahimovjavlon1212.musicplayer.fragments.FragmentInNowPlaying;
import com.rahimovjavlon1212.musicplayer.models.MusicData;
import com.rahimovjavlon1212.musicplayer.player.MyPlayer;

public class NowPlayingActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton playlistButton;
    private ImageButton favButton;
    private ImageButton addButton;
    private ImageButton shuffleButton;
    private ImageButton prevButton;
    private ImageButton playPauseButton;
    private ImageButton nextButton;
    private ImageButton loopButton;
    private ImageView musicImage;
    private TextView musicTitle;
    private TextView musicArtist;
    private TextView currentTime;
    private TextView duration;
    private SeekBar seekBar;

    MusicData musicData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        MyPlayer.getPlayer().onChangeListenerNowPlaying = this::loadDataToViews;
        loadViews();
        loadDataToViews();
        loadListeners();
    }

    private void loadDataToViews() {
        musicData = PlayerCache.getPlayerCache().getCurrentMusic();
        musicImage.setImageBitmap(getRoundedCornerBitmap(PlayerCache.getPlayerCache().getCurrentBitmap()));
        musicTitle.setText(musicData.getTitle());
        musicArtist.setText(musicData.getArtist());
        int durationCount = MyPlayer.getPlayer().getDuration();
        duration.setText(createTimeLabel(durationCount));
        seekBar.setMax(durationCount);
        if (MyPlayer.getPlayer().isPlaying()) {
            playPauseButton.setImageResource(R.drawable.ic_pause_black_big);
        } else {
            playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_big);
        }
        if (PlayerCache.getPlayerCache().getShuffleMode()) {
            shuffleButton.setImageResource(R.drawable.ic_shuffle_black_active);
            shuffleButton.setBackgroundResource(R.drawable.button_background_active);
        } else {
            shuffleButton.setImageResource(R.drawable.ic_shuffle_black_24dp);
            shuffleButton.setBackgroundResource(R.drawable.button_background);
        }
        if (PlayerCache.getPlayerCache().getLoopMode()) {
            loopButton.setImageResource(R.drawable.ic_loop_black_active);
            loopButton.setBackgroundResource(R.drawable.button_background_active);
        } else {
            loopButton.setImageResource(R.drawable.ic_loop_black_24dp);
            loopButton.setBackgroundResource(R.drawable.button_background);
        }
        if (PlayerDatabase.getPlayerDatabase().isFav(musicData)) {
            favButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            favButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private void loadListeners() {
        playlistButton.setOnClickListener(this);
        playPauseButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        favButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        shuffleButton.setOnClickListener(this);
        loopButton.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MyPlayer.getPlayer().seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(() -> {
            while (MyPlayer.getPlayer() != null) {
                try {
                    Message msg = new Message();
                    msg.what = MyPlayer.getPlayer().getCurrentPosition();
                    handler.sendMessage(msg);
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            int currentPosition = msg.what;
            seekBar.setProgress(currentPosition);
            currentTime.setText(createTimeLabel(currentPosition));
        }
    };

    private String createTimeLabel(int time) {
        String timeLabel;
        String min = String.valueOf(time / 1000 / 60);
        String sec = String.valueOf(time / 1000 % 60);

        if (sec.length() < 2) sec = "0" + sec;
        if (min.length() < 2) min = "0" + min;
        timeLabel = min + " : " + sec;
        return timeLabel;
    }

    private void loadViews() {
        playlistButton = findViewById(R.id.playlistButtonNowPlayingButton);
        favButton = findViewById(R.id.favButtonNowPlayingActivity);
        addButton = findViewById(R.id.addButtonNowPlayingActivity);
        shuffleButton = findViewById(R.id.shuffleButtonNowPlayingActivity);
        prevButton = findViewById(R.id.prevButtonNowPlayingActivity);
        playPauseButton = findViewById(R.id.playPauseButtonNowPlayingActivity);
        nextButton = findViewById(R.id.nextButtonNowPlayingActivity);
        loopButton = findViewById(R.id.loopButtonNowPlayingActivity);

        currentTime = findViewById(R.id.currentTimeNowPlayingActivity);
        duration = findViewById(R.id.musicDurationNowPlayingActivity);
        seekBar = findViewById(R.id.seekBarNowPlayingActivity);

        musicImage = findViewById(R.id.musicImageNowPlayingActivity);
        musicArtist = findViewById(R.id.musicArtistNowPlayingActivity);
        musicTitle = findViewById(R.id.musicTitleNowPlayingActivity);

        musicTitle.setSelected(true);
        musicArtist.setSelected(true);

    }

    @Override
    public void onClick(View v) {
        if (v == playlistButton) {
            getSupportFragmentManager().beginTransaction().add(R.id.containerForFragmentNowPlayingActivity, new FragmentInNowPlaying(false)).addToBackStack("Playlist").commit();
        } else if (v == playPauseButton) {
            if (MyPlayer.getPlayer().isPlaying()) {
                MyPlayer.getPlayer().pause();
                playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_big);
            } else {
                MyPlayer.getPlayer().start(null, PlayerDatabase.getPlayerDatabase().getPlaylist(PlayerCache.getPlayerCache().getCurrentPlaylistName()));
                playPauseButton.setImageResource(R.drawable.ic_pause_black_big);
            }
        } else if (v == prevButton) {
            MyPlayer.getPlayer().prevMusic();
        } else if (v == nextButton) {
            MyPlayer.getPlayer().nextMusic(true);
        } else if (v == favButton) {
            if (!PlayerDatabase.getPlayerDatabase().isFav(musicData)) {
                PlayerDatabase.getPlayerDatabase().addFav(musicData);
                favButton.setImageResource(R.drawable.ic_favorite_black_24dp);
            } else {
                PlayerDatabase.getPlayerDatabase().minusFav(musicData);
                favButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }
        } else if (v == addButton) {
            getSupportFragmentManager().beginTransaction().add(R.id.containerForFragmentNowPlayingActivity, new FragmentInNowPlaying(true)).addToBackStack("Add").commit();
        } else if (v == shuffleButton) {
            if (!PlayerCache.getPlayerCache().getShuffleMode()) {
                PlayerCache.getPlayerCache().writeShuffleMode(true);
                shuffleButton.setImageResource(R.drawable.ic_shuffle_black_active);
                shuffleButton.setBackgroundResource(R.drawable.button_background_active);
            } else {
                PlayerCache.getPlayerCache().writeShuffleMode(false);
                shuffleButton.setImageResource(R.drawable.ic_shuffle_black_24dp);
                shuffleButton.setBackgroundResource(R.drawable.button_background);
            }
        } else if (v == loopButton) {
            if (!PlayerCache.getPlayerCache().getLoopMode()) {
                PlayerCache.getPlayerCache().writeLoopMode(true);
                loopButton.setImageResource(R.drawable.ic_loop_black_active);
                loopButton.setBackgroundResource(R.drawable.button_background_active);
            } else {
                PlayerCache.getPlayerCache().writeLoopMode(false);
                loopButton.setImageResource(R.drawable.ic_loop_black_24dp);
                loopButton.setBackgroundResource(R.drawable.button_background);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.info_menu) {
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(duration.getText().toString());
            bottomSheetFragment.show(getSupportFragmentManager(), "Info");
        }
        return super.onOptionsItemSelected(item);
    }


    private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(
                bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = -0xbdbdbe;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = 52f;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 125, 125, 125);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
