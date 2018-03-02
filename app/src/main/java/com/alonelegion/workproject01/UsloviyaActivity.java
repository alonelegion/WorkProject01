package com.alonelegion.workproject01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UsloviyaActivity extends AppCompatActivity {

    Button mConformButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usloviya);

        mConformButton = (Button) findViewById(R.id.btn_conform_usloviya);
        mConformButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsloviyaActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}
