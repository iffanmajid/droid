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
public class KeywordRecycleSwipeAdapter extends RecyclerView.Adapter<KeywordRecycleSwipeAdapter.RecycleViewHolder> {

    private List<Keyword> list;
    private Context context;
    private SwipeListKeywordListener listener;

    public KeywordRecycleSwipeAdapter(Context context, List<Keyword> list, SwipeListKeywordListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
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
        recycleViewHolder.textViewChannelName.setText(list.get(i).getName());
        recycleViewHolder.frontLayout.setBackgroundColor(colors.getColor(colorIndex, 0));
        Picasso.with(context)
                .load(list.get(i).getPreferedImage())
                .placeholder(android.R.color.transparent)
                .error(android.R.color.transparent)
                .into(recycleViewHolder.imageView);

        recycleViewHolder.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelSwipe(i);
            }
        });

        recycleViewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirmDelete(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RecycleViewHolder extends RecyclerView.ViewHolder  {

        TextView textViewChannelName;
        Button buttonDelete;
        Button buttonCancel;
        View frontLayout;
        ImageView imageView;

        private RecycleViewHolder(View view) {
            super(view);
            textViewChannelName = (TextView) view.findViewById(R.id.channel_name);
            buttonDelete = (Button) view.findViewById(R.id.button_yes);
            buttonCancel = (Button) view.findViewById(R.id.button_no);
            frontLayout = view.findViewById(R.id.front);
            imageView = (ImageView) view.findViewById(R.id.image);
        }

    }


}
