package com.gdilab.gnews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.fragment.ArticleDetailFragment;
import com.gdilab.gnews.model.api.ActionKeywordForm;
import com.gdilab.gnews.model.api.Article;
import com.gdilab.gnews.service.RestService;
import com.gdilab.gnews.service.RestServiceImpl;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 12/21/14.
 */
public class ArticleDetail extends ActionBarActivity {

    private String url;
    private String title;
    private Long keywordId;
    private boolean readingList = false;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private AppPreference appPreference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);
        ButterKnife.inject(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bundle bundle = getIntent().getExtras();

        appPreference = Esperandro.getPreferences(AppPreference.class, this);

        url = bundle.getString("url");
        title = bundle.getString("title");
        keywordId = bundle.getLong("keywordId");
        if(keywordId == 0){
            readingList = true;
        }
        Log.d(getClass().getName(), keywordId+"");

        changeView(new ArticleDetailFragment());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RestService restService = new RestServiceImpl();
        switch(item.getItemId()) {
            case R.id.action_share:
                progressDialog.show();
                new ShareUrl().execute(url, title);
                return true;
            case R.id.action_favorite:
                try{
                    restService.addtoFavorite(url, appPreference.accessToken());
                    restService.addtoFavoriteKeyword(new ActionKeywordForm(appPreference.accessToken(), keywordId));
                    Toast.makeText(this, getResources().getText(R.string.popular_success), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.d(getClass().getName(), e.getMessage());
                }
                return true;
            case R.id.action_readlater:
                try{
                    restService.addtoarchive(url, appPreference.accessToken());
                    Toast.makeText(this, getResources().getText(R.string.readinglist_success), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.d(getClass().getName(), e.getMessage());
                }
                return true;
            case android.R.id.home :
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(appPreference.loggedIn()){
            if(!readingList){
                inflater.inflate(R.menu.article_menu, menu);
            }
        }else{
            inflater.inflate(R.menu.article_menu_archieve, menu);
        }
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

    private class ShareUrl extends AsyncTask<String, String, String> {

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
