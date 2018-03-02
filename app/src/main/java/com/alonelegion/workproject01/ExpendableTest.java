package com.alonelegion.workproject01;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.alonelegion.workproject01.adapters.WorkOrderListAdapter;
import com.alonelegion.workproject01.service.JSONParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by develop2 on 26.02.2018.
 */

public class ExpendableTest extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expandableListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private ProgressDialog mprocessingdialog;
    private static String url = "http://109.234.153.44/fss/qwerynz.asp?token=717085139&dtstart=01.02.2018&dtend=10.02.2018";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_orders);

        mprocessingdialog = new ProgressDialog(this);

        // get the listview
        expandableListView = (ExpandableListView) findViewById(R.id.workorders_list);

        //preparing list data
        new DownloadJason().execute();
    }

    private class DownloadJason extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Showing Progress dialog
            mprocessingdialog.setTitle("Пожалуйста подождите...");
            mprocessingdialog.setMessage("Идёт загрузка");
            mprocessingdialog.setCancelable(false);
            mprocessingdialog.setIndeterminate(false);
            mprocessingdialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONParse jp = new JSONParse();
            String jsonstr = jp.makeServiceCall(url);
            Log.d("Response = ", jsonstr);

            if (jsonstr != null){
                // For Header title ArrayList
                listDataHeader = new ArrayList<String>();
                // Hashmap for child data key = header title and value = ArrayList ( child data)
                listDataChild = new HashMap<String, List<String>>();

                try {
                    JSONObject jobj = new JSONObject(jsonstr);
                    JSONArray jsonArray = jobj.getJSONArray("CROSS");

                    for (int hk = 0; hk < jsonArray.length(); hk++){
                        JSONObject d = jsonArray.getJSONObject(hk);
                        // Adding header data
                        listDataHeader.add(d.getString("NZ_ID"));

                        // Adding child data for lease offer
                        List<String> lease_offer = new ArrayList<String>();

                        lease_offer.add(d.getString("NZ_ID"));
                        lease_offer.add(d.getString("Data"));
                        lease_offer.add(d.getString("t_begin"));
                        lease_offer.add(d.getString("how"));
                        lease_offer.add(d.getString("Filial"));
                        lease_offer.add(d.getString("city"));
                        lease_offer.add(d.getString("street"));
                        lease_offer.add(d.getString("fio"));
                        lease_offer.add(d.getString("tip_nz"));
                        lease_offer.add(d.getString("stat"));
                        lease_offer.add(d.getString("nz_id_part"));
                        
                        // Header into Child data
                        listDataChild.put(listDataHeader.get(hk), lease_offer);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(ExpendableTest.this, "Пожалуйста проверьте интернет соединение",
                        Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mprocessingdialog.dismiss();
            // call constructor
            listAdapter = new WorkOrderListAdapter(getApplicationContext(),
                    listDataHeader, listDataChild);
            // setting list adapter
            expandableListView.setAdapter(listAdapter);
        }
    }
}
