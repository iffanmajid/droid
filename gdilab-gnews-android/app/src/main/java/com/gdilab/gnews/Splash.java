package com.gdilab.gnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.gdilab.gnews.config.AppPreference;

import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 12/3/14.
 */
public class Splash extends ActionBarActivity {

    private static final String TAG = "splash screen";

    private AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreference = Esperandro.getPreferences(AppPreference.class, this);

        setContentView(R.layout.splash);

        SplashThread splashThread = new SplashThread();
        splashThread.start();
    }

    class SplashThread extends Thread {
        @Override
        public void run() {
            try {
                synchronized (this) {
                    wait(3000);
                }
            } catch (InterruptedException e) {
                Log.d(TAG, e.getMessage());
            }

            if(appPreference.firstTimeOpen()){
                Intent intent = new Intent(Splash.this, Slider.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(Splash.this, Main.class);
                startActivity(intent);
            }
            finish();
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
