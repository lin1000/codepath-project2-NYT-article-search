package com.codepath.week2assignment.net;


import android.content.Context;
import android.util.Log;

import com.codepath.week2assignment.model.UIFilter;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by lin1000 on 2017/2/24.
 */

public class NYTClient {
    public static final String PARA_API_KEY = "api-key";
    public static final String API_VERSION = "v2/";
    public static final String API_BASE_URL = "https://api.nytimes.com/svc/";
    public static final String API_SEARCH_BASE_PATH= "search/" ;
    public static final String API_SEARCH_SUFFIX_PATH= "articlesearch.json" ;
    public static final String PARA_BEGIN_DATE = "begin_date";
    public static final String PARA_END_DATE = "end_date";
    public static final String PARA_SORT = "sort";
    public static final String PARA_NEWS_DESK = "news_desk:";
    public static final String PARA_FQ = "fq";

    private Context context;
    //client is singleton within application
    private OkHttpClient client;

    public NYTClient (Context context) {
        //OkHttpClient is singleton : multiple NYTClient will share one OkHttpClient
        client =  OkHttpClientFactory.getInstance(context).getClient();
    }

    public OkHttpClient getClient() {
        return client;
    }

    public Request requestBuilderByUIFilter(int page, String query, UIFilter filter){
        HttpUrl.Builder httpUrlBuilder = new Request.Builder().url(getSearchApiUrl()).build().url().newBuilder();

        if(filter.getBeginDate() != null)  httpUrlBuilder.addQueryParameter(PARA_BEGIN_DATE, filter.getBeginDate() );
        if(filter.getSort() != null)  httpUrlBuilder.addQueryParameter(PARA_SORT, filter.getSort() );
        if(filter.genNewsDesk() != null)  httpUrlBuilder.addQueryParameter(PARA_FQ, PARA_NEWS_DESK+ filter.genNewsDesk() );
        Request request = new Request.Builder().url(httpUrlBuilder.build()).build();
        return request;
    }

    private String getSearchApiUrl() {
        String getSearchApiUrl = API_BASE_URL + API_SEARCH_BASE_PATH + API_VERSION + API_SEARCH_SUFFIX_PATH;
        Log.d(this.getClass().getName(),"getSearchApiUrl="+ getSearchApiUrl);
        return getSearchApiUrl;
    }


    private String addQueryParameter(String key,String value,String separator){
        return key+ "=" + value + separator;
    }

    private String addQueryParameter(String key,String value){
        return key+ "=" + value;
    }

    public String getSearchApiUrl(int page, String query) {
        String getSearchApiUrl = API_BASE_URL + API_SEARCH_BASE_PATH + API_VERSION + API_SEARCH_SUFFIX_PATH + "?"+
                addQueryParameter("page",String.valueOf(page),"&") +
                addQueryParameter("q",query);
        Log.d(this.getClass().getName(),"getSearchApiUrl="+ getSearchApiUrl);
        return getSearchApiUrl;
    }

    public String getSearchApiUrl(int page, String query, String beginDate, String Sort) {
        String getSearchApiUrl = getSearchApiUrl(page,query) +
                addQueryParameter("beginDate",beginDate,"&") +
                addQueryParameter("Sort",Sort);
        Log.d(this.getClass().getName(),"getSearchApiUrl="+ getSearchApiUrl);
        return getSearchApiUrl;
    }

}
