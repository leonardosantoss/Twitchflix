package com.example.twitchflix;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class LiveAdapter extends RecyclerView.Adapter <LiveAdapter.MyViewHolder> {


    public interface OnItemClickListener{
        void onItemClick(LiveAdapter.MyViewHolder holder);
    }

    private LiveAdapter.OnItemClickListener listener;
    private String[] mTitles;
    private int[] mLiveId;


    public LiveAdapter(OnItemClickListener listener, String[] mTitles, int[] mLiveId){
        this.listener = listener;
        this.mTitles = mTitles;
        this.mLiveId = mLiveId;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView image;
        public CardView cardView;
        int id;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.live_title);
            this.image = itemView.findViewById(R.id.image_live);
            this.cardView = itemView.findViewById(R.id.card_view_live);
        }


        // bind the data to its place inside the cardView and set the onClickListener
        public void bind(final LiveAdapter.MyViewHolder holder, String live_title, int live_id, final LiveAdapter.OnItemClickListener listener){
            title.setText(live_title);
            image.setImageResource(R.drawable.live_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder);
                }
            });
            id = live_id;
        }
    }

    @NonNull
    @Override
    public LiveAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_item_live, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LiveAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.bind(myViewHolder, mTitles[i], mLiveId[i] ,listener);
    }

    @Override
    public int getItemCount() {
        if(mTitles != null){
            return mTitles.length;
        }
        return 0;
    }


}
