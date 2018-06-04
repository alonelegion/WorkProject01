package com.alonelegion.workproject01;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alonelegion.workproject01.api.FssService;
import com.alonelegion.workproject01.api.RegistrationResponse;
import com.alonelegion.workproject01.service.FssApp;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    Object value;
    Context context;
    Spinner mNbase;
    EditText mFio;
    EditText mPhone;
    EditText mPhone2;
    EditText mEmail;

    String fio;
    String phone;
    String phone2;
    String nbase;
    String email;

    Button mRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setUpToolbar();

        context = this;

        // Получаем экземпляры элементов ViewGroup
        mNbase = (Spinner) findViewById(R.id.reg_choice_city);
        mFio = (EditText) findViewById(R.id.reg_name);
        mPhone = (EditText) findViewById(R.id.reg_phone);
        mPhone2 = (EditText) findViewById(R.id.reg_phone2);
        mEmail = (EditText) findViewById(R.id.reg_email);
        mRegistration = (Button) findViewById(R.id.btn_registration);

        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "+7 ([000])-[000]-[00]-[00]",
                true,
                mPhone,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                        Log.d(RegistrationActivity.class.getSimpleName(), extractedValue);
                        Log.d(RegistrationActivity.class.getSimpleName(), String.valueOf(maskFilled));
                    }
                }
        );

        mPhone.addTextChangedListener(listener);
        mPhone.setOnFocusChangeListener(listener);
        mPhone.setHint(listener.placeholder());
        mPhone.setHintTextColor(Color.WHITE);

        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fio = mFio.getText() + "";
                phone = mPhone.getText() + "";
                phone2 = mPhone2.getText() + "";
                email = mEmail.getText() + "";

                if (fio.length() == 0){
                    Toast.makeText(context,
                            "Введите имя", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone.length() == 0){
                    Toast.makeText(context,
                            "Заполните номер телефона", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.length() == 0){
                    Toast.makeText(context,
                            "Заполните Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (fio.length() > 0 || phone.length() > 0 || email.length() > 0) {
                    registrationSend(fio, phone, phone2, nbase, email);

                    showConfirmDialog();
                }
            }
        });

        mNbase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                value = parent.getItemAtPosition(position);
                switch (position) {
                    case 0:
                        nbase = "0";
                        break;

                    case 1:
                        nbase = "36";
                        break;

                    case 2:
                        nbase = "32";
                        break;

                    case 3:
                        nbase = "24";
                        break;

                    case 4:
                        nbase = "33";
                        break;

                    case 5:
                        nbase = "17";
                        break;

                    case 6:
                        nbase = "18";
                        break;

                    case 7:
                        nbase = "3";
                        break;

                    case 8:
                        nbase = "21";
                        break;

                    case 9:
                        nbase = "30";
                        break;

                    case 10:
                        nbase = "10";
                        break;

                    case 11:
                        nbase = "27";
                        break;

                    case 12:
                        nbase = "25";
                        break;

                    case 13:
                        nbase = "14";
                        break;

                    case 14:
                        nbase = "8";
                        break;

                    case 15:
                        nbase = "23";
                        break;

                    case 16:
                        nbase = "20";
                        break;

                    case 17:
                        nbase = "1";
                        break;

                    case 18:
                        nbase = "26";
                        break;

                    case 19:
                        nbase = "4";
                        break;

                    case 20:
                        nbase = "7";
                        break;

                    case 21:
                        nbase = "19";
                        break;

                    case 22:
                        nbase = "35";
                        break;

                    case 23:
                        nbase = "9";
                        break;

                    case 24:
                        nbase = "13";
                        break;

                    case 25:
                        nbase = "34";
                        break;

                    case 26:
                        nbase = "2";
                        break;

                    case 27:
                        nbase = "12";
                        break;

                    case 28:
                        nbase = "28";
                        break;

                    case 29:
                        nbase = "29";
                        break;

                    case 30:
                        nbase = "11";
                        break;

                    case 31:
                        nbase = "6";
                        break;

                    case 32:
                        nbase = "22";
                        break;

                    case 33:
                        nbase = "15";
                        break;

                    case 34:
                        nbase = "5";
                        break;

                    case 35:
                        nbase = "31";
                        break;

                    case 36:
                        nbase = "16";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // Передаём заполненые поля через интерфейс Retrofit
    private void registrationSend(String phone, String phone2, String fio,
                                  String nbase, String email) {
        FssService fssService = ((FssApp) getApplication()).getFssService();
        fssService.registrations(phone, phone2, fio, nbase, email).enqueue(
                new Callback<RegistrationResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                        Log.e("DecorOrderActivity", "Проблема отправки", t);
                        Toast.makeText(RegistrationActivity.this, "Проблема отправки", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Заявка на регистрацию принятаю Ждите звонка оператора")
                .setNeutralButton("Понятно", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        returnToLoginActivity();
                    }
                })
                .create()
                .show();
    }

    private void returnToLoginActivity() {
        Intent intent = new Intent(RegistrationActivity.this,
                LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void Usloviya(View view) {
        Intent intent = new Intent(RegistrationActivity.this, UsloviyaActivity  .class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
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
