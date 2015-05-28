package com.gdilab.gnews.service;

import android.util.Log;

import com.gdilab.gnews.model.api.ActionNewsForm;
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
import com.gdilab.gnews.model.api.ResponseData;
import com.gdilab.gnews.model.api.UserFollowForm;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by masasdani on 12/21/14.
 */
public class RestServiceImpl implements RestService {

    private static final String TAG = "GNEWS_API";

//    public static final String BASE_URL = "http://192.168.1.107:9090";
//    public static final String BASE_URL = "http://119.81.81.123:9090";
    public static final String BASE_URL = "http://api.gnews.io";
    public static final String SHORTEN_URL_API = "http://gnews.id/api?url=";
    public static final String REGISTER = "/account/register";
    public static final String REGISTER_BASIC = "/account/register/basic";
    public static final String FOLLOW = "/account/follow";
    public static final String RECOMENDATION_KEYWORDS = "/channel/recommendation-keyword";
    public static final String KEYWORD = "/keyword";
    public static final String FILTER = "/filter";
    public static final String VALIDATE = "/validate";
    public static final String DELETE = "/delete";
    public static final String FAVORITE = "/favorite";
    public static final String CHANNEL = "/channel";
    public static final String RECENT_ARTICLE = "/news/recent/";
    public static final String RECENT_ARTICLE_BY_KEYWORD = "/news/recent?keywordId=";
    public static final String FAVORITE_ARTICLE = "/news/favorite/";
    public static final String ARTICLE_CONTENT = "/news";
    public static final String ADD_TO_ARCHIVED = "/action/archive";
    public static final String DELETE_ARCHIVED = "/action/archive/delete";
    public static final String ADD_TO_FAVORITES_KEYWORD = "/keyword/favorite";
    public static final String ADD_TO_FAVORITES = "/action/favorite";
    public static final String ARCHIVED_ARTICLE = "/news/archive";
    public static final String DATE_FORMAT = "MMM dd, yyyy | kk:mm";

    private RestTemplate restTemplate;

    public RestServiceImpl(){
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Override
    public Credential register(Register register) {
        String url = BASE_URL+REGISTER;
        Log.d(getClass().getName(), url);
        Object object = restTemplate.postForObject(url, register, Object.class);
        Log.d(getClass().getName(), object.toString());
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) object;

        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        map = (Map<String, Object>) responseData.getData();
        if(map == null){
            Log.d(getClass().getName(), "error register");
            return null;
        }
        Credential credential = new Credential();
        if(map.containsKey("oauth"))
            credential.setOauth((Boolean) map.get("oauth"));
        if(map.containsKey("accessToken"))
            credential.setAccessToken(map.get("accessToken").toString());
        if(map.containsKey("email")){
            Object mail = map.get("email");
            if(mail != null){
                credential.setEmail(mail.toString());
            }
        }
        return credential;
    }

