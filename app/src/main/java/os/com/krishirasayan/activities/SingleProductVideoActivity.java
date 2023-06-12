package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.e;
import static os.com.krishirasayan.consts.Helper.i;
import static os.com.krishirasayan.consts.Helper.w;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import os.com.krishirasayan.R;

public class SingleProductVideoActivity extends AppCompatActivity {

    Context context = this;
    String YOUTUBE_URL;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_video);
        bindToolbar(context, R.string.video_details);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        if (getIntent().hasExtra("youtube_link")) {
            YOUTUBE_URL =  getIntent().getStringExtra("youtube_link");
            w(YOUTUBE_URL);
            i(YOUTUBE_URL);
            getLifecycle().addObserver(youTubePlayerView);

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(YOUTUBE_URL, 0);
                    youTubePlayerView.toggleFullScreen();
                }
            });
        } else {
            e("No Youtube link or video ");
            onBackPressed();
        }
    }


}