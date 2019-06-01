package com.example.twitchflix;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

public class RtmpPlayerActivity extends AppCompatActivity {

    String live_url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                live_url = null;
            } else {
                live_url = extras.getString("live_url");
            }
        } else {
            live_url = (String) savedInstanceState.getSerializable("live_url");
        }


        setContentView(R.layout.activity_rtmp_player);
        //initiate Player
        //Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        //Create the player
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        PlayerView playerView = findViewById(R.id.rtmp_player);
        playerView.setPlayer(player);

        RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(rtmpDataSourceFactory)
                .createMediaSource(Uri.parse(live_url));

        // Prepare the player with the source.
        player.prepare(videoSource);
        //auto start playing
        player.setPlayWhenReady(true);

    }


}
