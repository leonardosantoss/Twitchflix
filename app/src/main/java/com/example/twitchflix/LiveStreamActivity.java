package com.example.twitchflix;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.widget.Toast;

import com.pedro.rtplibrary.rtmp.RtmpCamera1;

import net.ossrs.rtmp.ConnectCheckerRtmp;


public class LiveStreamActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    ConnectCheckerRtmp connectCheckerRtmp;
    String username, url = null;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_AUDIO_REQUEST_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live_stream);
        surfaceView = findViewById(R.id.surfaceViewLiveStream);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                username = null;
            } else {
                username = extras.getString("username");
            }
        } else {
            username = (String) savedInstanceState.getSerializable("username");
        }

        connectCheckerRtmp = new ConnectCheckerRtmp() {
            @Override
            public void onConnectionSuccessRtmp() {
                System.out.println("Connection Successfull");
            }

            @Override
            public void onConnectionFailedRtmp(String reason) {
                System.out.println("Connection Failed");
            }

            @Override
            public void onDisconnectRtmp() {
                System.out.println("Disconnect");
            }

            @Override
            public void onAuthErrorRtmp() {
                System.out.println("Auth Error");
            }

            @Override
            public void onAuthSuccessRtmp() {
                System.out.println("Auth Success");
            }
        };

        // check if device has a camera
        if(checkCameraHardware(getApplicationContext())){
            // Check permission to use camera
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                System.out.println("Check for permission Camera");
                //request permission
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }

            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
                System.out.println("Check for permission Camera");
                //request permission
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, MY_AUDIO_REQUEST_CODE);
            }

            //create builder
            RtmpCamera1 rtmpCamera1 = new RtmpCamera1(surfaceView, connectCheckerRtmp);
            //start stream
            if (rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
                url = "rtmp://35.222.56.211/live/" + username;
                System.out.println(url);
                rtmpCamera1.startStream(url);
            } else {
                System.out.println("This device cant init encoders, this could be for 2 reasons: The encoder selected doesnt support any configuration setted or your device hasnt a H264 or AAC encoder");
            }
            //stop stream
            //rtmpCamera1.stopStream();


        }
        else {
            System.out.println("Device does not have a working camera!");
        }




    }


    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }



}
