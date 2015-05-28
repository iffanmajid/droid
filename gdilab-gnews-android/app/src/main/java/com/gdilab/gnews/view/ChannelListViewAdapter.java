package com.gdilab.gnews.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gdilab.gnews.R;
import com.gdilab.gnews.model.api.Channel;

import java.util.List;

public class ChannelListViewAdapter extends ArrayAdapter<Channel> {

	Context context;

	public ChannelListViewAdapter(Context context, int resource, List<Channel> items) {
		super(context, resource, items);
		this.context = context;
	}

	private class ViewHolder {
        TextView name;
        TextView description;
    }
	
	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Channel rowItem = getItem(position);
        
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.channel_list_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.channel_name);
            holder.description = (TextView) convertView.findViewById(R.id.channel_desc);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        
        holder.name.setText(rowItem.getName());
        holder.description.setText(rowItem.getDescription());

        return convertView;
    }
}
