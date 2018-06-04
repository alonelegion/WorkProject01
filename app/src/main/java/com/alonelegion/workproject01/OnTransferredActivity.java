package com.alonelegion.workproject01;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.alonelegion.workproject01.adapters.Transferred;
import com.alonelegion.workproject01.adapters.TransferredAdapter;
import com.alonelegion.workproject01.api.FssService;
import com.alonelegion.workproject01.api.TransferredResponse;
import com.alonelegion.workproject01.service.FssApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnTransferredActivity extends AppCompatActivity {
    private static final String TAG = OnTransferredActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private TransferredAdapter transferredAdapter;
    private String token;

    private EditText dateStartView;
    private EditText dateEndView;
    private Calendar dateStart = Calendar.getInstance();
    private Calendar dateEnd = Calendar.getInstance();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_transferred);
        setUpToolbar();
        setUpDateFilter();
        setUpRecyclerView();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        recyclerView = findViewById(R.id.for_finance_list);
        token = prefs.getString("token", null);
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.on_transferred_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transferredAdapter = new TransferredAdapter(new TransferredAdapter.OnTransferredClickListener() {
            @Override
            public void onTransferredClick(Transferred transferred) {
                TransferredDetailActivity.launch(OnTransferredActivity.this, transferred);
            }
        });
        recyclerView.setAdapter(transferredAdapter);
    }

    private void setUpDateFilter() {
        dateStartView = findViewById(R.id.date_begin);
        dateEndView = findViewById(R.id.date_end);
        dateStartView.setText(dateFormat.format(dateStart.getTime()));
        dateEndView.setText(dateFormat.format(dateEnd.getTime()));
        dateStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(dateStart, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateStart.set(year, month, dayOfMonth);
                        dateStartView.setText(dateFormat.format(dateStart.getTime()));
                    }
                });
            }
        });
        dateEndView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(dateEnd, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateEnd.set(year, month, dayOfMonth);
                        dateEndView.setText(dateFormat.format(dateEnd.getTime()));
                    }
                });
            }
        });
        findViewById(R.id.btn_check_orders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTransferred(dateStart, dateEnd);
            }
        });
    }

    private void showDatePickerDialog(Calendar dateStart,
                                      DatePickerDialog.OnDateSetListener listener) {
        new DatePickerDialog(
                this,
                listener,
                dateStart.get(Calendar.YEAR),
                dateStart.get(Calendar.MONTH),
                dateStart.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void loadTransferred(Calendar dateStart, Calendar dateEnd) {

        if (dateStart.getTimeInMillis() > dateEnd.getTimeInMillis()) {
            showIncorrectDataError();
            return;
        }

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.on_transferred_list);

        recyclerView.setVisibility(View.INVISIBLE);
        FssService fssService = ((FssApp) getApplication()).getFssService();
        fssService.transferreds(token, dateFormat.format(dateStart.getTime()), dateFormat.format(dateEnd.getTime())).enqueue(new Callback<TransferredResponse>() {
            @Override
            public void onResponse(Call<TransferredResponse> call, Response<TransferredResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                List<Transferred> transferreds = response.body().getTransferreds();
                showTransferred(transferreds);
            }

            @Override
            public void onFailure(Call<TransferredResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.w(TAG, "Failed to load orders", t);
                Toast.makeText(OnTransferredActivity.this,
                        "Ошибка в загрузке", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showIncorrectDataError() {
        new AlertDialog.Builder(this)
                .setMessage("Введена некорректная дата")
                .setNeutralButton("Понятно", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .create()
                .show();
    }

    private void showTransferred(List<Transferred> transferreds) {
        transferredAdapter.setTransferreds(transferreds);
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
