package com.example.twitchflix;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VodsFragment extends Fragment {


    public String url = "https://twitchflix-240014.appspot.com/webapi/get_movies";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] titles = null;
    int[] filmIds = null;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_vods, container, false);
        progressBar = rootView.findViewById(R.id.progressBarVods);
        OkHttpHandler okHttpHandler = new OkHttpHandler();
        okHttpHandler.execute(url);


        recyclerView = rootView.findViewById(R.id.recycler_view_vods);
        layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }


    public class OkHttpHandler extends AsyncTask<String,String,String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        }

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
            progressBar.setVisibility(View.GONE);
            try {
                JSONArray jsonarray = new JSONArray(s);
                titles = new String[jsonarray.length()];
                filmIds = new int[jsonarray.length()];
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

                    int filmId = holder.id;
                    String film_url;
                    //Toast.makeText(getContext(), filmTitle + "" , Toast.LENGTH_LONG).show();
                    postMovieId pmid = new postMovieId(filmId);
                    try{
                        film_url = pmid.execute("https://twitchflix-240014.appspot.com/webapi/get_movie").get();
                        Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
                        intent.putExtra("film_url", film_url);
                        startActivity(intent);

                    } catch (Exception e){
                        e.printStackTrace();
                    }




                }
            }, titles, filmIds);

            // adapter to the card based layout
            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
            //textView.setText(s);

        }
    }

    public class postMovieId extends AsyncTask<String, String, String> {

        int filmId;

        public postMovieId(int filmId){
            this.filmId = filmId;
        }


        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("filmId", filmId + "")
                    .build();
            Request request = new Request.Builder()
                    .url("https://twitchflix-240014.appspot.com/webapi/get_movie")
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
