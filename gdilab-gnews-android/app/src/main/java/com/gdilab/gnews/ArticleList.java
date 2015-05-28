package com.gdilab.gnews;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.fragment.ArticleDetailFragment;
import com.gdilab.gnews.fragment.ArticleListFragment;
import com.gdilab.gnews.model.api.ActionKeywordForm;
import com.gdilab.gnews.service.RestService;
import com.gdilab.gnews.service.RestServiceImpl;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 12/21/14.
 */
public class ArticleList extends ActionBarActivity {

    private Long keywordId;
    private String keywordName;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bundle bundle = getIntent().getExtras();

        keywordId = bundle.getLong("id");
        keywordName = bundle.getString("name");

        changeView(new ArticleListFragment());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home :
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeView(Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putLong("id", keywordId);
        bundle.putString("name", keywordName);
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.init(this, Gnews.FLURRY_API);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

}
