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

public class OnTransferredActivity extends AppCompatActivity {

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
            Toast.makeText(OnTransferredActivity.this, "Json data is downloading",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            //Выполнение запроса на URL-адрес и получение ответа(response)
            String url = "http://109.234.153.44/fss/qwerynzpartstat.asp?token=717085139&dtstart=01.01.2018&dtend=19.01.2018";
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

                        String peredan = c.getString("Peredannie zaiavki za otchetnii period");
                        String obrabot = c.getString("Obrabotannie zayavki");
                        String ne_obrabot = c.getString("Ne obrabotannie zayavki");
                        String peredymal = c.getString("Peredymal");
                        String sdelal_sam = c.getString("Sdelal sam");
                        String obrat_k_dryg = c.getString("Obratilsa k drygim");
                        String nekor_dann = c.getString("Nekorrektnie dannie");
                        String nedozvon = c.getString("Nedozvon");
                        String dubl = c.getString("Dubl");
                        String ne_region = c.getString("Ne viezjaem v region");
                        String ne_rabot = c.getString("Ne vipolnyaem takie raboti");
                        String adres_sc = c.getString("Dali adres SC");
                        String sam_perezvon = c.getString("Sam perezvonit");
                        String hotel_konsul = c.getString("Hotel konsyltacyu po telefony");
                        String v_rabot = c.getString("V rabote");

                        HashMap<String, String> contact = new HashMap<>();

                        //adding each child node to HashMap key => value
                        contact.put("Peredannie zaiavki za otchetnii period", peredan);
                        contact.put("Obrabotannie zayavki", obrabot);
                        contact.put("Ne obrabotannie zayavki", ne_obrabot);
                        contact.put("Peredymal", peredymal);
                        contact.put("Sdelal sam", sdelal_sam);
                        contact.put("Obratilsa k drygim", obrat_k_dryg);
                        contact.put("Nekorrektnie dannie", nekor_dann);
                        contact.put("Nedozvon", nedozvon);
                        contact.put("Dubl", dubl);
                        contact.put("Ne viezjaem v region", ne_region);
                        contact.put("Ne vipolnyaem takie raboti", ne_rabot);
                        contact.put("Dali adres SC", adres_sc);
                        contact.put("Sam perezvonit", sam_perezvon);
                        contact.put("Hotel konsyltacyu po telefony", hotel_konsul);
                        contact.put("V rabote", v_rabot);

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
            ListAdapter adapter = new SimpleAdapter(OnTransferredActivity.this, contactList,
                    R.layout.item_on_transferred_list, new String[]{"Peredannie zaiavki za otchetnii period", "Obrabotannie zayavki",
                    "Ne obrabotannie zayavki", "Peredymal", "Sdelal sam", "Obratilsa k drygim", "Nekorrektnie dannie", "Nedozvon",
                    "Dubl", "Ne viezjaem v region", "Ne vipolnyaem takie raboti", "Dali adres SC", "Sam perezvonit",
                    "Hotel konsyltacyu po telefony", "V rabote"},
                    new int[]{R.id.trans_pered, R.id.trans_obrabot, R.id.trans_ne_obrabot,
                            R.id.trans_peredymal, R.id.trans_sdelal_sam, R.id.trans_obrat_k_drygim, R.id.trans_nekorrekt_dannie,
                            R.id.trans_nedozvon, R.id.trans_dubl, R.id.trans_ne_region,
                            R.id.trans_ne_raboti, R.id.trans_adres_sc, R.id.trans_sam_perezvon,
                            R.id.trans_konsultacyu, R.id.trans_v_rabote});
            lv.setAdapter(adapter);
        }
    }
}
