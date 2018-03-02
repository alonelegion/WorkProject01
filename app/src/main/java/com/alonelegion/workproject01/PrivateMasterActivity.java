package com.alonelegion.workproject01;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PrivateMasterActivity extends AppCompatActivity {

    Locale myLocale = new Locale("ru", "RU");
    int indexInt = 0;
    Object value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_master);

        // Создаём слушателя для ввода даты
        final EditText editTextPrivateDate = (EditText) findViewById(R.id.edit_private_date);
        // Получаем календарь
        final Calendar calendar = Calendar.getInstance();
        // Подключаем DatePicker для отображения календаря при нажатии на поле даты
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "dd.MM.yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, myLocale);
                editTextPrivateDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        editTextPrivateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PrivateMasterActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText editTextTimeBegin = (EditText) findViewById(R.id.edit_time_begin);
        editTextTimeBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mBeginTime = Calendar.getInstance();
                final int hour = mBeginTime.get(Calendar.HOUR_OF_DAY);
                int minute = mBeginTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(PrivateMasterActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editTextTimeBegin.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Выберете время");
                timePickerDialog.show();
            }
        });

        final EditText editTextTimeEnd = (EditText) findViewById(R.id.edit_time_end);
        editTextTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mEndTime = Calendar.getInstance();
                final int hour = mEndTime.get(Calendar.HOUR_OF_DAY);
                int minute = mEndTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(PrivateMasterActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editTextTimeEnd.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Выберете время");
                timePickerDialog.show();
            }
        });


        //Получаем экземпляры элементов ViewGroup
        final EditText editTextPrivateFio = (EditText) findViewById(R.id.edit_private_Fio);
        final EditText editTextStreet = (EditText) findViewById(R.id.edit_street);
        final EditText editTextHouse = (EditText) findViewById(R.id.edit_house);
        final EditText editTextBlock = (EditText) findViewById(R.id.edit_block);
        final EditText editTextFlat = (EditText) findViewById(R.id.edit_flat);
        final EditText editTextPrivatePhone1 = (EditText) findViewById(R.id.edit_private_phone1);
        final EditText editTextPrivatePhone2 = (EditText) findViewById(R.id.edit_private_phone2);
        final EditText editPrivateProblem = (EditText) findViewById(R.id.edit_private_problem);
        final CheckBox checkBoxHardSoft = (CheckBox) findViewById(R.id.check_box_HardSoft);
        final Spinner choiceCity = (Spinner)findViewById(R.id.choice_city);

        // Создаем слушателя для выбора позиции города с присвоением индекса
        choiceCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value = parent.getItemAtPosition(position);
                switch (position){
                    case 0:
                        indexInt = 0;
                        break;

                    case 1:
                        indexInt = 36;
                        break;

                    case 2:
                        indexInt = 32;
                        break;

                    case 3:
                        indexInt = 24;
                        break;

                    case 4:
                        indexInt = 33;
                        break;

                    case 5:
                        indexInt = 17;
                        break;

                    case 6:
                        indexInt = 18;
                        break;

                    case 7:
                        indexInt = 3;
                        break;

                    case 8:
                        indexInt = 21;
                        break;

                    case 9:
                        indexInt = 30;
                        break;

                    case 10:
                        indexInt = 10;
                        break;

                    case 11:
                        indexInt = 27;
                        break;

                    case 12:
                        indexInt = 25;
                        break;

                    case 13:
                        indexInt = 14;
                        break;

                    case 14:
                        indexInt = 8;
                        break;

                    case 15:
                        indexInt = 23;
                        break;

                    case 16:
                        indexInt = 20;
                        break;

                    case 17:
                        indexInt = 1;
                        break;

                    case 18:
                        indexInt = 26;
                        break;

                    case 19:
                        indexInt = 4;
                        break;

                    case 20:
                        indexInt = 7;
                        break;

                    case 21:
                        indexInt = 19;
                        break;

                    case 22:
                        indexInt = 35;
                        break;

                    case 23:
                        indexInt = 9;
                        break;

                    case 24:
                        indexInt = 13;
                        break;

                    case 25:
                        indexInt = 34;
                        break;

                    case 26:
                        indexInt = 2;
                        break;

                    case 27:
                        indexInt = 12;
                        break;

                    case 28:
                        indexInt = 28;
                        break;

                    case 29:
                        indexInt = 29;
                        break;

                    case 30:
                        indexInt = 11;
                        break;

                    case 31:
                        indexInt = 6;
                        break;

                    case 32:
                        indexInt = 22;
                        break;

                    case 33:
                        indexInt = 15;
                        break;

                    case 34:
                        indexInt = 5;
                        break;

                    case 35:
                        indexInt = 31  ;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Создаем слушателей для CheckBox и SwitchCompat
        checkBoxHardSoft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()){
                    Toast.makeText(getApplicationContext(), "Проблема с железом",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Нет проблемы",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        final CheckBox checkBoxSoft = (CheckBox) findViewById(R.id.check_box_soft);

        checkBoxSoft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()){
                    Toast.makeText(getApplicationContext(), "Проблема с ПО",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Нет проблемы",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        final SwitchCompat switchCompatZvon = (SwitchCompat) findViewById(R.id.switch_zvon);

        switchCompatZvon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SwitchCompat)v).isChecked()){
                    Toast.makeText(getApplicationContext(), "Не звонить клиенту", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Позвонить клиенту", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final SwitchCompat switchCompatEmergency = (SwitchCompat) findViewById(R.id.switch_emergencyCall);

        switchCompatEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SwitchCompat)v).isChecked()){
                    Toast.makeText(getApplicationContext(), "Срочный вызов", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Обычный вызов", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final SwitchCompat switchCompatPrice = (SwitchCompat) findViewById(R.id.switch_price);

        switchCompatPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SwitchCompat)v).isChecked()){
                    Toast.makeText(getApplicationContext(), "Цены озвучены", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Цены не озвучены", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // Создаем слушателя для отправки информации при нажатии button
        findViewById(R.id.btn_privat_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewButton) {

                // Записывыем в поля введенную пользователем данные
                final String fio = editTextPrivateFio.getText().toString();
                final String street = editTextStreet.getText().toString();
                final String house = editTextHouse.getText().toString();
                final String block = editTextBlock.getText().toString();
                final String flat = editTextFlat.getText().toString();
                final String phone1 = editTextPrivatePhone1.getText().toString();
                final String phone2 = editTextPrivatePhone2.getText().toString();
                final String date = editTextPrivateDate.getText().toString();
                final String timeBegin = editTextTimeBegin.getText().toString();
                final String timeEnd = editTextTimeEnd.getText().toString();
                final String problem = editPrivateProblem.getText().toString();
                String hardSoft;
                String soft;
                String switchPrice;
                String switchZvon;
                String switchEmergency;

                // Операторы для CheckBox и SwitchCompat
                if (switchCompatZvon.isChecked()){
                    switchZvon = "1";
                }else {
                    switchZvon = "0";
                }

                if (switchCompatPrice.isChecked()){
                    switchPrice = "1";
                }else {
                    switchPrice = "2";
                }

                if (switchCompatEmergency.isChecked()){
                    switchEmergency = "1";
                }else {
                    switchEmergency = "0";
                }

                if (checkBoxHardSoft.isChecked()){
                    hardSoft = "1";
                }else{
                    hardSoft = "0";
                }

                if (checkBoxSoft.isChecked()){
                    soft = "1";
                }else {
                    soft = "0";
                }

                // Загружаем из памяти token
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PrivateMasterActivity.this);
                String token = prefs.getString("token", null);
                boolean hasToken = prefs.contains("token");

                // Собираем строку для отправки через браузер заполненной заявки
                String url = "http://109.234.153.44/fss/m_save.asp?token=" + token + "&str=<region>" +
                        indexInt + "</region><lk>1</lk><chm>1</chm>" +
                        "<fio>" + fio + "</fio>" +
                        "<data>" + date + "</data>" +
                        "<phone1>" + phone1 + "</phone1>" +
                        "<phone2>" + phone2 + "</phone2>" +
                        "<work>" + problem + "</work>" +
                        "<street>" + street + "</street>" +
                        "<dom>" + house + "</dom>" +
                        "<korp>" + block + "</korp>" +
                        "<kv>" + flat + "</kv>" +
                        "<t_begin>" + timeBegin + "</t_begin>" +
                        "<t_end>" + timeEnd + "</t_end>" +
                        "<pr_int1>" + switchPrice + "</pr_int1>" +
                        "<pr_check1>" + hardSoft + "</pr_check1>" +
                        "<pr_check2>" + soft + "</pr_check2>" +
                        "<zvon>" + switchZvon + "</zvon>" +
                        "<kod_tel>" + switchEmergency + "</kod_tel>";

                // Создаём намерение ACTION_VIEW для открытия браузера с последующим
                // всавлянием созданной ранее строки
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(url));
//                startActivity(intent);

                Toast.makeText(PrivateMasterActivity.this, url, Toast.LENGTH_LONG).show();

            }
        });
    }
}
