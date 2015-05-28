package com.gdilab.gnews.service;

import com.gdilab.gnews.model.api.Article;
import com.gdilab.gnews.model.api.Channel;
import com.gdilab.gnews.model.api.CreateKeywordForm;
import com.gdilab.gnews.model.api.Credential;
import com.gdilab.gnews.model.api.ActionKeywordForm;
import com.gdilab.gnews.model.api.DeleteFilterForm;
import com.gdilab.gnews.model.api.FilterKeywordForm;
import com.gdilab.gnews.model.api.Keyword;
import com.gdilab.gnews.model.api.NewChannel;
import com.gdilab.gnews.model.api.Register;
import com.gdilab.gnews.model.api.RegisterBasic;
import com.gdilab.gnews.model.api.UserFollowForm;

import java.util.List;

/**
 * Created by masasdani on 12/21/14.
 */
public interface RestService {

    public Credential register(Register register);
    public Credential registerBasic(RegisterBasic registerBasic);
    public boolean addFilter(FilterKeywordForm form);
    public boolean removeFilter(DeleteFilterForm form);
    public boolean addKeyword(CreateKeywordForm createKeywordForm);
    public boolean deleteKeyword(ActionKeywordForm actionKeywordForm);
    public boolean ownChannel(NewChannel newChannel);
    public boolean addtoFavorite(String url, String accessToken);
    public boolean addtoFavoriteKeyword(ActionKeywordForm actionKeywordForm);
    public boolean addtoarchive(String url, String accessToken);
    public boolean deleteArchive(String url, String accessToken);
    public List<String> recomendationKeywords(String accessToken);
    public Keyword getKeyword(Long id, String accessToken);
    public List<Keyword> allKeyword(String accessToken);
    public List<Channel> allChannel(String accessToken);
    public List<Keyword> favoriteKeyword(String accessToken, int limit, int offset);
    public List<Article> recentArticle(Long id, String accessToken, String country, String filter, int limit, int offset);
    public List<Article> favoritesArticles(Long id, String accessToken, int limit, int offset);
    public List<Article> arcivedArticles(String accessToken,int limit, int offset);
    public Article articleContent(String url, String accessToken);
    public String shortenUrl(String url);
    public boolean validateKeyword(String accessToken, String keyword);
    public boolean follow(UserFollowForm form);

}
