package com.alonelegion.workproject01;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.alonelegion.workproject01.api.LoginResponse;
import com.alonelegion.workproject01.api.VerifyResponse;
import com.alonelegion.workproject01.service.FssApp;
import com.alonelegion.workproject01.service.VarifyApp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by develop2 on 16.01.2018.
 */

public class HelloActivity extends Activity {

    String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean hasToken = prefs.contains("token");

        if (hasToken) {
            startMainActivity();
//            login(token);
//            if (token != "0"){
//                startMainActivity();
//            }else {
//                startLoginActivity();
//            }
        } else {
            startLoginActivity();
        }
    }


//    private void login(String token) {
//        ((VarifyApp) getApplication()).getVarifyToken().varify(token).enqueue(new Callback<VerifyResponse>() {
//            @Override
//            public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
//                String tokenCheck = response.body().token;
//                saveTokenCheck(tokenCheck);
//            }
//
//            @Override
//            public void onFailure(Call<VerifyResponse> call, Throwable t) {
//                Log.e("LoginActivity", "Failed to login", t);
//                Toast.makeText(HelloActivity.this, "Failed to login", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    private void saveTokenCheck(String tokenCheck) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        prefs.edit().putString("token", tokenCheck).apply();
//    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, NavMainActivity.class);
        startActivity(intent);
    }
}
