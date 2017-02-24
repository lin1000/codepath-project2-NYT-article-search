package com.codepath.week2assignment.net;

import android.content.Context;
import android.util.Log;

import com.codepath.week2assignment.R;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lin1000 on 2017/2/18.
 */

public class OkHttpClientFactory {

    private static OkHttpClientFactory instance;
    private OkHttpClient client ;
    private Context context;

    public static OkHttpClientFactory getInstance(Context context){
        if(instance==null){
            synchronized (OkHttpClientFactory.class){
                if(instance==null) instance = new OkHttpClientFactory(context);
            }
        }
        return instance;
    }

    public OkHttpClient getClient(){
        return client;
    }

    private OkHttpClientFactory(Context context){

        final String VALUE_MY_API_KEY = context.getResources().getString(R.string.my_api_key);
        Log.d(this.getClass().getName(),"VALUE_MY_API_KEY:"+VALUE_MY_API_KEY);
        client = new okhttp3.OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(
                        new okhttp3.Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request original = chain.request();
                                HttpUrl url = chain.request().url()
                                        .newBuilder()
                                        .addQueryParameter(NYTClient.PARA_API_KEY, VALUE_MY_API_KEY)
                                        .build();
                                Request request = chain.request().newBuilder().url(url).build();
                                return chain.proceed(request);
                            }
                        })
                .build();;

    }
}
