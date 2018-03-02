package com.alonelegion.workproject01;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class SubmittedActivity extends AppCompatActivity {

    Locale myLocale = new Locale("ru", "RU");

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    String url = "";

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted);

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.submitted_list);

        final EditText editTextDateBegin = (EditText) findViewById(R.id.date_begin);
        final EditText editTextDateEnd = (EditText) findViewById(R.id.date_end);
        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date_start = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel(){
                String myFormat = "dd.MM.yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, myLocale);
                editTextDateBegin.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        editTextDateBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SubmittedActivity.this, date_start,calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener date_finish = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel(){
                String myFormat = "dd.MM.yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, myLocale);
                editTextDateEnd.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };


        editTextDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SubmittedActivity.this, date_finish,calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Вызываем из памяти приложения token
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SubmittedActivity.this);
        final String token = prefs.getString("token", null);
        boolean hasToken = prefs.contains("token");

        // Оператор на нажатие книопки для выгрузки данных из JSON
        findViewById(R.id.btn_check_orders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Записываем в переменные значения полученные из DatePickerDialog
                final String date_begin = editTextDateBegin.getText().toString();
                final String date_end = editTextDateEnd.getText().toString();

                // Склеиваем ссылку на запрос JSON
                url = "http://109.234.153.44/fss/qwerynzpart.asp?token=" + token + "&dtstart=" + date_begin + "&dtend=" + date_end;

                new GetContacts().execute();

            }
        });


    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(SubmittedActivity.this, "Json data is downloading",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            //Выполнение запроса на URL-адрес и получение ответа(response

            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null){
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    //Поучаем JSON Array
                    JSONArray contacts = jsonObj.getJSONArray("CROSS");

                    //Проход по всем полям
                    for (int i = 0; i < contacts.length(); i++){
                        JSONObject c = contacts.getJSONObject(i);
//                        String id = c.getString("id");
                        String id = c.getString("NZ_ID");
                        String id_kp = c.getString("NZ_ID_KP");
                        String data = c.getString("Data");
                        String data_f = c.getString("Data_f");
                        String filial = c.getString("Filial");
                        String fio = c.getString("Fio");
                        String prz = c.getString("prz");
                        String phone = c.getString("Phone");
                        String phone1 = c.getString("Phone1");
                        String work = c.getString("Work");


                        HashMap<String, String> contact = new HashMap<>();

                        //adding each child node to HashMap key => value
                        contact.put("NZ_ID", id);
                        contact.put("NZ_ID_KP", id_kp);
                        contact.put("Data", data);
                        contact.put("Data_f", data_f);
                        contact.put("prz", prz);
                        contact.put("Filial", filial);
                        contact.put("Fio", fio);
                        contact.put("Phone", phone);
                        contact.put("Phone1", phone1);
                        contact.put("Work", work);
//                        contact.put("name", name);

                        //adding contact to contact list
                        contactList.add(contact);
                    }
                }catch (final JSONException e){
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(SubmittedActivity.this, contactList,
                    R.layout.item_submitted_list, new String[]{"NZ_ID", "NZ_ID_KP", "Data_f", "Data",
                    "prz", "Filial", "Fio", "Phone", "Phone1", "Work"},
                    new int[]{R.id.nz_id, R.id.nz_id_kp, R.id.data_f, R.id.data, R.id.prz,
                            R.id.filial, R.id.fio, R.id.phone, R.id.phone1, R.id.work});
            lv.setAdapter(adapter);
        }
    }
}
