package com.alonelegion.workproject01;


import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

public class OrdersActivity extends AppCompatActivity {

    Button mSubmittedList;
    Button mMakeOrder;
    Button mWorkOrders;

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
        setContentView(R.layout.activity_orders);

        KenBurnsView kbv = (KenBurnsView) findViewById(R.id.imageTest);
        RandomTransitionGenerator generator = new RandomTransitionGenerator();
        kbv.setTransitionGenerator(generator);

        mSubmittedList = (Button)findViewById(R.id.btn_Submitted_list);
        mSubmittedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrdersActivity.this, SubmittedActivity.class);
                startActivity(intent);
            }
        });

        mMakeOrder = (Button) findViewById(R.id.btn_make_order);
        mMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrdersActivity.this, OrderTypeActivity.class);
                startActivity(intent);
            }
        });

        mWorkOrders = (Button) findViewById(R.id.work_orders);
        mWorkOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this, WorkOrdersActivity.class);
                startActivity(intent);
            }
        });
    }
}
