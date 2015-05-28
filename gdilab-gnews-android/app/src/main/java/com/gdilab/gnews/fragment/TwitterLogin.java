package com.gdilab.gnews.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.gdilab.gnews.Main;
import com.gdilab.gnews.R;
import com.gdilab.gnews.config.AppConfig;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.model.api.Credential;
import com.gdilab.gnews.model.api.Register;
import com.gdilab.gnews.service.RestService;
import com.gdilab.gnews.service.RestServiceImpl;
import com.gdilab.gnews.service.TwitterService;
import com.gdilab.gnews.service.TwitterServiceImpl;

import de.devland.esperandro.Esperandro;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by masasdani on 12/21/14.
 */
public class TwitterLogin extends DefaultFragment {

    private static final String TAG = "twitter_login";

    private WebView twitterLoginWebView;
    private ProgressDialog progressDialog;

    private static Twitter twitter;
    private static RequestToken requestToken;

    private AppPreference appPreference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        appPreference = Esperandro.getPreferences(AppPreference.class, getActivity());

        View view = inflater.inflate(R.layout.twitter_oauth, container, false);

        if(AppConfig.TWITTER_CONSUMER_KEY == null || AppConfig.TWITTER_CONSUMER_SECRET == null){
            getFragmentManager().popBackStack();
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        twitterLoginWebView = (WebView) view.findViewById(R.id.twitter_login_web_view);
        twitterLoginWebView.setWebViewClient( new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if( url.contains(AppConfig.TWITTER_CALLBACK_URL)) {
                    Uri uri = Uri.parse(url);
                    saveAccessTokenAndFinish(uri);
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(progressDialog != null) progressDialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if(progressDialog != null) progressDialog.show();
            }
        });

        Log.d(TAG, "ASK OAUTH");
        askOAuth();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void saveAccessTokenAndFinish(final Uri uri){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String verifier = uri.getQueryParameter(AppConfig.URL_TWITTER_OAUTH_VERIFIER);
                try {
                    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

                    appPreference.twitterAccessToken(accessToken.getToken());
                    appPreference.twitterAccessTokenSecret(accessToken.getTokenSecret());

                    Log.d(TAG, "TWITTER LOGIN SUCCESS!!!");

                    onTwitterLoginSuccess();

                } catch (Exception e) {
                    e.printStackTrace();
                    if(e.getMessage() != null){
                        Log.e(TAG, e.getMessage());
                    } else {
                        Log.e(TAG, "ERROR: Twitter callback failed");
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "ERROR: Twitter callback failed", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                        }
                    });

                }
            }
        }).start();
    }

    private void askOAuth() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(AppConfig.TWITTER_CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(AppConfig.TWITTER_CONSUMER_SECRET);
        Configuration configuration = configurationBuilder.build();
        twitter = new TwitterFactory(configuration).getInstance();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    requestToken = twitter.getOAuthRequestToken(AppConfig.TWITTER_CALLBACK_URL);
                } catch (Exception e) {
                    final String errorString = e.toString();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            AppMsg.makeText(getActivity(), errorString.toString(), AppMsg.STYLE_ALERT).show();
                            changeView(new SigninFragment());
                        }
                    });
                    e.printStackTrace();
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "LOADING AUTH URL");
                        twitterLoginWebView.loadUrl(requestToken.getAuthenticationURL());
                    }
                });
            }
        }).start();
    }

    public void onTwitterLoginSuccess(){
        TwitterService twitterService = new TwitterServiceImpl(
                appPreference.twitterAccessToken(),
                appPreference.twitterAccessTokenSecret());

        RestService restService = new RestServiceImpl();

        User user = twitterService.getTwitterUser();
        if(user != null){
            Register register = new Register(
                    AppConfig.GNEWS_APP_ID,
                    AppConfig.GNEWS_APP_SECRET,
                    user.getId(),
                    user.getScreenName(),
                    user.getName(),
                    appPreference.twitterAccessToken(),
                    appPreference.twitterAccessTokenSecret(),
                    user.getProfileImageURL());

            Credential credential = restService.register(register);

            appPreference.accessToken(credential.getAccessToken());
            appPreference.profilePicture(user.getOriginalProfileImageURL());
            appPreference.fullname(user.getName());
            appPreference.twitterUsername(user.getScreenName());
            appPreference.loggedIn(true);

            appPreference.email(credential.getEmail());
            appPreference.finishedProfile(true);

            getActivity().setResult(AppConfig.RESULT_SUCCESS);
            getActivity().finish();
        }else{
            getFragmentManager().popBackStack();
        }

    }

}
