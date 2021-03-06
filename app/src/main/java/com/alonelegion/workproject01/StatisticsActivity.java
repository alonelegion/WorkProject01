package com.alonelegion.workproject01;

import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

public class StatisticsActivity extends AppCompatActivity {

    Button mButtonForFinance;
    Button mButtonForProcessing;
    Button mButtonOnTransferred;

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
        ViewGroup container = (ViewGroup) findViewById(R.id.anim_main_container);

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        setUpToolbar();

        KenBurnsView kbv = (KenBurnsView) findViewById(R.id.imageTest);
        RandomTransitionGenerator generator = new RandomTransitionGenerator();
        kbv.setTransitionGenerator(generator);

        mButtonForFinance = (Button) findViewById(R.id.btn_for_finance);
        mButtonForProcessing = (Button) findViewById(R.id.btn_for_processed);
        mButtonOnTransferred = (Button) findViewById(R.id.btn_on_transferred);

        mButtonForFinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsActivity.this, ForFinanceActivity.class);
                startActivity(intent);
            }
        });

        mButtonForProcessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsActivity.this, ForProcessedActivity.class);
                startActivity(intent);
            }
        });

        mButtonOnTransferred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsActivity.this, OnTransferredActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
