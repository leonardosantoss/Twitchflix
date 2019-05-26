package com.example.twitchflix;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView mVideoView;
    String film_url;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        progressBar = findViewById(R.id.progressBarVideoPlayer);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);



        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                film_url= null;
            } else {
                film_url= extras.getString("film_url");
            }
        } else {
            film_url= (String) savedInstanceState.getSerializable("film_url");
        }

        mVideoView = findViewById(R.id.videoPlayer);

        Uri uri = Uri.parse(film_url);
        mVideoView.setVideoURI(uri);

        MediaController mController = new MediaController(this);
        mController.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(mController);


        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                progressBar.setVisibility(View.INVISIBLE);

            }
        });


    }


    private void releasePlayer() {
        mVideoView.stopPlayback();
    }



    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // do nothing, just override
    }

}
