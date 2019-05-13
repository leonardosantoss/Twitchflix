package com.example.twitchflix;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView mVideoView;
    String film_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

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

        mVideoView.start();


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
