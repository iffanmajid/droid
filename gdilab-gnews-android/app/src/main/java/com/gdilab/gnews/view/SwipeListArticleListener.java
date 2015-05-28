package com.gdilab.gnews.view;

/**
 * Created by masasdani on 1/10/15.
 */
public interface SwipeListArticleListener {

    public void onFavorite(int position);
    public void onReadingList(int position);
    public void onShare(int position);
    public void onDetail(int position);
    public void onFollow(int position);

}
