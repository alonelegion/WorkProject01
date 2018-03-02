package com.alonelegion.workproject01.service;

import android.app.Application;

import com.alonelegion.workproject01.BuildConfig;
import com.alonelegion.workproject01.api.FssService;
import com.alonelegion.workproject01.api.VarifyToken;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by develop2 on 23.01.2018.
 */

public class VarifyApp extends Application{
    
    private VarifyToken varifyToken;

    @Override
    public void onCreate() {
        super.onCreate();
        createVarifyToken();
    }

    private void createVarifyToken() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://109.234.153.44/fss/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        varifyToken = retrofit.create(VarifyToken.class);
    }

    public VarifyToken getVarifyToken(){
        return varifyToken;
    }
}
