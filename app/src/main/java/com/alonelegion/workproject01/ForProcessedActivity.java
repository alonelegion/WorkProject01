package com.alonelegion.workproject01;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ForProcessedActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted);

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.submitted_list);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(ForProcessedActivity.this, "Отчёт составлен",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            //Выполнение запроса на URL-адрес и получение ответа(response)
            String url = "http://109.234.153.44/fss/qwerynzmain.asp?token=717085139&dtstart=01.01.2018&dtend=19.01.2018";
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

                        String peredan = c.getString("Kol-vo peredannih zaiavok");
                        String vipolnen = c.getString("Kol-vo vipolnennih zaiavok");
                        String otkazov = c.getString("Kol-vo otkazov");
                        String otmen = c.getString("Kol-vo otmen");
                        String vrabote = c.getString("V rabote");

                        HashMap<String, String> contact = new HashMap<>();

                        //adding each child node to HashMap key => value
                        contact.put("Kol-vo peredannih zaiavok", peredan);
                        contact.put("Kol-vo vipolnennih zaiavok", vipolnen);
                        contact.put("Kol-vo otkazov", otkazov);
                        contact.put("Kol-vo otmen", otmen);
                        contact.put("V rabote", vrabote);

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
            ListAdapter adapter = new SimpleAdapter(ForProcessedActivity.this, contactList,
                    R.layout.item_for_processed_list, new String[]{"Kol-vo peredannih zaiavok", "Kol-vo vipolnennih zaiavok", "Kol-vo otkazov", "Kol-vo otmen", "V rabote"},
                    new int[]{R.id.proc_peredan, R.id.proc_vipolnen, R.id.proc_otkazov, R.id.proc_otmen, R.id.proc_v_rabote});
            lv.setAdapter(adapter);
        }
    }
}
