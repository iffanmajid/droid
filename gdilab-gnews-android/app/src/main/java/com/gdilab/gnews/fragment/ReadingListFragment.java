package com.gdilab.gnews.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdilab.gnews.ArticleDetail;
import com.gdilab.gnews.R;
import com.gdilab.gnews.config.AppConfig;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.model.api.Article;
import com.gdilab.gnews.service.RestService;
import com.gdilab.gnews.service.RestServiceImpl;
import com.gdilab.gnews.view.ArticleRecycleSwipeAdapter;
import com.gdilab.gnews.view.ReadingListRecycleSwipeAdapter;
import com.gdilab.gnews.view.RecyclerItemClickListener;
import com.gdilab.gnews.view.SwipeReadingListListener;
import com.gdilab.gnewsrecycleview.BaseSwipeListViewListener;
import com.gdilab.gnewsrecycleview.SwipeListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 12/21/14.
 */
public class ReadingListFragment extends DefaultFragment {

    List<Article> listItems;

    @InjectView(R.id.list)
    SwipeListView listview;

    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;

    @InjectView(R.id.crossfade_loading)
    RelativeLayout crossfadeLoading;

    private ReadingListRecycleSwipeAdapter listViewAdapter;
    private AppPreference appPreference;
    private LinearLayoutManager layoutManager;

    private InitDataTask initDataTask;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        appPreference = Esperandro.getPreferences(AppPreference.class, getActivity());

        View view = inflater.inflate(R.layout.article_list, container, false);
        ButterKnife.inject(this, view);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        listItems = new ArrayList<>();
        listViewAdapter = new ReadingListRecycleSwipeAdapter(getActivity(), listItems, new SwipeReadingListListener() {
            @Override
            public void onDelete(int position) {
                final Article article = listItems.get(position);

                listItems.remove(position);
                listViewAdapter.notifyDataSetChanged();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RestService restService = new RestServiceImpl();
                        restService.deleteArchive(article.getUrl(), appPreference.accessToken());
                    }
                }).start();
            }

            @Override
            public void onShare(int position) {
                share(position);
            }

            @Override
            public void onDetail(int position) {
                changeView(position);
            }

        });

        layoutManager = new LinearLayoutManager(getActivity());
        listview.setLayoutManager(layoutManager);

        View header = getActivity().getWindow().getDecorView().findViewById(R.id.toolbar);
        View rightItem = header.findViewById(R.id.right_item);
        rightItem.setVisibility(View.VISIBLE);
        TextView fragmentTitle = (TextView) header.findViewById(R.id.fragment_title);
        ImageView fragmentLogo = (ImageView) header.findViewById(R.id.fragment_logo);
        fragmentTitle.setText(R.string.reading_list);
        fragmentLogo.setImageResource(R.drawable.icon_mark);

        listview.setAdapter(listViewAdapter);
        listview.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = listview.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {

                    new LoadDataTask().execute();
                    loading = true;
                }
            }
        });

//        listview.addOnItemTouchListener(
//                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        changeView(position);
//                    }
//                })
//        );

        listview.setVisibility(View.GONE);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                listItems = new ArrayList<Article>();
                new InitDataTask().execute();
            }

        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDataTask = new InitDataTask();
        initDataTask.execute();
    }

    private class LoadDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            updateData(listItems.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            listViewAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled() {

        }
    }

    private class InitDataTask extends AsyncTask<Void, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                if (isCancelled()) {
                    return false;
                }
                updateData(listItems.size());
                return true;
            }catch (Exception e){
                onDestroy();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                finishLoading(listview, crossfadeLoading);
                listViewAdapter.notifyDataSetChanged();
                if(refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
                super.onPostExecute(result);
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

    private void updateData(int size) {
        RestService restService = new RestServiceImpl();
        List<Article> list = restService.arcivedArticles(
                appPreference.accessToken(),
                AppConfig.NUM_ARTICLE_PER_REQUEST, size);
        listItems.addAll(list);
        Log.d(getClass().getName(), "updated");
    }

    private void changeView(int position) {
        Article article = listItems.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("id", article.getId());
        bundle.putString("url", article.getUrl());
        bundle.putString("title", article.getTitle());

        Intent intent = new Intent(getActivity(), ArticleDetail.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        initDataTask.cancel(true);
        ButterKnife.reset(this);
    }

    public void share(int position){
        Article article = listItems.get(position);
        progressDialog.show();
        new ShareUrl().execute(article.getUrl(), article.getTitle());
    }

    private class ShareUrl extends AsyncTask<String, String, String>{

        Intent intent;

        @Override
        protected String doInBackground(String... params) {
            RestService restService = new RestServiceImpl();
            String shortenUrl = restService.shortenUrl(params[0]);
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, params[1] + " "+ shortenUrl + " via @gnewsapp");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "I've read article from gnews app!");
            return shortenUrl;
        }

        @Override
        protected void onPostExecute(String aBoolean) {
            progressDialog.dismiss();
            startActivity(Intent.createChooser(intent, "Share"));
        }
    }

}
