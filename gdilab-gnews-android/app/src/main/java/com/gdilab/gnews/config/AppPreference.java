package com.gdilab.gnews.config;

import de.devland.esperandro.annotations.Default;
import de.devland.esperandro.annotations.SharedPreferences;

/**
 * Created by masasdani on 12/18/14.
 */

@SharedPreferences
public interface AppPreference {

    public String twitterAccessToken();
    public void twitterAccessToken(String twitterAccessToken);
    public String twitterAccessTokenSecret();
    public void twitterAccessTokenSecret(String twitterAccessTokenSecret);
    public boolean loggedIn();
    public boolean loggedIn(boolean loggedIn);
    @Default(ofBoolean = true)
    public boolean firstTimeOpen();
    public boolean firstTimeOpen(boolean firstTimeOpen);
    public String accessToken();
    public void accessToken(String accessToken);
    public boolean finishedProfile(boolean finishedProfile);
    public boolean finishedProfile();
    public String email();
    public void email(String email);
    public String password();
    public void password(String password);
    public String country();
    public void country(String country);
    public String fullname();
    public void fullname(String fullaname);
    public String twitterUsername();
    public void twitterUsername(String twitterUsername);
    public Long twitterId();
    public void twitterId(Long twitterId);
    public String profilePicture();
    public void profilePicture(String profilePicture);

}
