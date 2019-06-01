package com.example.twitchflix;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class DisplayLivesFragment extends Fragment {

    public String url = "https://twitchflix-240014.appspot.com/webapi/get_lives";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] titles = null;
    int[] liveIds = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_display_live, container, false);

        DisplayLivesFragment.OkHttpHandler okHttpHandler = new DisplayLivesFragment.OkHttpHandler();
        okHttpHandler.execute(url);



        recyclerView = rootView.findViewById(R.id.recycler_view_live);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }


    // get all current livestreams
    public class OkHttpHandler extends AsyncTask<String,String,String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String...params) {
            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            Request request = builder.build();

            try{
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonarray = new JSONArray(s);
                titles = new String[jsonarray.length()];
                liveIds = new int[jsonarray.length()];
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    titles[i] = jsonobject.getString("name");
                    liveIds[i] = jsonobject.getInt("idlives");
                }
            }
            catch (JSONException e){
                Log.e("TwitchFlix", "unexpected JSON exception", e);
            }
            // new Card Adapter with a onItemClickListener
            mAdapter = new LiveAdapter(new LiveAdapter.OnItemClickListener(){
                @Override
                public void onItemClick(LiveAdapter.MyViewHolder holder) {

                    int liveId = holder.id;
                    String live_url;
                    postLiveId plid = new postLiveId(liveId);
                    try{
                        live_url = plid.execute("https://twitchflix-240014.appspot.com/webapi/get_live").get();
                        Toast.makeText(getContext(), live_url, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
                        intent.putExtra("film_url", live_url);
                        startActivity(intent);

                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }, titles, liveIds);

            // adapter to the card based layout
            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
            //textView.setText(s);

        }
    }

    //posts liveid to sv, gets matching url back
    public class postLiveId extends AsyncTask<String, String, String> {

        int liveId;

        public postLiveId(int liveId){
            this.liveId = liveId;
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("liveId", liveId + "")
                    .build();
            Request request = new Request.Builder()
                    .url("https://twitchflix-240014.appspot.com/webapi/get_live")
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
