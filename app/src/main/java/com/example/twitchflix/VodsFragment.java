package com.example.twitchflix;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class VodsFragment extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_vods, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_vods);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // new Card Adapter with a onItemClickListener
        mAdapter = new CardAdapter(new CardAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(CardAdapter.MyViewHolder holder) {

                //String filmTitle = holder.title.getText().toString();
                //Toast.makeText(getContext(), filmTitle , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
                String film_url = "https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
                intent.putExtra("film_url", film_url);
                startActivity(intent);


            }
        });

        // adapter to the card based layout
        recyclerView.setAdapter(mAdapter);


        return rootView;
    }

}
