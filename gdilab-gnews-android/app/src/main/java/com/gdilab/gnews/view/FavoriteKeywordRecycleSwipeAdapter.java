package com.gdilab.gnews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdilab.gnews.R;
import com.gdilab.gnews.model.api.Keyword;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by masasdani on 1/7/15.
 */
public class FavoriteKeywordRecycleSwipeAdapter extends RecyclerView.Adapter<FavoriteKeywordRecycleSwipeAdapter.RecycleViewHolder> {

    private List<Keyword> list;
    private Context context;

    public FavoriteKeywordRecycleSwipeAdapter(Context context, List<Keyword> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.keyword_list_item, viewGroup, false);

        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder recycleViewHolder, final int i) {
        TypedArray colors = context.getResources().obtainTypedArray(R.array.gnews_random_background);
        int colorIndex = i % colors.length();
        int number = i+1;
        recycleViewHolder.textViewChannelName.setText("#"+number+" "+list.get(i).getName());
        recycleViewHolder.frontLayout.setBackgroundColor(colors.getColor(colorIndex, 0));
        Picasso.with(context)
                .load(list.get(i).getPreferedImage())
                .placeholder(android.R.color.transparent)
                .error(android.R.color.transparent)
                .into(recycleViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RecycleViewHolder extends RecyclerView.ViewHolder  {

        TextView textViewChannelName;
        View frontLayout;
        ImageView imageView;

        private RecycleViewHolder(View view) {
            super(view);
            textViewChannelName = (TextView) view.findViewById(R.id.channel_name);
            frontLayout = view.findViewById(R.id.front);
            imageView = (ImageView) view.findViewById(R.id.image);
        }

    }


}
