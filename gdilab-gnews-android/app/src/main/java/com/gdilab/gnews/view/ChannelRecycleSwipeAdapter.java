package com.gdilab.gnews.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdilab.gnews.R;
import com.gdilab.gnews.model.api.Channel;

import java.util.List;

/**
 * Created by masasdani on 1/7/15.
 */
public class ChannelRecycleSwipeAdapter extends RecyclerView.Adapter<ChannelRecycleSwipeAdapter.RecycleViewHolder> {

    private List<Channel> list;
    private Context context;

    public ChannelRecycleSwipeAdapter(Context context, List<Channel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.channel_list_item, viewGroup, false);

        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder recycleViewHolder, int i) {
        recycleViewHolder.textViewChannelName.setText(list.get(i).getName());
        recycleViewHolder.textViewChannelDesc.setText(list.get(i).getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RecycleViewHolder extends RecyclerView.ViewHolder  {

        TextView textViewChannelName;
        TextView textViewChannelDesc;

        private RecycleViewHolder(View view) {
            super(view);
            textViewChannelName = (TextView) view.findViewById(R.id.channel_name);
            textViewChannelDesc = (TextView) view.findViewById(R.id.channel_desc);
        }

    }


}
