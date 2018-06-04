package com.alonelegion.workproject01;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alonelegion.workproject01.api.FssService;
import com.alonelegion.workproject01.api.OrderResponse;
import com.alonelegion.workproject01.service.FssApp;
import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.redmadrobot.inputmask.helper.Mask;
import com.redmadrobot.inputmask.model.CaretString;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DecorOrderActivity extends AppCompatActivity {

    Object value;
    Context c;
    Spinner mRegion;
    EditText mFio;
    EditText mData;
    EditText mPhone1;
    EditText mPhone2;
    EditText mWork;
    Button mSend;
    String region;
    String fio;
    String phone1;
    String phone2;
    String work;

    private Calendar data = Calendar.getInstance();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decor_order);

        setUpToolbar();
        setUpDate();

        c = this;

        //Получаем экземпляры элементов ViewGroup
        mRegion = (Spinner) findViewById(R.id.choice_city);
        mFio = (EditText) findViewById(R.id.edit_Fio);
        mPhone1 = (EditText) findViewById(R.id.edit_phone1);
        mPhone2 = (EditText) findViewById(R.id.edit_phone2);
        mWork = (EditText) findViewById(R.id.edit_problem);

        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "+7 ([000])-[000]-[00]-[00]",
                true,
                mPhone1,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                        Log.d(DecorOrderActivity.class.getSimpleName(), extractedValue);
                        Log.d(DecorOrderActivity.class.getSimpleName(), String.valueOf(maskFilled));
                    }
                }
        );

        mPhone1.addTextChangedListener(listener);
        mPhone1.setOnFocusChangeListener(listener);
        mPhone1.setHint(listener.placeholder());
        mPhone1.setHintTextColor(Color.WHITE);

        mSend = (Button) findViewById(R.id.btn_send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fio = mFio.getText() + "";
                phone1 = mPhone1.getText() + "";
                phone2 = mPhone2.getText() + "";
                work = mWork.getText() + "";

                if (region.length() == 0){
                    Toast.makeText(c, "выберите нужный город", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone1.length() == 0){
                    Toast.makeText(c, "Заполните номер телефона", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (region.length() > 0 || phone1.length() > 0){
                    orderSend(fio, region, data, phone1, phone2, work);
                }
            }
        });



        mRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // Присваиваем для каждогор города свой индекс в списке спинера
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
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

    // По нажатию на поле ввода даты, будем вызывать календарь и записывать в поле выбранную дату
    private void setUpDate() {
        mData = (EditText) findViewById(R.id.edit_Date);
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
    private void showDatePikerDialog(Calendar date, DatePickerDialog.OnDateSetListener listener) {
        new DatePickerDialog(
                this,
                listener,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
        ).show();

    }



    // Передаём заполненые поля через интерфейс Retrofit
    private void orderSend(String fio, String region, Calendar data, String phone1, String phone2, String work) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                DecorOrderActivity.this);
        String token = prefs.getString("token", null);
        FssService fssService = ((FssApp) getApplication()).getFssService();
                fssService.answerOrder(token, region, fio, dateFormat.format(data.getTime()),
                        phone1, phone2, work).enqueue(
                                new Callback<OrderResponse>() {

            // Получаем ответ в виде JSON - файла в котором содержится текст,
            // с последующим отображением на экране
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                String token = response.body().token;
                showAnswer(token);
                returnToDecorOrderActivity();
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.e("DecorOrderActivity", "Проблема отправки", t);
                Toast.makeText(DecorOrderActivity.this,
                        "Проблема отправки", Toast.LENGTH_LONG).show();
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

    // Выводим на экран полученное сообщение из JSON файла
    private void showAnswer(String token) {
        Toast.makeText(c, token, Toast.LENGTH_LONG).show();
    }

    private void returnToDecorOrderActivity() {
        Intent intent = new Intent(DecorOrderActivity.this,
                NavOrdersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
