package com.gdilab.gnews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.flurry.android.FlurryAgent;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.service.AppService;
import com.gdilab.gnews.view.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 12/18/14.
 */
public class Setting extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.email)
    EditText emailEditText;

    @InjectView(R.id.country)
    Spinner countrySpinner;

    @InjectView(R.id.fullname)
    TextView fullnameTextView;

    @InjectView(R.id.username)
    TextView usernameTextView;

    @InjectView(R.id.thumbnail)
    RoundedImageView thumbnail;

    private AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreference = Esperandro.getPreferences(AppPreference.class, this);

        setContentView(R.layout.setting);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(appPreference.loggedIn()){
            Picasso.with(this)
                    .load(appPreference.profilePicture())
                    .into(thumbnail);
            emailEditText.setText(appPreference.email());
            fullnameTextView.setText(appPreference.fullname());
            usernameTextView.setText("@" + appPreference.twitterUsername());
        }

        if(appPreference.country().equalsIgnoreCase("Indonesia"))
            countrySpinner.setSelection(0);
        else
            countrySpinner.setSelection(1);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    appPreference.country("Indonesia");
                }else{
                    appPreference.country("United States");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.logout)
    public void logout(){
        AppService appService = new AppService(appPreference);
        appService.logout();
        finish();
        startActivity(new Intent(this, Splash.class));
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
