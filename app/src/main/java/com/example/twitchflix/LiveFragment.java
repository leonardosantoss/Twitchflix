package com.example.twitchflix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LiveFragment extends Fragment {


    String logged_user_username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_live, container, false);
        final SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        logged_user_username = pref.getString("Username","default");


        AppCompatTextView start_livestream = rootView.findViewById(R.id.start_livestream);
        start_livestream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logged_user_username.equals("default")){
                    Toast.makeText(getContext(), "Can only start a livestream if logged in!", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(getContext(), LiveStreamActivity.class);
                    intent.putExtra("username", logged_user_username);
                    startActivity(intent);
                }
            }
        });

        AppCompatTextView watch_livestream = rootView.findViewById(R.id.watch_livestream);
        watch_livestream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new DisplayLivesFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragments_container, fragment)
                        .commit();
            }
        });

        return rootView;
    }

}
