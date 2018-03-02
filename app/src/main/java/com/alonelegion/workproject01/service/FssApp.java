package com.alonelegion.workproject01.service;

import android.app.Application;

import com.alonelegion.workproject01.BuildConfig;
import com.alonelegion.workproject01.api.FssService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by develop2 on 16.01.2018.
 */

public class FssApp extends Application {

    private FssService fssService;

    @Override
    public void onCreate() {
        super.onCreate();
        createFssService();
    }

    private void createFssService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? Level.BODY : Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://109.234.153.44/fss/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        fssService = retrofit.create(FssService.class);
    }

    public FssService getFssService(){
        return fssService;
    }
}
