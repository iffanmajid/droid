package com.gdilab.gnews.fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdilab.gnews.ArticleDetail;
import com.gdilab.gnews.R;
import com.gdilab.gnews.config.AppConfig;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.model.api.ActionKeywordForm;
import com.gdilab.gnews.model.api.Article;
import com.gdilab.gnews.model.api.DeleteFilterForm;
import com.gdilab.gnews.model.api.FilterKeywordForm;
import com.gdilab.gnews.model.api.Keyword;
import com.gdilab.gnews.model.api.UserFollowForm;
import com.gdilab.gnews.service.RestService;
import com.gdilab.gnews.service.RestServiceImpl;
import com.gdilab.gnews.view.ArticleRecycleSwipeAdapter;
import com.gdilab.gnews.view.SwipeListArticleListener;
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
public class ArticleListFragment extends DefaultFragment {

    List<Article> listItems;

    @InjectView(R.id.list)
    SwipeListView listview;

    @InjectView(R.id.crossfade_loading)
    RelativeLayout crossfadeLoading;

    @InjectView(R.id.channel_name)
    TextView channelNameTextView;

    @InjectView(R.id.remove_filter)
    Button buttonRemoveFilter;

    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;

    private ArticleRecycleSwipeAdapter listViewAdapter;
    private AppPreference appPreference;
    private LinearLayoutManager layoutManager;
    private ProgressDialog progressDialog;
    private SearchView searchView;

    private Long channelId;
    private String channelName;
    private String filterString;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private InitDataTask initDataTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        appPreference = Esperandro.getPreferences(AppPreference.class, getActivity());

        View view = inflater.inflate(R.layout.article_list_header, container, false);
        ButterKnife.inject(this, view);

        setHasOptionsMenu(true);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        View header = getActivity().getWindow().getDecorView().findViewById(R.id.toolbar);
        View rightItem = header.findViewById(R.id.right_item);
        rightItem.setVisibility(View.GONE);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                initListView();
                new InitDataTask().execute();
            }

        });

        initListView();

        Bundle bundle = getArguments();
        channelId = bundle.getLong("id");
        channelName = bundle.getString("name");

        channelNameTextView.setText(channelName);
        buttonRemoveFilter.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new LoadFilter().execute();
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
        List<Article> list = restService.recentArticle(channelId,
                appPreference.accessToken(),
                appPreference.country(),
                filterString,
                AppConfig.NUM_ARTICLE_PER_REQUEST,
                size);
        listItems.addAll(list);
    }

    private void changeView(int position) {
        Article article = listItems.get(position);
        Bundle bundle = new Bundle();
        bundle.putLong("keywordId", channelId);
        bundle.putString("id", article.getId());
        bundle.putString("url", article.getUrl());
        bundle.putString("title", article.getTitle());

        Intent intent = new Intent(getActivity(), ArticleDetail.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.jump_to_top)
    public void jumpToTop(){
        Log.d("article list", "jump");
        layoutManager.smoothScrollToPosition(listview, null, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        initDataTask.cancel(true);
        ButterKnife.reset(this);
    }

    public void favorite(int position){
        Article article = listItems.get(position);
        RestService restService = new RestServiceImpl();
        try{
            restService.addtoFavorite(article.getUrl(), appPreference.accessToken());
            restService.addtoFavoriteKeyword(new ActionKeywordForm(appPreference.accessToken(), channelId));
            Toast.makeText(getActivity(), getResources().getText(R.string.popular_success), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.d(getClass().getName(), e.getMessage());
        }
    }

    public void archieve(int position){
        Article article = listItems.get(position);
        RestService restService = new RestServiceImpl();
        try{
            restService.addtoarchive(article.getUrl(), appPreference.accessToken());
            Toast.makeText(getActivity(), getResources().getText(R.string.readinglist_success), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.d(getClass().getName(), e.getMessage());
        }
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

    private class SetFilter extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            FilterKeywordForm form = new FilterKeywordForm();
            form.setAccessToken(appPreference.accessToken());
            form.setKeywordId(channelId);
            form.setFilter(params[0]);
            RestService restService = new RestServiceImpl();
            restService.addFilter(form);
            return params[0];
        }

        @Override
        protected void onPostExecute(String string) {
            if(string != null){
                filterString = string;
                channelNameTextView.setText(channelName+ " :: "+ string);
                buttonRemoveFilter.setVisibility(View.VISIBLE);
            }else{
                buttonRemoveFilter.setVisibility(View.GONE);
            }
        }
    }

    private class RemoveFilter extends AsyncTask<String, Boolean, Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            DeleteFilterForm form = new DeleteFilterForm();
            form.setAccessToken(appPreference.accessToken());
            form.setKeywordId(channelId);
            RestService restService = new RestServiceImpl();
            restService.removeFilter(form);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            if(bool){
                filterString = null;
                channelNameTextView.setText(channelName);
                buttonRemoveFilter.setVisibility(View.GONE);
            }
        }
    }

    private class LoadFilter extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            RestService restService = new RestServiceImpl();
            Keyword keyword = restService.getKeyword(channelId, appPreference.accessToken());
            return keyword.getFilter();
        }

        @Override
        protected void onPostExecute(String string) {
            if(string != null){
                filterString = string;
                channelNameTextView.setText(channelName+ " :: "+ string);
                buttonRemoveFilter.setVisibility(View.VISIBLE);
            }else{
                channelNameTextView.setText(channelName);
                buttonRemoveFilter.setVisibility(View.GONE);
            }
        }
    }

    private class FollowUser extends AsyncTask<String, Boolean, Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            RestService restService = new RestServiceImpl();
            UserFollowForm form = new UserFollowForm();
            form.setAccessToken(appPreference.accessToken());
            form.setUsername(params[0]);
            return restService.follow(form);
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            progressDialog.dismiss();
            if(bool)
                Toast.makeText(getActivity(), "Followed", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "An Error occured", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(appPreference.loggedIn()){
            inflater.inflate(R.menu.article_list_menu, menu);
            SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.search).getActionView();

            searchView.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    Log.i("search", "submit : " + s);
                    if (s.length() == 0) {
                        return false;
                    } else {
                        saveFilter(s);
                        initListView();
                        initDataTask = new InitDataTask();
                        initDataTask.execute();
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return true;
                }

            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private ArticleRecycleSwipeAdapter newAdapter(){
        return new ArticleRecycleSwipeAdapter(getActivity(), listItems, new SwipeListArticleListener() {
            @Override
            public void onFavorite(int position) {
                favorite(position);
            }

            @Override
            public void onReadingList(int position) {
                archieve(position);
            }

            @Override
            public void onShare(int position) {
                share(position);
            }

            @Override
            public void onDetail(int position) {
                changeView(position);
            }

            @Override
            public void onFollow(int position) {
                follow(position);
            }

        });
    }

    private void initListView(){
        listItems = new ArrayList<>();
        listViewAdapter = newAdapter();
        layoutManager = new LinearLayoutManager(getActivity());
        listview.setLayoutManager(layoutManager);
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

        crossfadeLoading.setVisibility(View.VISIBLE);
        crossfadeLoading.setAlpha(1);
        listview.setVisibility(View.GONE);

    }

    public void follow(int position){
        Article article = listItems.get(position);
        progressDialog.show();
        new FollowUser().execute(article.getTwitterUsername());
    }

    public void saveFilter(String filter){
        new SetFilter().execute(filter);
    }

    @OnClick(R.id.remove_filter)
    public void removeFilter(){
        new RemoveFilter().execute();
        initListView();
        initDataTask = new InitDataTask();
        initDataTask.execute();
    }

}
