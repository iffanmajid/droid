package com.gdilab.gnews;

import android.app.Activity;
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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.gdilab.gnews.config.AppConfig;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.fragment.AboutFragment;
import com.gdilab.gnews.fragment.ChannelListFragment;
import com.gdilab.gnews.fragment.FavoriteListFragment;
import com.gdilab.gnews.fragment.FavoriteListPublicFragment;
import com.gdilab.gnews.fragment.PrivacyPolicyFragment;
import com.gdilab.gnews.fragment.ReadingListFragment;
import com.gdilab.gnews.fragment.HelpFragment;
import com.gdilab.gnews.fragment.KeywordListFragment;
import com.gdilab.gnews.fragment.SettingFragment;
import com.gdilab.gnews.fragment.TermOfServiceFragment;
import com.gdilab.gnews.service.AppService;
import com.gdilab.gnews.view.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 12/18/14.
 */
public class Main extends ActionBarActivity {

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    ActionBarDrawerToggle drawerToggle;

    @InjectView(R.id.fullname)
    TextView fullnameTextView;

    @InjectView(R.id.username)
    TextView usernameTextView;

    @InjectView(R.id.thumbnail)
    RoundedImageView thumbnail;

    @InjectView(R.id.logout)
    Button logoutButton;

    @InjectView(R.id.top_bar)
    View profileBox;

    private AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        appPreference = Esperandro.getPreferences(AppPreference.class, this);

        setContentView(R.layout.main);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);

        if (appPreference.loggedIn()) {
            Picasso.with(this)
                    .load(appPreference.profilePicture())
                    .into(thumbnail);
            fullnameTextView.setText(appPreference.fullname());
            usernameTextView.setText("@" + appPreference.twitterUsername());
            logoutButton.setText(getResources().getText(R.string.logout));
        } else {
            logoutButton.setVisibility(View.GONE);
            profileBox.setVisibility(View.GONE);
        }

        if (savedInstanceState == null) {
            if (appPreference.loggedIn()) {
                keywords();
            } else {
                favorite();
            }
        }

    }

    private void changeView(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            drawerLayout.closeDrawers();
            return;
        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Main.super.onBackPressed();
                        return;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        return;
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @OnClick(R.id.thumbnail)
    public void setThumbnail() {
        changeView(new SettingFragment());
        if (appPreference.loggedIn()) {
            drawerLayout.closeDrawers();
        } else {
            twitterLogin();
        }
    }

    @OnClick(R.id.channels)
    public void channel() {
        drawerLayout.closeDrawers();
        if (appPreference.loggedIn()) {
            changeView(new ChannelListFragment());
        } else {
            twitterLogin();
        }
    }

    @OnClick(R.id.keywords)
    public void keywords() {
        drawerLayout.closeDrawers();
        if (appPreference.loggedIn()) {
            changeView(new KeywordListFragment());
        } else {
            twitterLogin();
        }
    }

    @OnClick(R.id.favorite)
    public void favorite() {
        drawerLayout.closeDrawers();
        if (appPreference.loggedIn()) {
            changeView(new FavoriteListFragment());
        } else {
            changeView(new FavoriteListPublicFragment());
            drawerLayout.closeDrawers();
        }
    }

    @OnClick(R.id.clipping)
    public void clippings() {
        drawerLayout.closeDrawers();
        if (appPreference.loggedIn()) {
            changeView(new ReadingListFragment());
        } else {
            twitterLogin();
        }
    }

    @OnClick(R.id.setting)
    public void setting() {
        drawerLayout.closeDrawers();
        if (appPreference.loggedIn()) {
            changeView(new SettingFragment());
        } else {
            twitterLogin();
        }
    }

    @OnClick(R.id.about)
    public void about() {
        changeView(new AboutFragment());
        drawerLayout.closeDrawers();
    }

    @OnClick(R.id.help)
    public void help() {
        changeView(new HelpFragment());
        drawerLayout.closeDrawers();
    }

    @OnClick(R.id.privacy_policy)
    public void privacyPolicy() {
        changeView(new PrivacyPolicyFragment());
        drawerLayout.closeDrawers();
    }

    @OnClick(R.id.terms_of_service)
    public void termOfService() {
        changeView(new TermOfServiceFragment());
        drawerLayout.closeDrawers();
    }

    @OnClick(R.id.logout)
    public void logout() {
        if (appPreference.loggedIn()) {
            AppService appService = new AppService(appPreference);
            appService.logout();
            finish();
            startActivity(new Intent(this, Splash.class));
        } else {
            twitterLogin();
        }
    }

    private void twitterLogin() {
        startActivityForResult(new Intent(this, Signin.class), AppConfig.SIGNIN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("callback", "request code" + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.SIGNIN_REQUEST_CODE && resultCode == AppConfig.RESULT_SUCCESS) {
            finish();
            startActivity(new Intent(this, Main.class));
        }
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
