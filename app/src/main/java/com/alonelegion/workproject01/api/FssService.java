package com.alonelegion.workproject01.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by develop2 on 15.01.2018.
 */

public interface FssService {

    @GET("login_mob.asp")
    public Call<LoginResponse> login(@Query("login") String login, @Query("password") String password);
}
