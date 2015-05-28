package com.gdilab.gnews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.fragment.AboutFragment;
import com.gdilab.gnews.fragment.ChannelListFragment;
import com.gdilab.gnews.fragment.FavoriteListFragment;
import com.gdilab.gnews.fragment.HelpFragment;
import com.gdilab.gnews.fragment.InputKeywordFragmentNew;
import com.gdilab.gnews.fragment.KeywordListFragment;
import com.gdilab.gnews.fragment.ReadingListFragment;
import com.gdilab.gnews.fragment.RegisterFragment;
import com.gdilab.gnews.fragment.SettingFragment;
import com.gdilab.gnews.service.AppService;
import com.gdilab.gnews.view.RoundedImageView;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 12/18/14.
 */
public class AddKeyword extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.blank);
        ButterKnife.inject(this);

        if(savedInstanceState == null){
            changeView(new InputKeywordFragmentNew());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home :
                setResult(RESULT_CANCELED);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void changeView(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
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
