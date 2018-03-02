package com.alonelegion.workproject01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

public class MainActivity extends AppCompatActivity {

    Button mButtonOrders;
    Button mButtonStatistics;
    Button mButtonSignOut;

    Button mButtonChat;
    Button mButtonNews;
    Button mButtonFaq;

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
        ViewGroup container = (ViewGroup) findViewById(R.id.anim_main_container);

        ViewCompat.animate(logoImageView)
                .translationY(-250)
                .setStartDelay(STARTUP_DELAY)
                .setDuration(ANIM_ITEM_DURATION).setInterpolator(
                new DecelerateInterpolator(1.2f)).start();

        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            if (!(v instanceof Button)){
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
        setContentView(R.layout.activity_main);

        KenBurnsView kbv = (KenBurnsView) findViewById(R.id.imageTest);
        RandomTransitionGenerator generator = new RandomTransitionGenerator();
        kbv.setTransitionGenerator(generator);



        // Переход на экран заявок
        mButtonOrders = (Button)findViewById(R.id.btn_Orders);
        mButtonOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                startActivity(intent);
            }
        });

        // Переход на экран статистики
        mButtonStatistics = (Button) findViewById(R.id.btn_statistics);
        mButtonStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });

        // Выход из аккаунта
        mButtonSignOut = (Button) findViewById(R.id.btn_signout);
        mButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Вызываем метод очистки памяти от токена
                removeToken();
                // Отправляем на экран входа в личный кабинет
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mButtonChat = (Button) findViewById(R.id.btn_chat);
        mButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "РАЗДЕЛ НАХОДИТСЯ В РАЗРАБОТКЕ", Toast.LENGTH_SHORT).show();
            }
        });

        mButtonNews = (Button) findViewById(R.id.btn_news);
        mButtonNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "РАЗДЕЛ НАХОДИТСЯ В РАЗРАБОТКЕ", Toast.LENGTH_SHORT).show();
            }
        });

        mButtonFaq = (Button) findViewById(R.id.btn_faq);
        mButtonFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "РАЗДЕЛ НАХОДИТСЯ В РАЗРАБОТКЕ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Удаляем токен из памяти
    private void removeToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove("token").apply();
    }
}
