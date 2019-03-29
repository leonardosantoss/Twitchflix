package com.example.twitchflix;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CardAdapter extends RecyclerView.Adapter <CardAdapter.MyViewHolder>{

    // Provide a reference to the views for each data item
    private String[] mFilm = {"Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9" };
    private ImageView mImage;



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.film_title);
            this.image = (ImageView) itemView.findViewById(R.id.film_image);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_vods_layout, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.title.setText(mFilm[i]);
        myViewHolder.image.setImageResource(R.drawable.vod_icon);

    }


    @Override
    public int getItemCount() {
        return mFilm.length;
    }
}
