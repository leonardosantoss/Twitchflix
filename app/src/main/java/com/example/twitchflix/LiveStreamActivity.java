package com.example.twitchflix;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LiveStreamActivity extends AppCompatActivity implements ConnectCheckerRtmp, SurfaceHolder.Callback {

    RtmpCamera1 rtmpCamera1;
    SurfaceView surfaceView;
    String username, url = null;
    Button button_live_stream, button_swap_camera, button_go_back_live;
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    String[] appPermissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream);

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

        url = "rtmp://35.222.56.211/live/" + username;

        surfaceView = findViewById(R.id.surfaceViewLiveStream);
        surfaceView.getHolder().addCallback(this);
        button_live_stream = findViewById(R.id.button_live_stream);
        button_swap_camera = findViewById(R.id.button_swap_camera);
        button_go_back_live = findViewById(R.id.button_go_back_live);

        // check if device has a camera
        if(checkCameraHardware(getApplicationContext())){
            // Check permission to use camera
            if(checkAndRequestPermissions()){
                rtmpCamera1 = new RtmpCamera1(surfaceView, this);

            }

        }
        else {
            Toast.makeText(this, "Device does not have a working camera!", Toast.LENGTH_SHORT).show();
            System.out.println("Device does not have a working camera!");
        }


        button_live_stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!rtmpCamera1.isStreaming()){
                    if(rtmpCamera1.isRecording() || rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()){
                        rtmpCamera1.startStream(url);
                        rtmpCamera1.startPreview();
                        activate_live activate = new activate_live(url, username);

                        try{
                            activate.execute(); // should treat response later

                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                    else{
                        System.out.println("Error preparing stream, This device cant do it");
                    }
                    button_live_stream.setText("Stop Stream");

                }else{
                    //delete_live delete = new delete_live(url);
                    button_live_stream.setText("Start Stream");
                    rtmpCamera1.stopStream();
                    /*
                    try{
                        delete.execute(); // should treat response later

                    } catch (Exception e){
                        e.printStackTrace();
                    }*/
                }
            }
        });

        button_swap_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rtmpCamera1.switchCamera();
                } catch (CameraOpenException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_go_back_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!rtmpCamera1.isStreaming()){
                    rtmpCamera1.stopStream();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(rtmpCamera1 != null){
           rtmpCamera1.startPreview();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(rtmpCamera1 != null){

            if (rtmpCamera1.isRecording()) {
                rtmpCamera1.stopRecord();
            }
            if (rtmpCamera1.isStreaming()) {
                rtmpCamera1.stopStream();
            }
            rtmpCamera1.stopPreview();
        }
    }

    @Override
    public void onConnectionSuccessRtmp() {
    }

    @Override
    public void onConnectionFailedRtmp(String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LiveStreamActivity.this, "Connection failed. ",
                        Toast.LENGTH_SHORT).show();
                if(rtmpCamera1 != null){
                  rtmpCamera1.stopStream();
                }

            }
        });
    }

    @Override
    public void onDisconnectRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LiveStreamActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                delete_live delete = new delete_live(url);
                try{
                    delete.execute(); // should treat response later

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onAuthErrorRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LiveStreamActivity.this, "Auth error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LiveStreamActivity.this, "Auth success", Toast.LENGTH_SHORT).show();
            }
        });
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



    public boolean checkAndRequestPermissions(){
        List<String> listPermissionsNeeded = new ArrayList<>();
        for(String perm : appPermissions){
            if(ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(perm);
            }
        }

        if(!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSIONS_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSIONS_REQUEST_CODE){
            HashMap<String, Integer> permissionsResult = new HashMap<>();
            int denyCount = 0;

            for(int i=0;i<grantResults.length;i++){
                if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                    permissionsResult.put(permissions[i], grantResults[i]);
                    denyCount++;
                }
            }

            if(denyCount == 0){
                  rtmpCamera1 = new RtmpCamera1(surfaceView, this);
                  rtmpCamera1.startPreview();
            }
            else{
                Toast.makeText(getApplicationContext(), "Need permissions to start streaming", Toast.LENGTH_SHORT);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }

    }



    public class activate_live extends AsyncTask<String, String, String> {

        String link;
        String name;

        public activate_live(String link, String name){
            this.name = name;
            this.link = link;
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("link", link + "")
                    .add("name", name + "")
                    .build();
            Request request = new Request.Builder()
                    .url("https://twitchflix-240014.appspot.com/webapi/activate_live")
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                System.out.println("Handle Response");
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class delete_live extends AsyncTask<String, String, String> {

        String link;

        public delete_live(String link){
            this.link = link;
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("link", link + "")
                    .build();
            Request request = new Request.Builder()
                    .url("https://twitchflix-240014.appspot.com/webapi/delete_live")
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                System.out.println("Handle Response");
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
