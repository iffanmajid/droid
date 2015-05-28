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
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.model.api.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 1/7/15.
 */
public class ArticleRecycleSwipeAdapter extends RecyclerView.Adapter<ArticleRecycleSwipeAdapter.RecycleViewHolder> {

    private List<Article> list;
    private Context context;
    private SwipeListArticleListener listener;
    private AppPreference appPreference;

    public ArticleRecycleSwipeAdapter(Context context, List<Article> list, SwipeListArticleListener swipeListArticleListener) {
        this.appPreference = Esperandro.getPreferences(AppPreference.class, context);
        this.context = context;
        this.list = list;
        this.listener = swipeListArticleListener;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.article_list_item_swipe_default, viewGroup, false);

        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, final int i) {
        Article rowItem = list.get(i);
        holder.title.setText(rowItem.getTitle());
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
        holder.buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShare(i);
            }
        });

        holder.buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFavorite(i);
            }
        });

        holder.buttonArchieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReadingList(i);
            }
        });

        holder.buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFollow(i);
            }
        });

        if(rowItem.getTwitterUsername() == null){
            holder.host.setText(rowItem.getHost());
            holder.shared.setVisibility(View.INVISIBLE);
            holder.buttonFollow.setVisibility(View.GONE);
        }else{
            holder.host.setText("@"+rowItem.getTwitterUsername());
            holder.shared.setVisibility(View.VISIBLE);
            holder.buttonFollow.setVisibility(View.VISIBLE);
        }
        holder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDetail(i);
            }
        });
        if(!appPreference.loggedIn()){
            holder.buttonArchieve.setVisibility(View.GONE);
            holder.buttonFavorite.setVisibility(View.GONE);
            holder.buttonFollow.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RecycleViewHolder extends RecyclerView.ViewHolder  {

        ImageView image;
        TextView host;
        TextView shared;
        TextView title;
        TextView content;
        TextView date;
        ImageButton buttonFavorite;
        ImageButton buttonArchieve;
        ImageButton buttonShare;
        ImageButton buttonFollow;
        View contentView;

        private RecycleViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image_thumbnails);
            host = (TextView) view.findViewById(R.id.article_host);
            shared = (TextView) view.findViewById(R.id.article_shared);
            title = (TextView) view.findViewById(R.id.article_title);
            content = (TextView) view.findViewById(R.id.article_content);
            date = (TextView) view.findViewById(R.id.article_date);
            buttonArchieve = (ImageButton) view.findViewById(R.id.button_reading_list);
            buttonFavorite = (ImageButton) view.findViewById(R.id.button_favorite);
            buttonShare = (ImageButton) view.findViewById(R.id.button_share);
            buttonFollow = (ImageButton) view.findViewById(R.id.button_follow);
            contentView = view.findViewById(R.id.content);
        }

    }


}
