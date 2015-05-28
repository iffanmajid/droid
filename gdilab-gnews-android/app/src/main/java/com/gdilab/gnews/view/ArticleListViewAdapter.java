package com.gdilab.gnews.view;

import java.util.List;

import com.gdilab.gnews.R;
import com.gdilab.gnews.model.api.Article;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArticleListViewAdapter extends ArrayAdapter<Article> {

	Context context;
	
	public ArticleListViewAdapter(Context context, int resource, List<Article> items) {
		super(context, resource, items);
		this.context = context;
	}

	private class ViewHolder {
        ImageView image;
        TextView host;
        TextView title;
        TextView content;
        TextView date;
    }
	
	@SuppressLint("InflateParams") 
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Article rowItem = getItem(position);
        
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.article_list_item_swipe_default, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image_thumbnails);
            holder.host = (TextView) convertView.findViewById(R.id.article_host);
            holder.title = (TextView) convertView.findViewById(R.id.article_title);
            holder.content = (TextView) convertView.findViewById(R.id.article_content);
            holder.date = (TextView) convertView.findViewById(R.id.article_date);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        
        holder.title.setText(rowItem.getTitle());
        holder.host.setText(rowItem.getHost());
        holder.content.setText(rowItem.getContent());
        holder.date.setText(rowItem.getDate());

        if(rowItem.getImage() == null){
            holder.image.setVisibility(View.GONE);
        }else{
            Picasso.with(context)
                    .load(rowItem.getImage())
                    .placeholder(android.R.color.transparent)
                    .error(android.R.color.transparent)
                    .into(holder.image);
        }
        return convertView;
    }
}
