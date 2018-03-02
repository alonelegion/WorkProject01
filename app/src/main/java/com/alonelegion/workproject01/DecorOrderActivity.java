package com.alonelegion.workproject01;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DecorOrderActivity extends AppCompatActivity {

    Locale myLocale = new Locale("ru", "RU");
    int indexInt = 0;
    Object value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decor_order);

        KenBurnsView kbv = (KenBurnsView) findViewById(R.id.imageTest);
        RandomTransitionGenerator generator = new RandomTransitionGenerator();
        kbv.setTransitionGenerator(generator);

        //Получаем экземпляры элементов ViewGroup
        final EditText editTextFio = (EditText) findViewById(R.id.edit_Fio);
        final EditText editTextData = (EditText) findViewById(R.id.edit_Date);
        final EditText editTextPhone1 = (EditText) findViewById(R.id.edit_phone1);
        final EditText editTextPhone2 = (EditText) findViewById(R.id.edit_phone2);
        final EditText editTextProblem = (EditText) findViewById(R.id.edit_problem);
        final Spinner choiceCity = (Spinner) findViewById(R.id.choice_city);


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

        // Для удобства, при нажатии на дату будем вызывать
        final Calendar calendar = Calendar.getInstance();
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
                editTextData.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        editTextData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewDate) {
                new DatePickerDialog(DecorOrderActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Создаем слушателя для отправки информации при нажатии button
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Записывыем в поля введенную пользователем данные
                final String fio = editTextFio.getText().toString();
                final String date = editTextData.getText().toString();
                final String phoneNumber1 = editTextPhone1.getText().toString();
                final String phoneNumber2 = editTextPhone2.getText().toString();
                final String problem = editTextProblem.getText().toString();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                        DecorOrderActivity.this);
                String token = prefs.getString("token", null);
                boolean hasToken = prefs.contains("token");

                String url = "http://109.234.153.44/fss/m_save.asp?token=" + token + "&str=" +
                        "<region>" + indexInt + "</region>" +
                        "<fio>" + fio + "</fio>" +
                        "<date>" + date + "</date>" +
                        "<phone1>" + phoneNumber1 + "</phone1>" +
                        "<phone2>" + phoneNumber2 + "</phone2>" +
                        "<work>" + problem + "</work>";

                // Создаём намерение ACTION_VIEW для открытия браузера с последующим
                // всавлянием созданной ранее строки
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

//                Toast.makeText(DecorOrderActivity.this, url, Toast.LENGTH_LONG).show();
            }
        });
    }
}
