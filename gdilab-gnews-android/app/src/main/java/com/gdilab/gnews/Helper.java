package com.gdilab.gnews;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.gdilab.gnews.R;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.fragment.ArticleDetailFragment;
import com.gdilab.gnews.model.api.ActionKeywordForm;
import com.gdilab.gnews.service.RestService;
import com.gdilab.gnews.service.RestServiceImpl;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 12/21/14.
 */
public class Helper extends ActionBarActivity {

    private String url;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bundle bundle = getIntent().getExtras();

        appPreference = Esperandro.getPreferences(AppPreference.class, this);

        url = bundle.getString("url");
        changeView(new ArticleDetailFragment());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RestService restService = new RestServiceImpl();
        switch(item.getItemId()) {
            case android.R.id.home :
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }

    private void changeView(Fragment fragment){
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
