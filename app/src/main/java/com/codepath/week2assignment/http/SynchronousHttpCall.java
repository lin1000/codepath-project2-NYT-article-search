package com.codepath.week2assignment.http;

import okhttp3.OkHttpClient;

/**
 * Created by lin1000 on 2017/2/18.
 */

public class SynchronousHttpCall {

    private static SynchronousHttpCall instance;
    private OkHttpClient client ;

    public static SynchronousHttpCall getInstance(){
        if(instance==null){
            synchronized (SynchronousHttpCall.class){
                if(instance==null) instance = new SynchronousHttpCall();
            }
        }
        return instance;
    }

    public OkHttpClient getClient(){
        return client;
    }

    private SynchronousHttpCall(){
        client = new OkHttpClient();
    }
}
