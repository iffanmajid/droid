package com.gdilab.gnews.service;

import com.gdilab.gnews.config.AppPreference;

/**
 * Created by masasdani on 1/20/15.
 */
public class AppService {

    AppPreference appPreference;

    public AppService(AppPreference appPreference) {
        this.appPreference = appPreference;
    }

    public void logout(){
        appPreference.firstTimeOpen(true);
        appPreference.country("");
        appPreference.email("");
        appPreference.accessToken("");
        appPreference.finishedProfile(false);
        appPreference.loggedIn(false);
        appPreference.profilePicture("");
        appPreference.fullname("");
        appPreference.twitterAccessTokenSecret("");
        appPreference.twitterAccessToken("");
        appPreference.twitterUsername("");
        appPreference.password("");
    }

}
