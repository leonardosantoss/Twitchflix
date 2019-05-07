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

import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.protocol.HttpContext;

public class LiveFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_live, container, false);


        new LongRunningGetIO(rootView).execute();

        return rootView;
    }

    private class LongRunningGetIO extends AsyncTask<Void,Void,String> {

        View mView;
        public LongRunningGetIO(View view){
            this.mView = view;
        }

        protected String getContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();


            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n>0) {
                byte[] b = new byte[4096];
                n =  in.read(b);


                if (n>0) out.append(new String(b, 0, n));
            }
            
            return out.toString();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String text =  null;
            try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()){

                HttpContext localContext = new BasicHttpContext();
                HttpGet httpGet = new HttpGet("http://192.168.1.74:8081/server/webapi/myresource");

                try{
                    HttpResponse response =  httpClient.execute(httpGet, localContext);
                    HttpEntity entity = response.getEntity();
                    text = getContentFromEntity(entity);
                    System.out.println(text);

                }catch (Exception e){
                    System.out.println("Error " + e.getLocalizedMessage());
                }

            } catch(IOException e){
                System.out.println("Error " + e.getLocalizedMessage());
            }


            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null){
                TextView live = mView.findViewById(R.id.textViewLive);
                live.setText(s);
            }
        }
    }
}
