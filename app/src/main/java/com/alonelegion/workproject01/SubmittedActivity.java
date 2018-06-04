package com.alonelegion.workproject01;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alonelegion.workproject01.adapters.Submitted;
import com.alonelegion.workproject01.adapters.SubmittedAdapter;
import com.alonelegion.workproject01.api.FssService;
import com.alonelegion.workproject01.api.SubmittedResponse;
import com.alonelegion.workproject01.service.FssApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SubmittedActivity extends AppCompatActivity {

    private static final String TAG = SubmittedActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private SubmittedAdapter submittedAdapter;
    private String token;

    private EditText dateStartView;
    private EditText dateEndView;
    private Calendar dateStart = Calendar.getInstance();
    private Calendar dateEnd = Calendar.getInstance();

    private TextView countOrders;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_submitted);
        setUpToolbar();
        setUpDateFilter();
        setUpRecyclerView();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        recyclerView = findViewById(R.id.submitted_list);
        countOrders = (TextView) findViewById(R.id.count_orders);
        token = prefs.getString("token", null);
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.submitted_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        submittedAdapter = new SubmittedAdapter(new SubmittedAdapter.OnSubmittedClicklListener() {
            @Override
            public void onSubmittedClick(Submitted submitted) {
                SubmittedDetailActivity.launch(SubmittedActivity.this, submitted);
            }
        });
        recyclerView.setAdapter(submittedAdapter);
    }

    private void setUpDateFilter() {
        dateStartView = findViewById(R.id.date_begin);
        dateEndView = findViewById(R.id.date_end);
        dateStartView.setText(dateFormat.format(dateStart.getTime()));
        dateEndView.setText(dateFormat.format(dateEnd.getTime()));
        dateStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePikerDialog(dateStart, new DatePickerDialog.OnDateSetListener() {
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
            public void onClick(View v) {
                showDatePikerDialog(dateEnd, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateEnd.set(year, month, dayOfMonth);
                        dateEndView.setText(dateFormat.format(dateEnd.getTime()));
                    }
                });
            }
        });
        findViewById(R.id.btn_check_orders).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                recyclerView.removeAllViewsInLayout();
                loadOrders(dateStart, dateEnd);

            }
        });
    }


    private void showDatePikerDialog(Calendar date, DatePickerDialog.OnDateSetListener listener) {
        new DatePickerDialog(
                this,
                listener,
                dateStart.get(Calendar.YEAR),
                dateStart.get(Calendar.MONTH),
                dateStart.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void loadOrders(Calendar dateStart, Calendar dateEnd) {

        if (dateStart.getTimeInMillis() > dateEnd.getTimeInMillis()) {
            showIncorrectDateError();
            return;
        }

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.submitted_list);
        recyclerView.setVisibility(View.INVISIBLE);

        FssService fssService = ((FssApp) getApplication()).getFssService();
        fssService.submitteds(token, dateFormat.format(dateStart.getTime()), 
                dateFormat.format(dateEnd.getTime())).enqueue(
                new Callback<SubmittedResponse>() {
                    @Override
                    public void onResponse(Call<SubmittedResponse> call, Response<SubmittedResponse> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        List<Submitted> submitteds = response.body().getSubmitteds();
                        if (!submitteds.isEmpty()) {
                            showSubmitteds(submitteds);
                            countOrders.setText(Integer.toString(submitteds.size()));
                        }else {
                            showEmptyData();
                        }
                    }

                    @Override
                    public void onFailure(Call<SubmittedResponse> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.v(TAG, "Ошибка в загрузке заявок", t);
                        Toast.makeText(SubmittedActivity.this,
                                "Ошибка в загрузке заявок", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showEmptyData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SubmittedActivity.this);
        builder.setTitle("Заявок не найдено")
                .setMessage("За выбранный период заявок не обнаруженно")
                .setCancelable(false);
        builder.setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showIncorrectDateError() {
        new AlertDialog.Builder(this)
                .setMessage("Введена некорректная дата")
                .setNeutralButton("Понятно", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .create()
                .show();
    }

    private void showSubmitteds(List<Submitted> submitteds) {
        submittedAdapter.setSubmitteds(submitteds);
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


