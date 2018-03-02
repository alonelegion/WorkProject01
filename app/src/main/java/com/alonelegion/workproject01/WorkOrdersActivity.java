package com.alonelegion.workproject01;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class WorkOrdersActivity extends AppCompatActivity {

    Locale myLocale = new Locale("ru", "RU");

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    String url = "";
    
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_orders);

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.workorders_list);

        //
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
                new DatePickerDialog(WorkOrdersActivity.this, date_start,calendar
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
                new DatePickerDialog(WorkOrdersActivity.this, date_finish,calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Вызываем из памяти приложения token
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WorkOrdersActivity.this);
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
                url = "http://109.234.153.44/fss/qwerynz.asp?token=" + token + "&dtstart=" + date_begin + "&dtend=" + date_end;
                new GetContacts().execute();

            }
        });
    }

    // Класс для преобразования JSON массива в JSON объекты и выгрузки их в ListView
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(WorkOrdersActivity.this, "Json data is downloading",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            //Выполнение запроса на URL-адрес и получение ответа(response)

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

                        String id = c.getString("NZ_ID");
                        String data = c.getString("Data");
                        String t_begin = c.getString("t_begin");
                        String how = c.getString("how");
                        String filial = c.getString("Filial");
                        String city = c.getString("city");
                        String street = c.getString("street");
                        String fio = c.getString("fio");
                        String tip_nz = c.getString("tip_nz");
                        String stat = c.getString("stat");
                        String nz_id_part = c.getString("nz_id_part");

                        HashMap<String, String> contact = new HashMap<>();

                        //adding each child node to HashMap key => value
                        contact.put("NZ_ID", id);
                        contact.put("Data", data);
                        contact.put("t_begin", t_begin);
                        contact.put("how", how);
                        contact.put("Filial", filial);
                        contact.put("city", city);
                        contact.put("street", street);
                        contact.put("fio", fio);
                        contact.put("tip_nz", tip_nz);
                        contact.put("stat", stat);
                        contact.put("nz_id_part", nz_id_part);
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
            ListAdapter adapter = new SimpleAdapter(WorkOrdersActivity.this, contactList,
                    R.layout.item_work_orders_list, new String[]{"NZ_ID", "Data", "t_begin", "how",
                    "Filial", "city", "street", "fio", "tip_nz", "stat", "nz_id_part"},
                    new int[]{R.id.nz_id_work, R.id.data_work, R.id.t_begin_work, R.id.how_work, R.id.filial_work,
                            R.id.city_work, R.id.street_work, R.id.fio_work, R.id.tip_nz_work, R.id.stat_work, R.id.nz_id_part_work});
            lv.clearTextFilter();
            lv.setAdapter(adapter);
        }
    }

}
