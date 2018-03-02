package com.alonelegion.workproject01;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ForFinanceActivity extends AppCompatActivity {

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
            Toast.makeText(ForFinanceActivity.this, "Отчёт составлен",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            //Выполнение запроса на URL-адрес и получение ответа(response)
            String url = "http://109.234.153.44/fss/qwerynzfin.asp?token=717085139&dtstart=01.01.2018&dtend=19.01.2018";
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

                        String komissia = c.getString("itogo komissia za ot4etnii period");
                        String viplacheno = c.getString("itogo viplacheno");
                        String kViplate = c.getString("itogo k viplate");

                        HashMap<String, String> contact = new HashMap<>();

                        //adding each child node to HashMap key => value
                        contact.put("itogo komissia za ot4etnii period", komissia);
                        contact.put("itogo viplacheno", viplacheno);
                        contact.put("itogo k viplate", kViplate);

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
            ListAdapter adapter = new SimpleAdapter(ForFinanceActivity.this, contactList,
                    R.layout.item_for_finance_list, new String[]{"itogo komissia za ot4etnii period", "itogo viplacheno", "itogo k viplate"},
                    new int[]{R.id.fin_komissia, R.id.fin_viplacheno, R.id.fin_k_viplate});
            lv.setAdapter(adapter);
        }
    }
}