    @Override
    public Credential registerBasic(RegisterBasic registerBasic) {
        Object object = restTemplate.postForObject(BASE_URL+REGISTER_BASIC, registerBasic, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        if(responseData.getStatus().equalsIgnoreCase("error")){
            return null;
        }
        map = (Map<String, Object>) responseData.getData();
        Credential credential = new Credential();
        credential.setOauth((Boolean) map.get("oauth"));
        credential.setAccessToken(map.get("accessToken").toString());
        Object mail = map.get("email");
        if(mail != null){
            credential.setEmail(mail.toString());
        }
        return credential;
    }

    @Override
    public boolean addFilter(FilterKeywordForm form) {
        Object object = restTemplate.postForObject(BASE_URL+KEYWORD+FILTER, form, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        if(responseData.getStatus().equalsIgnoreCase("success")){
            return true;
        }else{
            Log.e(TAG, responseData.getMessage());
        }
        return false;
    }

    @Override
    public boolean removeFilter(DeleteFilterForm form) {
        Object object = restTemplate.postForObject(BASE_URL+KEYWORD+FILTER+DELETE, form, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        if(responseData.getStatus().equalsIgnoreCase("success")){
            return true;
        }else{
            Log.e(TAG, responseData.getMessage());
        }
        return false;
    }

    @Override
    public boolean addKeyword(CreateKeywordForm createKeywordForm) {
        Object object = restTemplate.postForObject(BASE_URL+KEYWORD, createKeywordForm, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        if(responseData.getStatus().equalsIgnoreCase("success")){
            return true;
        }else{
            Log.e(TAG, responseData.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteKeyword(ActionKeywordForm actionKeywordForm) {
        Object object = restTemplate.postForObject(BASE_URL+KEYWORD+DELETE, actionKeywordForm, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        if(responseData.getStatus().equalsIgnoreCase("success")){
            return true;
        }else{
            Log.e(TAG, responseData.getMessage());
        }
        return false;
    }

    @Override
    public boolean ownChannel(NewChannel newChannel) {
        Object object = restTemplate.postForObject(BASE_URL+CHANNEL, newChannel, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        if(responseData.getStatus().equalsIgnoreCase("success")){
            return true;
        }else{
            Log.e(TAG, responseData.getMessage());
        }
        return false;
    }

    @Override
    public List<Article> recentArticle(Long id, String accessToken, String country, String filter, int limit, int offset) {
        if(filter != null){
            if(filter.length() == 0) filter = null;
        }
        String tokenParam = "";
        if(accessToken != null) tokenParam = "&accessToken="+accessToken;
        String countryParam = "";
        if(country != null) countryParam = "&country="+country;
        String url = BASE_URL+RECENT_ARTICLE_BY_KEYWORD
                +id+tokenParam+countryParam+"&limit="+limit+"&offset="+offset;
        if(filter != null){
            url = url + "&filter="+ filter;
        }
        Log.d("recent article", url);
        Object object = restTemplate.getForObject(url, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);

        List<Article> articles = new ArrayList<>();
        List<Object> list =  (List<Object>) responseData.getData();
        for(Object o : list){
            Map<String, Object> mapObject = (Map<String, Object>) o;
            Article article = new Article();
            Long dates = (long) ((double) mapObject.get("date"));
            Date date = new Date(dates * 1000);
            article.setId(mapObject.get("id").toString());
            article.setContent(mapObject.get("content").toString());
            article.setHost(mapObject.get("host").toString());
            article.setUrl(mapObject.get("url").toString());
            article.setTitle(mapObject.get("title").toString());
            if(mapObject.containsKey("twitterUsername")){
                if(mapObject.get("twitterUsername") != null){
                    article.setTwitterUsername(mapObject.get("twitterUsername").toString());
                }
            }
            String format = new SimpleDateFormat(DATE_FORMAT).format(date);
            article.setDate(format);
            if(mapObject.get("image") != null)
                article.setImage(mapObject.get("image").toString());
            articles.add(article);
        }

        return articles;
    }

    @Override
    public List<Article> favoritesArticles(Long id, String accessToken, int limit, int offset ) {
        String tokenParam = "";
        if(accessToken != null && accessToken.length() > 0) tokenParam = "&accessToken="+accessToken;
        Object object = restTemplate.getForObject(BASE_URL + FAVORITE_ARTICLE + id +"?" + tokenParam + "&limit=" + limit + "&offset=" + offset, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        List<Article> articles = new ArrayList<>();
        List<Object> list =  (List<Object>) responseData.getData();
        for(Object o : list){
            Map<String, Object> mapObject = (Map<String, Object>) o;
            Article article = new Article();
            Long dates = (Long) mapObject.get("date");
            Date date = new Date(dates * 1000);
            article.setId(mapObject.get("id").toString());
            article.setContent(mapObject.get("content").toString());
            article.setHost(mapObject.get("host").toString());
            article.setUrl(mapObject.get("url").toString());
            article.setTitle(mapObject.get("title").toString());
            if(mapObject.containsKey("twitterUsername")){
                if(mapObject.get("twitterUsername") != null){
                    article.setTwitterUsername(mapObject.get("twitterUsername").toString());
                }
            }

            String format = new SimpleDateFormat(DATE_FORMAT).format(date);
            article.setDate(format);
            if(mapObject.get("image") != null)
                article.setImage(mapObject.get("image").toString());
            articles.add(article);
        }

        return articles;
    }

    @Override
    public List<Article> arcivedArticles(String accessToken, int limit, int offset) {
        String tokenParam = "";
        if(accessToken != null && accessToken.length() > 0) tokenParam = "&accessToken="+accessToken;
        String url = BASE_URL+ARCHIVED_ARTICLE+"?"+tokenParam+"&limit="+limit+"&offset="+offset;
        Object object = restTemplate.getForObject(url, Object.class);
        Log.d(getClass().getName(), url);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        List<Article> articles = new ArrayList<>();
        List<Object> list =  (List<Object>) responseData.getData();
        for(Object o : list){
            Map<String, Object> mapObject = (Map<String, Object>) o;
            Article article = new Article();
            Long dates = (long) ((double) mapObject.get("date"));
            Date date = new Date(dates * 1000);
            article.setId(mapObject.get("id").toString());
            article.setContent(mapObject.get("content").toString());
            article.setHost(mapObject.get("host").toString());
            article.setUrl(mapObject.get("url").toString());
            article.setTitle(mapObject.get("title").toString());
            if(mapObject.containsKey("twitterUsername")){
                if(mapObject.get("twitterUsername") != null){
                    article.setTwitterUsername(mapObject.get("twitterUsername").toString());
                }
            }

            String format = new SimpleDateFormat(DATE_FORMAT).format(date);
            article.setDate(format);
            if(mapObject.get("image") != null)
                article.setImage(mapObject.get("image").toString());
            articles.add(article);
        }

        return articles;
    }

    @Override
    public List<String> recomendationKeywords(String accessToken) {
        String tokenParam = "";
        if(accessToken != null && accessToken.length() > 0) tokenParam = "&accessToken="+accessToken;
        Object object = restTemplate.getForObject(BASE_URL+RECOMENDATION_KEYWORDS+tokenParam, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        List<String> strings = new ArrayList<>();
        List<Object> list =  (List<Object>) responseData.getData();
        for(Object o : list){
            strings.add(o.toString());
        }

        return strings;
    }

    @Override
    public Keyword getKeyword(Long id, String accessToken) {
        String url = BASE_URL+KEYWORD+"/"+id+"?"+"&accessToken="+accessToken;
        Object object = restTemplate.getForObject(url, Object.class);
        Log.d(TAG, url);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);

        Object o =  responseData.getData();
        Map<String, Object> mapObject = (Map<String, Object>) o ;
        Keyword keyword = new Keyword();
        keyword.setId((long)((double)mapObject.get("id")));
        keyword.setName(mapObject.get("name").toString());
        if( mapObject.get("preferedImage") != null){
            keyword.setPreferedImage(mapObject.get("preferedImage").toString());
        }
        if( mapObject.get("filter") != null){
            keyword.setFilter(mapObject.get("filter").toString());
        }

        return  keyword;
    }

    @Override
    public List<Keyword> allKeyword(String accessToken) {
        String url = BASE_URL+KEYWORD+"?"+"&accessToken="+accessToken;
        Object object = restTemplate.getForObject(url, Object.class);
        Log.d(TAG, url);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);

        List<Keyword> keywords = new ArrayList<>();
        List<Object>list =  (List<Object>) responseData.getData();
        for(Object o : list){
            Map<String, Object> mapObject = (Map<String, Object>) o ;
            Keyword keyword = new Keyword();
            keyword.setId((long)((double)mapObject.get("id")));
            keyword.setName(mapObject.get("name").toString());
            if( mapObject.get("preferedImage") != null){
                keyword.setPreferedImage(mapObject.get("preferedImage").toString());
            }
            if( mapObject.get("filter") != null){
                keyword.setFilter(mapObject.get("filter").toString());
            }
            keywords.add(keyword);
        }
        return  keywords;
    }

    @Override
    public Article articleContent(String url, String accessToken) {
        String tokenParam = "";
        if(accessToken != null) tokenParam = "&accessToken="+accessToken;
        String aa = BASE_URL+ARTICLE_CONTENT+"?"+tokenParam+"&url="+url;
        Log.d(TAG, aa);
        Object object = restTemplate.getForObject(BASE_URL+ARTICLE_CONTENT
                +"?accessToken="+accessToken+"&url="+url, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);

        Map<String, Object> mapObject = (Map<String, Object>) responseData.getData();
        Article article = new Article();
        Long dates = (Long) mapObject.get("date");
        Date date = new Date(dates);
        article.setId(mapObject.get("id").toString());
        article.setContent(mapObject.get("content").toString());
        article.setHost(mapObject.get("host").toString());
        article.setUrl(mapObject.get("url").toString());
        article.setTitle(mapObject.get("title").toString());

        String format = new SimpleDateFormat(DATE_FORMAT).format(date);
        article.setDate(format);
        article.setImage(mapObject.get("image").toString());

        return article;
    }

    @Override
    public String shortenUrl(String url) {
        return restTemplate.getForObject(SHORTEN_URL_API+url, String.class);
    }

    @Override
    public boolean validateKeyword(String accessToken, String keyword) {
        Object object = restTemplate.getForObject(BASE_URL+KEYWORD+VALIDATE
                +"?accessToken="+accessToken+"&keyword="+keyword, Object.class);
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        if(responseData.getStatus().equalsIgnoreCase("Success")){
            return true;
        }
        return false;
    }

    @Override
    public boolean follow(UserFollowForm form) {
        Object object = restTemplate.postForObject(BASE_URL+FOLLOW, form, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        if(responseData.getStatus().equalsIgnoreCase("success")){
            return true;
        }else{
            Log.e(TAG, responseData.getMessage());
        }
        return false;
    }

    @Override
    public List<Channel> allChannel(String accessToken) {
        Object object = restTemplate.getForObject(BASE_URL+CHANNEL
                +"?accessToken="+accessToken, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);

        List<Channel> channels = new ArrayList<Channel>();
        List<Object>list =  (List<Object>) responseData.getData();
        for(Object o : list){
            Map<String, Object> mapObject = (Map<String, Object>) o ;
            Channel channel= new Channel();
            channel.setId((long)((double)mapObject.get("id")));
            channel.setName(mapObject.get("name").toString());
            channel.setDescription(mapObject.get("description").toString());
            channel.setKeywords(mapObject.get("keywords").toString());

            channels.add(channel);
        }
        return  channels;
    }

    @Override
    public List<Keyword> favoriteKeyword(String accessToken, int pageNumber, int pageSize) {
        String tokenParam = "";
        if(accessToken != null && accessToken.length() > 0) tokenParam = "&accessToken="+accessToken;
        String url = BASE_URL+KEYWORD+FAVORITE+"?"+tokenParam+"&pageNumber="+pageNumber+"&pageSize="+pageSize;
        Object object = restTemplate.getForObject(url, Object.class);
        Log.d(TAG, url);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);

        List<Keyword> keywords = new ArrayList<>();
        List<Object>list =  (List<Object>) responseData.getData();
        for(Object o : list){
            Map<String, Object> mapObject = (Map<String, Object>) o ;
            Keyword keyword = new Keyword();
            keyword.setId((long)((double)mapObject.get("id")));
            keyword.setName(mapObject.get("name").toString());
            if( mapObject.get("preferedImage") != null){
                keyword.setPreferedImage(mapObject.get("preferedImage").toString());
            }
            keywords.add(keyword);
        }
        return  keywords;
    }

    @Override
    public boolean addtoFavorite(String url, String accessToken) {
        Object object = restTemplate.getForObject(BASE_URL+ADD_TO_FAVORITES+"?accessToken="+accessToken+"&url="+url, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        if(responseData.getStatus().equalsIgnoreCase("success")){
            return true;
        }else{
            Log.e(TAG, responseData.getMessage());
        }
        return false;
    }

    @Override
    public boolean addtoFavoriteKeyword(ActionKeywordForm actionKeywordForm) {
        Object object = restTemplate.postForObject(BASE_URL + ADD_TO_FAVORITES_KEYWORD, actionKeywordForm, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        if(responseData.getStatus().equalsIgnoreCase("success")){
            return true;
        }else{
            Log.e(TAG, responseData.getMessage());
        }
        return false;
    }

    @Override
    public boolean addtoarchive(String url, String accessToken) {
        Object object = restTemplate.getForObject(BASE_URL+ADD_TO_ARCHIVED+"?accessToken="+accessToken+"&url="+url, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        if(responseData.getStatus().equalsIgnoreCase("success")){
            return true;
        }else{
            Log.e(TAG, responseData.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteArchive(String url, String accessToken) {
        ActionNewsForm actionNewsForm = new ActionNewsForm();
        actionNewsForm.setAccessToken(accessToken);
        actionNewsForm.setUrl(url);
        Object object = restTemplate.postForObject(BASE_URL+DELETE_ARCHIVED, actionNewsForm, Object.class);
        @SuppressWarnings("unchecked")
        Map<String, Object>  map = (Map<String, Object>) object;
        ResponseData<Object> responseData = new ResponseData<Object>();
        mapToResponseData(map, responseData);
        if(responseData.getStatus().equalsIgnoreCase("success")){
            return true;
        }else{
            Log.e(TAG, responseData.getMessage());
        }
        return false;
    }

    private void mapToResponseData(Map<String, Object> map, ResponseData<Object> responseData){
        if(map.get("status") != null)
            responseData.setStatus(map.get("status").toString());
        if(map.get("message") != null)
            responseData.setMessage(map.get("message").toString());
        if(map.containsKey("data"))
            responseData.setData(map.get("data"));
    }

}
