package com.example.twitchflix;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LiveFragment extends Fragment {


    public String url = "http://192.168.1.74:8081/server/webapi/myresource";
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_live, container, false);


        textView = (TextView) rootView.findViewById(R.id.textViewLive);

        OkHttpHandler okHttpHandler = new OkHttpHandler();
        okHttpHandler.execute(url);


        return rootView;
    }

    public class OkHttpHandler extends AsyncTask<String,String,String>{

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
            textView.setText(s);

        }
    }

}
