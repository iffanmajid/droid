package com.gdilab.gnews.fragment;

import android.app.Activity;
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
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gdilab.gnews.AddKeyword;
import com.gdilab.gnews.ArticleList;
import com.gdilab.gnews.Main;
import com.gdilab.gnews.R;
import com.gdilab.gnews.config.AppConfig;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.config.DefaultMessage;
import com.gdilab.gnews.model.api.ActionKeywordForm;
import com.gdilab.gnews.model.api.Keyword;
import com.gdilab.gnews.service.RestService;
import com.gdilab.gnews.service.RestServiceImpl;
import com.gdilab.gnews.view.KeywordRecycleSwipeAdapter;
import com.gdilab.gnews.view.SwipeListKeywordListener;
import com.gdilab.gnewsrecycleview.BaseSwipeListViewListener;
import com.gdilab.gnewsrecycleview.SwipeListView;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 12/21/14.
 */
public class KeywordListFragment extends DefaultFragment{

    public static final int ADD_KEYWORD_CODE = 1;

    List<Keyword> listItems;

    @InjectView(R.id.list)
    SwipeListView listview;

    @InjectView(R.id.add)
    FloatingActionButton buttonAdd;

    @InjectView(R.id.crossfade_loading)
    RelativeLayout crossfadeLoading;

    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;

    private ProgressDialog progressDialog;

    private KeywordRecycleSwipeAdapter listViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private AppPreference appPreference;
    private InitDataTask initDataTask;
    private boolean hideButton = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        appPreference = Esperandro.getPreferences(AppPreference.class, getActivity());

        View view = inflater.inflate(R.layout.keyword_list, container, false);
        ButterKnife.inject(this, view);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        View header = getActivity().getWindow().getDecorView().findViewById(R.id.toolbar);
        View rightItem = header.findViewById(R.id.right_item);
        rightItem.setVisibility(View.VISIBLE);
        TextView fragmentTitle = (TextView) header.findViewById(R.id.fragment_title);
        ImageView fragmentLogo = (ImageView) header.findViewById(R.id.fragment_logo);
        fragmentTitle.setText(R.string.keywords);
        fragmentLogo.setImageResource(R.drawable.icon_channel);

        listItems = new ArrayList<>();

        listViewAdapter = new KeywordRecycleSwipeAdapter(getActivity(), listItems, new SwipeListKeywordListener() {

            @Override
            public void onConfirmDelete(int position) {
                progressDialog.show();
                new DeleteDataTask().execute(position);
            }

            @Override
            public void onCancelSwipe(int position) {
                listview.closeOpenedItems();
            }
        });
        layoutManager = new LinearLayoutManager(getActivity());
        listview.setLayoutManager(layoutManager);

        listview.setSwipeListViewListener(new BaseSwipeListViewListener() {

            @Override
            public void onClickFrontView(int position) {
                changeView(position);
            }

        });

        listview.setAdapter(listViewAdapter);
        ArrayList<View> footerViews = new ArrayList<View>();
        buttonAdd.setTag(R.id.scroll_threshold_key, 2);
        footerViews.add(buttonAdd);

        listview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(listItems.size() < AppConfig.MAX_KEYWORD){
                    if(dy > 5){
                        if(!hideButton){
                            hideButton = true;
                            YoYo.with(Techniques.SlideOutDown).duration(200).playOn(buttonAdd);
                        }
                    }else if(dy < -5){
                        if(hideButton){
                            hideButton = false;
                            YoYo.with(Techniques.SlideInUp).duration(200).playOn(buttonAdd);
                        }
                    }
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                listItems = new ArrayList<>();
                new InitDataTask().execute();
            }

        });

        listview.setVisibility(View.GONE);
        buttonAdd.setVisibility(View.GONE);
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
                if(listItems.size() >= AppConfig.MAX_KEYWORD){
                    YoYo.with(Techniques.SlideOutDown).duration(200).playOn(buttonAdd);
                }else{
                    buttonAdd.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInUp).duration(200).playOn(buttonAdd);
                }
                if(refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
                super.onPostExecute(result);
            }
        }

    }

    private class DeleteDataTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            try{
                RestService restService = new RestServiceImpl();
                ActionKeywordForm actionKeywordForm = new ActionKeywordForm();
                actionKeywordForm.setAccessToken(appPreference.accessToken());
                actionKeywordForm.setId(listItems.get(params[0]).getId());
                restService.deleteKeyword(actionKeywordForm);
                return params[0];
            }catch (Exception e){
                Log.e(getClass().getName(), e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressDialog.dismiss();
            if(result != null){
                listview.closeOpenedItems();
                listItems.remove((int) result);
                listViewAdapter.notifyItemRemoved((int) result);
                listViewAdapter.notifyDataSetChanged();
                if(listItems.size() < AppConfig.MAX_KEYWORD){
                    buttonAdd.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInUp).duration(200).playOn(buttonAdd);
                }
                Log.i(getClass().getName(), "removed");
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), DefaultMessage.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void updateData() {
        RestService restService = new RestServiceImpl();
        List<Keyword> list = restService.allKeyword(appPreference.accessToken());
        listItems.addAll(list);
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

    @OnClick(R.id.add)
    public void add(){
        Intent intent = new Intent(getActivity(), AddKeyword.class);
        startActivityForResult(intent, ADD_KEYWORD_CODE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        initDataTask.cancel(true);
        ButterKnife.reset(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_KEYWORD_CODE) {
            if(resultCode == Activity.RESULT_OK){
                getActivity().finish();
                startActivity(new Intent(getActivity(), Main.class));
            }
        }
    }
}
