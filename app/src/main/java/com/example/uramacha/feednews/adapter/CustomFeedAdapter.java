package com.example.uramacha.feednews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uramacha.feednews.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * CustomAdapter class to populate the item into recyclerview
 */

public class CustomFeedAdapter extends RecyclerView.Adapter<CustomFeedAdapter.MyViewHolder> {

    private final Context context;

    private final ArrayList<FeedItem> allFeedItems;

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

        String title = allFeedItems.get(position).getTitle();
        String desc = allFeedItems.get(position).getDescription();
        String imageRef = allFeedItems.get(position).getImagehref();

        if (title.equalsIgnoreCase("null"))
            holder.textTitle.setText("");
        else
            holder.textTitle.setText(title);

        if (desc.equalsIgnoreCase("null"))
            holder.textPlaceDesc.setText("");
        else
            holder.textPlaceDesc.setText(desc);


        Glide.with(context)
                .load(imageRef)
                .into(holder.image);

    }

    @Override
    public CustomFeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_item_row, parent, false);
        return new MyViewHolder(view);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textTitle)
        TextView textTitle;
        @BindView(R.id.descAndPlace)
        TextView textPlaceDesc;
        @BindView(R.id.image)
        ImageView image;

        MyViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }

    }


}
