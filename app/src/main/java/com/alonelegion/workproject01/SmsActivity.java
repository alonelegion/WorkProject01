package com.alonelegion.workproject01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SmsActivity extends AppCompatActivity {

    Button mSmsConform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        mSmsConform = (Button) findViewById(R.id.btn_smsConform);
        mSmsConform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SmsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
