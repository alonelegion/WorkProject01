package com.alonelegion.workproject01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alonelegion.workproject01.api.LoginResponse;
import com.alonelegion.workproject01.service.FssApp;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    Context c;
    EditText ELogin;
    EditText passwordText;
    Button mButtonSign;
    Button mRegisterButton;
    String password;
    String login;

    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;
    public static final int ITEM_DELAY = 300;

    private boolean animationStarted = false;

    // Запуск анимации при старте активности
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus || animationStarted){
            return;
        }

        animate();

        super.onWindowFocusChanged(hasFocus);
    }

    // Описание параметров анимации
    private void animate() {
        ImageView logoImageView = (ImageView) findViewById(R.id.img_logo);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);

        ViewCompat.animate(logoImageView)
                .translationY(-250)
                .setStartDelay(STARTUP_DELAY)
                .setDuration(ANIM_ITEM_DURATION).setInterpolator(
                new DecelerateInterpolator(1.2f)).start();

        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            if (!((v instanceof Button) | (v instanceof EditText))){
                viewAnimator = ViewCompat.animate(v)
                        .translationY(50).alpha(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(1000);
            }
            else {
                viewAnimator = ViewCompat.animate(v)
                        .scaleY(1).scaleX(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(500);
            }

            viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        // Анимация для бэка
        KenBurnsView kbv = (KenBurnsView) findViewById(R.id.imageTest);
        RandomTransitionGenerator generator = new RandomTransitionGenerator();
        kbv.setTransitionGenerator(generator);

        c = this;
        ELogin = (EditText) findViewById(R.id.input_login);
        passwordText = (EditText) findViewById(R.id.input_password);

        //Отправка формы по нажатию Done на виртуальной клавиатуре
        passwordText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    mButtonSign.performClick();
                    return true;
                }
                return false;
            }
        });

        mButtonSign = (Button) findViewById(R.id.btn_sign);
        mButtonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login = ELogin.getText() + "";
                password = passwordText.getText() + "";

                // Token оповещение в случае если одно или оба полей не заполнены
                if (login.length() == 0 || password.length() == 0){
                    Toast.makeText(LoginActivity.this, "Пожалуйста заполните все поля",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (login.length() > 0 || password.length() > 0){
                     login(login, password);
                     SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                     String token = prefs.getString("token", null);
                }
            }
        });

        mRegisterButton = (Button) findViewById(R.id.btn_register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    // Передаём введённые данные по логину и паролю через интерфейс Retrofit
    private void login(String login, String passowrd) {
        ((FssApp) getApplication()).getFssService().login(login, passowrd).enqueue(new Callback<LoginResponse>() {
            // Получаем ответ в виде JSON - файла в котором содержится номер токена,
            // с последующей записью в приложение
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                String token = response.body().token;
                saveToken(token);
                if (token != "0"){
                    Intent intent = new Intent(LoginActivity.this, NavMainActivity.class);
                    startActivity(intent);
                // При получении нуля, выводим toast с просьбой
                    // проверить правильность введенных данных
                }else{
                    Toast.makeText(LoginActivity.this,
                            "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginActivity", "Неверный логин или пароль", t);
                Toast.makeText(LoginActivity.this, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Сохранияем полученное значение, с последующей передачей его
    // между активностями
    private void saveToken(String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString("token", token).apply();
    }

    public void ForgotPass(View view) {

    }

    //По нажатию клавиши назад - активность закрывается
    @Override
    public void onBackPressed() {
        Intent close = new Intent(Intent.ACTION_MAIN);
        close.addCategory(Intent.CATEGORY_HOME);
        close.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(close);
    }
}