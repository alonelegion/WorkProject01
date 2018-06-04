package com.alonelegion.workproject01;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alonelegion.workproject01.api.FssService;
import com.alonelegion.workproject01.api.OrderResponse;
import com.alonelegion.workproject01.service.FssApp;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.SimpleTimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivateMasterActivity extends AppCompatActivity {

    Object value;
    Context context;

    Spinner mRegion;
    CheckBox mPr_check1;
    CheckBox mPr_check2;
    SwitchCompat mZvon;
    SwitchCompat mPr_int1;
    SwitchCompat mKod_tel;

    EditText mFio;
    EditText mData;
    EditText mPhone1;
    EditText mPhone2;
    EditText mWork;
    EditText mStreet;
    EditText mDom;
    EditText mKorp;
    EditText mKv;
    EditText mT_begin;
    EditText mT_end;

    Button mSend;

    String region;
    String fio;
    String phone1;
    String phone2;
    String work;
    String street;
    String dom;
    String korp;
    String kv;
    String t_begin;
    String t_end;
    String pr_check1;
    String pr_check2;
    String pr_int1;
    String kod_tel;
    String zvon;
    String lk = "1";
    String chm = "1";

    private Calendar data = Calendar.getInstance();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_master);

        setUpToolbar();

        context = this;

        // Получаем экзепляры элементов ViewGroup
        mRegion = (Spinner) findViewById(R.id.choice_city);
        mFio = (EditText) findViewById(R.id.edit_private_Fio);
        mPhone1 = (EditText) findViewById(R.id.edit_private_phone1);
        mPhone2 = (EditText) findViewById(R.id.edit_private_phone2);
        mWork = (EditText) findViewById(R.id.edit_private_problem);
        mStreet = (EditText) findViewById(R.id.edit_street);
        mDom = (EditText) findViewById(R.id.edit_house);
        mKorp = (EditText) findViewById(R.id.edit_block);
        mKv = (EditText) findViewById(R.id.edit_flat);
        mPr_check1 = (CheckBox) findViewById(R.id.check_box_HardSoft);
        mPr_check2 = (CheckBox) findViewById(R.id.check_box_soft);
        mZvon = (SwitchCompat) findViewById(R.id.switch_zvon);
        mPr_int1 = (SwitchCompat) findViewById(R.id.switch_price);
        mKod_tel = (SwitchCompat) findViewById(R.id.switch_emergencyCall);

        setUpDate();
        setUpTimeStart();
        setUpTimeEnd();
        setUpCheckHardSoft();

        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "+7 ([000])-[000]-[00]-[00]",
                true,
                mPhone1,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                        Log.d(MainActivity.class.getSimpleName(), extractedValue);
                        Log.d(MainActivity.class.getSimpleName(), String.valueOf(maskFilled));
                    }
                }
        );

        mPhone1.addTextChangedListener(listener);
        mPhone1.setOnFocusChangeListener(listener);
        mPhone1.setHint(listener.placeholder());

        mSend = (Button) findViewById(R.id.btn_privat_send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fio = mFio.getText() + "";
                phone1 = mPhone1.getText() + "";
                phone2 = mPhone2.getText() + "";
                work = mWork.getText() + "";
                street = mStreet.getText() + "";
                dom = mDom.getText() + "";
                korp = mKorp.getText() + "";
                kv = mKv.getText() + "";
                t_begin = mT_begin.getText() + "";
                t_end = mT_end.getText() + "";

                if (fio.length() == 0){
                    Toast.makeText(context, "Введите имя", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone1.length() == 0){
                    Toast.makeText(context, "Введите номер телефона", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (street.length() == 0){
                    Toast.makeText(context, "Введите улицу", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dom.length() == 0){
                    Toast.makeText(context, "Введите дом", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (t_begin.length() == 0){
                    Toast.makeText(context, "Выберите начальное время", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (t_end.length() == 0){
                    Toast.makeText(context, "Выберите конечное время", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone1.length() > 0 || fio.length() > 0 ||
                        street.length() > 0 || dom.length() > 0 || t_begin.length() > 0 ||
                        t_end.length() > 0){
                    orderSend(fio, region, data, phone1, phone2,
                            work, street, dom, korp, kv,
                            t_begin, t_end, pr_int1, pr_check1,
                            pr_check2, zvon, kod_tel);
//                Intent intent = new Intent(PrivateMasterActivity.this,
//                        OrdersActivity.class);
//                startActivity(intent);
                }



            }
        });

        mPr_check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO может getApplicationContext?
                if (((CheckBox)v).isChecked()){
                    pr_check1 = "1";
                    Toast.makeText(context, "Проблема с железом", Toast.LENGTH_SHORT).show();
                }else{
                    pr_check1 = "0";
                    Toast.makeText(context, "Нет проблемы", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPr_check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO может getApplicationContext?
                if (((CheckBox)v).isChecked()){
                    pr_check2 = "1";
                    Toast.makeText(context, "Проблема с ПО", Toast.LENGTH_SHORT).show();
                }else{
                    pr_check2 = "0";
                    Toast.makeText(context, "Нет проблемы", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mZvon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SwitchCompat)v).isChecked()){
                    zvon = "1";
                    Toast.makeText(context, "Не звонить клиенту", Toast.LENGTH_SHORT).show();
                }else {
                    zvon = "0";
                    Toast.makeText(context, "Позвонить клиенту", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mKod_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SwitchCompat)v).isChecked()){
                    kod_tel = "1";
                    Toast.makeText(context, "Срочный вызов", Toast.LENGTH_SHORT).show();
                }else {
                    kod_tel = "0";
                    Toast.makeText(context, "Обычный вызов", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPr_int1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SwitchCompat)v).isChecked()){
                    pr_int1 = "1";
                    Toast.makeText(context, "Цены озвучены", Toast.LENGTH_SHORT).show();
                }else {
                    pr_int1 = "0";
                    Toast.makeText(context, "Цены не озвучены", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Создаем слушателя для выбора позиции города с присвоением индекса
        mRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value = parent.getItemAtPosition(position);
                switch (position) {
                    case 0:
                        region = "0";
                        break;

                    case 1:
                        region = "36";
                        break;

                    case 2:
                        region = "32";
                        break;

                    case 3:
                        region = "24";
                        break;

                    case 4:
                        region = "33";
                        break;

                    case 5:
                        region = "17";
                        break;

                    case 6:
                        region = "18";
                        break;

                    case 7:
                        region = "3";
                        break;

                    case 8:
                        region = "21";
                        break;

                    case 9:
                        region = "30";
                        break;

                    case 10:
                        region = "10";
                        break;

                    case 11:
                        region = "27";
                        break;

                    case 12:
                        region = "25";
                        break;

                    case 13:
                        region = "14";
                        break;

                    case 14:
                        region = "8";
                        break;

                    case 15:
                        region = "23";
                        break;

                    case 16:
                        region = "20";
                        break;

                    case 17:
                        region = "1";
                        break;

                    case 18:
                        region = "26";
                        break;

                    case 19:
                        region = "4";
                        break;

                    case 20:
                        region = "7";
                        break;

                    case 21:
                        region = "19";
                        break;

                    case 22:
                        region = "35";
                        break;

                    case 23:
                        region = "9";
                        break;

                    case 24:
                        region = "13";
                        break;

                    case 25:
                        region = "34";
                        break;

                    case 26:
                        region = "2";
                        break;

                    case 27:
                        region = "12";
                        break;

                    case 28:
                        region = "28";
                        break;

                    case 29:
                        region = "29";
                        break;

                    case 30:
                        region = "11";
                        break;

                    case 31:
                        region = "6";
                        break;

                    case 32:
                        region = "22";
                        break;

                    case 33:
                        region = "15";
                        break;

                    case 34:
                        region = "5";
                        break;

                    case 35:
                        region = "31";
                        break;

                    case 36:
                        region = "16";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void returnToPrivateMasterActivity() {
        Intent intent = new Intent(PrivateMasterActivity.this,
                NavOrdersActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private void orderSend(String fio, String region, Calendar data, String phone1, String phone2,
                           String work, String street, String dom, String korp, String kv,
                           String t_begin, String t_end, String pr_int1, String pr_check1,
                           String pr_check2, String zvon, String kod_tel) {
        // Загружаем из памяти Токен для идентификации пользователя
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                PrivateMasterActivity.this);
        String token = prefs.getString("token", null);
        FssService fssService = ((FssApp) getApplication()).getFssService();
                fssService.answerPrivate(token, region, lk, chm, fio, dateFormat.format(data.getTime()),
                        phone1, phone2, work, street, dom, korp, kv, t_begin, t_end, pr_int1, pr_check1,
                        pr_check2, zvon, kod_tel).enqueue(
                        new Callback<OrderResponse>() {
                            // Получаем ответ в виде JSON - файла в котором содержится текст,
                            // с последующим отображением на экране
                            @Override
                            public void onResponse(Call<OrderResponse> call,
                                                   Response<OrderResponse> response) {
                                String token = response.body().token;
                                showAnswer(token);
                                returnToPrivateMasterActivity();
                            }

                            @Override
                            public void onFailure(Call<OrderResponse> call, Throwable t) {
                                Log.e("DecorOrderctivity", "Проблема отправки", t);
                                Toast.makeText(PrivateMasterActivity.this,
                                        "Проблема отправки", Toast.LENGTH_LONG).show();
                            }
                        }
                );
    }

    private void showAnswer(String token) {
        Toast.makeText(context, token, Toast.LENGTH_LONG).show();
    }


    private void setUpCheckHardSoft() {
        mPr_check1 = (CheckBox) findViewById(R.id.check_box_HardSoft);
        mPr_check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()){
                    pr_check1 = "1";
                    Toast.makeText(getApplicationContext(), "Проблема с железом",
                            Toast.LENGTH_SHORT).show();
                }else {
                    pr_check1 = "0";
                    Toast.makeText(getApplicationContext(), "Нет проблемы",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // По нажатию на поле ввода времени, будем вызывать часы
    // и записывать в поле временной период "ОТ"
    private void setUpTimeStart() {
        mT_begin = (EditText) findViewById(R.id.edit_time_begin);
        //TODO проверить эту строку
        mT_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mBeginTime = Calendar.getInstance();
                int hour = mBeginTime.get(Calendar.HOUR_OF_DAY);
                int minute = mBeginTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        PrivateMasterActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mT_begin.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, true);
                        timePickerDialog.setTitle("Выберите время");
                        timePickerDialog.show();
            }
        });
    }

    // По нажатию на поле ввода времени, будем вызывать часы
    // и записывать в поле временной период "ДО"
    private void setUpTimeEnd() {
        mT_end = (EditText) findViewById(R.id.edit_time_end);
        //TODO проверить эту строку
        mT_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mEndTime = Calendar.getInstance();
                int hour = mEndTime.get(Calendar.HOUR_OF_DAY);
                int minute = mEndTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        PrivateMasterActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mT_end.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, true);
                timePickerDialog.setTitle("Выберите время");
                timePickerDialog.show();
            }
        });
    }

    // По нажатию на поле ввода даты, будем вызывать календарь
    // и записывать в поле выбранную дату
    private void setUpDate() {
        mData = (EditText) findViewById(R.id.edit_private_date);
        mData.setText(dateFormat.format(data.getTime()));
        mData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePikerDialog(data, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        data.set(year, month, dayOfMonth);
                        mData.setText(dateFormat.format(data.getTime()));
                    }
                });
            }
        });
    }

    // Отображаем календарь с годом, месяцем и днём
    private void showDatePikerDialog(Calendar data, DatePickerDialog.OnDateSetListener listener) {
        new DatePickerDialog(
                this,
                listener,
                data.get(Calendar.YEAR),
                data.get(Calendar.MONTH),
                data.get(Calendar.DAY_OF_MONTH)
        ).show();
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
