package com.example.twitchflix;

import android.content.Intent;
import android.net.Uri;
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

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VodsFragment extends Fragment {


    public String url = "http://192.168.1.74:8081/server/webapi/myresource";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] titles = null;
    int[] filmIds = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_vods, container, false);
        OkHttpHandler okHttpHandler = new OkHttpHandler();
        okHttpHandler.execute(url);


        recyclerView = rootView.findViewById(R.id.recycler_view_vods);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);




        return rootView;
    }


    public class OkHttpHandler extends AsyncTask<String,String,String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String...params) {
            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            Request request = builder.build();


            //.toString(): This returns your object in string format.
            // .string(): This returns your response.

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
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    titles[i] = jsonobject.getString("name");
                    filmIds[i] = jsonobject.getInt("idmovies");
                }
            }
            catch (JSONException e){
                Log.e("TwitchFlix", "unexpected JSON exception", e);
            }
            // new Card Adapter with a onItemClickListener
            mAdapter = new CardAdapter(new CardAdapter.OnItemClickListener(){
                @Override
                public void onItemClick(CardAdapter.MyViewHolder holder) {

                    String filmTitle = holder.title.getText().toString();
                    Toast.makeText(getContext(), filmTitle , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
                    String film_url = "https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
                    intent.putExtra("film_url", film_url);
                    startActivity(intent);


                }
            }, titles, filmIds);

            // adapter to the card based layout
            recyclerView.setAdapter(mAdapter);
            //textView.setText(s);

        }
    }

}
