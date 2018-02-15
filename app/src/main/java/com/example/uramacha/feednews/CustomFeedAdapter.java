package com.example.uramacha.feednews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * CustomAdapter to populate the item into recyclerview
 */

public class CustomFeedAdapter extends RecyclerView.Adapter<CustomFeedAdapter.MyViewHolder> {

    private final Context context;

    private final ArrayList<FeedItem> allFeedItems;

    private final String TAG = getClass().getSimpleName();

    public CustomFeedAdapter(Context context, ArrayList<FeedItem> allFeedItems) {
        this.context = context;
        this.allFeedItems = allFeedItems;
    }

    @Override
    public int getItemCount() {
        return allFeedItems.size();
    }


    @Override
    public void onBindViewHolder(CustomFeedAdapter.MyViewHolder holder, int position) {

        holder.textTitle.setText(allFeedItems.get(position).getTitle());
        holder.textPlaceDesc.setText(allFeedItems.get(position).getDescription());
        Glide.with(context)
                .load(allFeedItems.get(position).getImagehref())
                .into(holder.image);
        Log.d(TAG, "onBindViewHolder " + allFeedItems.get(position).getImagehref());
    }

    @Override
    public CustomFeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_item_row, parent, false);
        return new MyViewHolder(view);
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView textTitle;
        final TextView textPlaceDesc;
        final ImageView image;

        MyViewHolder(View view) {
            super(view);
            textTitle = (TextView) view.findViewById(R.id.textTitle);
            textPlaceDesc = (TextView) view.findViewById(R.id.descAndPlace);
            image = (ImageView) view.findViewById(R.id.image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }


}
