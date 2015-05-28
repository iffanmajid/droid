package com.gdilab.gnews.service;

import android.util.Log;

import com.gdilab.gnews.config.AppConfig;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by masasdani on 12/21/14.
 */
public class TwitterServiceImpl implements TwitterService {

    private Twitter twitter;

    public TwitterServiceImpl(String accessToken, String accessTokenSecret) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(AppConfig.TWITTER_CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(AppConfig.TWITTER_CONSUMER_SECRET);
        configurationBuilder.setOAuthAccessToken(accessToken);
        configurationBuilder.setOAuthAccessTokenSecret(accessTokenSecret);
        Configuration configuration = configurationBuilder.build();
        twitter = new TwitterFactory(configuration).getInstance();
    }

    public TwitterServiceImpl(Twitter twitter){
        this.twitter = twitter;
    }

    @Override
    public User getTwitterUser() {
        try {
            return twitter.showUser(twitter.getId());
        } catch (TwitterException e) {
            Log.e("twitter_api", e.getMessage());
            return null;
        }
    }
}
