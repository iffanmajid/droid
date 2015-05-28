package com.gdilab.gnews.view;

/**
 * Created by masasdani on 1/10/15.
 */
public interface SwipeReadingListListener {

    public void onDelete(int position);
    public void onShare(int position);
    public void onDetail(int position);

}
