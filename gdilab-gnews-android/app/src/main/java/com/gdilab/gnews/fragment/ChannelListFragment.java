package com.gdilab.gnews.fragment;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gdilab.gnews.R;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.model.api.Channel;
import com.gdilab.gnews.service.RestService;
import com.gdilab.gnews.service.RestServiceImpl;
import com.gdilab.gnews.view.ChannelListViewAdapter;
import com.gdilab.gnews.view.ChannelRecycleAdapter;
import com.gdilab.gnews.view.ChannelRecycleSwipeAdapter;
import com.gdilab.gnews.view.RecyclerItemClickListener;
import com.gdilab.gnewsrecycleview.BaseSwipeListViewListener;
import com.gdilab.gnewsrecycleview.SwipeListView;
import com.quentindommerc.superlistview.OnMoreListener;
import com.quentindommerc.superlistview.SuperListview;
import com.quentindommerc.superlistview.SwipeDismissListViewTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 12/21/14.
 */
public class ChannelListFragment extends DefaultFragment{

    List<Channel> listItems;

    @InjectView(R.id.list)
    SwipeListView listview;

    @InjectView(R.id.crossfade_loading)
    RelativeLayout crossfadeLoading;

    private ChannelRecycleSwipeAdapter listViewAdapter;

    private RecyclerView.LayoutManager layoutManager;

    private AppPreference appPreference;

    private InitDataTask initDataTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        appPreference = Esperandro.getPreferences(AppPreference.class, getActivity());

        View view = inflater.inflate(R.layout.channel_list, container, false);
        ButterKnife.inject(this, view);

        listItems = new ArrayList<>();
        listViewAdapter = new ChannelRecycleSwipeAdapter(getActivity(), listItems);
        layoutManager = new LinearLayoutManager(getActivity());
        listview.setLayoutManager(layoutManager);

        listview.setSwipeListViewListener(new BaseSwipeListViewListener() {

        });

        listview.setAdapter(listViewAdapter);
        listview.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        changeView(position);
                    }
                })
        );
        listview.setVisibility(View.GONE);
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
                if(isCancelled()){
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
            if(result){
                finishLoading(listview, crossfadeLoading);
                listViewAdapter.notifyDataSetChanged();
                super.onPostExecute(result);
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

    private void updateData(int size) {
        RestService restService = new RestServiceImpl();
        List<Channel> channels = restService.allChannel(appPreference.accessToken());
        listItems.addAll(channels);
    }

    private void changeView(int position) {
        Channel channel = listItems.get(position);
        Bundle bundle = new Bundle();
        bundle.putLong("id", channel.getId());
        bundle.putString("name", channel.getName());
        bundle.putString("description", channel.getDescription());

        changeView(new ArticleListFragment(), bundle);
    }

    @OnClick(R.id.add)
    public void add(){
        changeView(new InputKeywordFragment());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        initDataTask.cancel(true);
        ButterKnife.reset(this);
    }

}
