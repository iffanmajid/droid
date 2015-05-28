package com.gdilab.gnews.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdilab.gnews.R;
import com.gdilab.gnews.model.api.Channel;

import java.util.List;

/**
 * Created by masasdani on 11/26/14.
 */
public class ChannelRecycleAdapter extends RecyclerView.Adapter<ChannelRecycleAdapter.RecycleViewHolder> {

    private List<Channel> list;

    public ChannelRecycleAdapter(List<Channel> list) {
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

    public void addData(List<Channel> channels){
        list.addAll(channels);
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
