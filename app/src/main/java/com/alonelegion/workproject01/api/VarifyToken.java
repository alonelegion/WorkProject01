package com.alonelegion.workproject01.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by develop2 on 23.01.2018.
 */

public interface VarifyToken {

    @GET("varify.asp")
    public Call<VerifyResponse> varify(@Path("token") String token);
}
