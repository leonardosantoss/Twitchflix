package com.example.twitchflix;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

public class CardAdapter extends RecyclerView.Adapter <CardAdapter.MyViewHolder>{

    // recycler view has no default onclicklistener
    // create interface to implement own onClick to each cardView
    public interface OnItemClickListener{
         void onItemClick(CardView cardView);
    }

    // Provide a reference to the views for each data item
    private String[] mFilm = {"Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9" };
    private OnItemClickListener listener;

    public CardAdapter(OnItemClickListener listener){
        this.listener = listener;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView image;
        public CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.film_title);
            this.image = (ImageView) itemView.findViewById(R.id.film_image);
            this.cardView = itemView.findViewById(R.id.card_view);
        }


         // bind the data to its place inside the cardView and set the onClickListener
         public void bind(String film_title, int film_image, final CardView cardview, final OnItemClickListener listener){
             title.setText(film_title);
             image.setImageResource(film_image);

             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     listener.onItemClick(cardview);
                 }
             });
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
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        // bind takes the data for each cardview, a reference to the cardview and the listener
        myViewHolder.bind(mFilm[i], R.drawable.vod_icon, myViewHolder.cardView, listener);

    }


    @Override
    public int getItemCount() {
        return mFilm.length;
    }


}
