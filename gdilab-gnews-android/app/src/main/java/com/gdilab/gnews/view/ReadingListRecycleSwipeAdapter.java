package com.gdilab.gnews.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdilab.gnews.R;
import com.gdilab.gnews.model.api.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by masasdani on 1/7/15.
 */
public class ReadingListRecycleSwipeAdapter extends RecyclerView.Adapter<ReadingListRecycleSwipeAdapter.RecycleViewHolder> {

    private List<Article> list;
    private Context context;
    private SwipeReadingListListener listener;

    public ReadingListRecycleSwipeAdapter(Context context, List<Article> list, SwipeReadingListListener swipeListArticleListener) {
        this.context = context;
        this.list = list;
        this.listener = swipeListArticleListener;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.article_list_item_swipe_reading_list, viewGroup, false);

        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, final int i) {
        Article rowItem = list.get(i);
        holder.title.setText(rowItem.getTitle());
        holder.host.setText(rowItem.getHost());
        holder.content.setText(rowItem.getContent());
        holder.date.setText(rowItem.getDate());
        if(rowItem.getImage() != null){
            holder.image.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(rowItem.getImage())
                    .placeholder(android.R.color.transparent)
                    .error(android.R.color.transparent)
                    .into(holder.image);
        }else{
            holder.image.setVisibility(View.GONE);
        }
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDelete(i);
            }
        });

        holder.buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShare(i);
            }
        });

        holder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDetail(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RecycleViewHolder extends RecyclerView.ViewHolder  {

        ImageView image;
        TextView host;
        TextView title;
        TextView content;
        TextView date;
        ImageButton buttonDelete;
        ImageButton buttonShare;
        View contentView;

        private RecycleViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image_thumbnails);
            host = (TextView) view.findViewById(R.id.article_host);
            title = (TextView) view.findViewById(R.id.article_title);
            content = (TextView) view.findViewById(R.id.article_content);
            date = (TextView) view.findViewById(R.id.article_date);
            buttonDelete = (ImageButton) view.findViewById(R.id.button_delete);
            buttonShare = (ImageButton) view.findViewById(R.id.button_share);
            contentView = view.findViewById(R.id.content);
        }

    }


}
