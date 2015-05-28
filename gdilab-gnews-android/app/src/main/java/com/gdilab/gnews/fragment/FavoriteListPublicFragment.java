package com.gdilab.gnews.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdilab.gnews.ArticleList;
import com.gdilab.gnews.Main;
import com.gdilab.gnews.R;
import com.gdilab.gnews.Signin;
import com.gdilab.gnews.config.AppConfig;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.model.api.Keyword;
import com.gdilab.gnews.service.RestService;
import com.gdilab.gnews.service.RestServiceImpl;
import com.gdilab.gnews.view.FavoriteKeywordRecycleSwipeAdapter;
import com.gdilab.gnews.view.RecyclerItemClickListener;
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
public class FavoriteListPublicFragment extends DefaultFragment{

    List<Keyword> listItems;

    @InjectView(R.id.list)
    SwipeListView listview;

    @InjectView(R.id.crossfade_loading)
    RelativeLayout crossfadeLoading;

    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;

    private FavoriteKeywordRecycleSwipeAdapter listViewAdapter;
    private LinearLayoutManager layoutManager;
    private AppPreference appPreference;
    private InitDataTask initDataTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        appPreference = Esperandro.getPreferences(AppPreference.class, getActivity());

        View view = inflater.inflate(R.layout.keyword_list_favorite_public, container, false);
        ButterKnife.inject(this, view);

        View header = getActivity().getWindow().getDecorView().findViewById(R.id.toolbar);
        View rightItem = header.findViewById(R.id.right_item);
        rightItem.setVisibility(View.VISIBLE);
        TextView fragmentTitle = (TextView) header.findViewById(R.id.fragment_title);
        ImageView fragmentLogo = (ImageView) header.findViewById(R.id.fragment_logo);
        fragmentTitle.setText(R.string.popular);
        fragmentLogo.setImageResource(R.drawable.icon_star);

        listItems = new ArrayList<>();

        listViewAdapter = new FavoriteKeywordRecycleSwipeAdapter(getActivity(), listItems);
        layoutManager = new LinearLayoutManager(getActivity());
        listview.setLayoutManager(layoutManager);

        listview.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        changeView(position);
                    }
                })
        );

        listview.setAdapter(listViewAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                listItems = new ArrayList<>();
                new InitDataTask().execute();
            }

        });

        listview.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDataTask = new InitDataTask();
        initDataTask.execute();
    }

    private class InitDataTask extends AsyncTask<Void, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                if(isCancelled()){
                    return false;
                }
                updateData();
                return true;
            }catch (Exception e){
                onDestroy();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                finishLoading(listview, crossfadeLoading);
                listViewAdapter.notifyDataSetChanged();
                if(refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
                super.onPostExecute(result);
            }
        }
    }

    @OnClick(R.id.top_panel)
    public void topPanel(){
        Intent intent = new Intent(getActivity(), Signin.class);
        startActivityForResult(intent, AppConfig.SIGNIN_REQUEST_CODE);
    }

    private void updateData() {
        RestService restService = new RestServiceImpl();
        if((listItems.size() % AppConfig.MAX_KEYWORD_FAVORITE) == 0){
            int pageNumber = listItems.size() / AppConfig.MAX_KEYWORD_FAVORITE;
            List<Keyword> list = restService.favoriteKeyword(
                    appPreference.accessToken(),
                    pageNumber + 1,
                    AppConfig.MAX_KEYWORD_FAVORITE);
            listItems.addAll(list);
        }
        Log.i(getClass().getName(), "updated");
    }

    private void changeView(int position) {
        Keyword keyword = listItems.get(position);
        Bundle bundle = new Bundle();
        bundle.putLong("id", keyword.getId());
        bundle.putString("name", keyword.getName());

        Intent intent = new Intent(getActivity(), ArticleList.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        initDataTask.cancel(true);
        ButterKnife.reset(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AppConfig.SIGNIN_REQUEST_CODE && resultCode == AppConfig.RESULT_SUCCESS){
            getActivity().finish();
            startActivity(new Intent(getActivity(), Main.class));
        }
    }
}
