package com.alonelegion.workproject01;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alonelegion.workproject01.adapters.Finance;
import com.alonelegion.workproject01.adapters.FinanceAdapter;
import com.alonelegion.workproject01.api.FinanceResponse;
import com.alonelegion.workproject01.api.FssService;
import com.alonelegion.workproject01.service.FssApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForFinanceActivity extends AppCompatActivity {

    private static final String TAG = ForFinanceActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private FinanceAdapter financeAdapter;
    private String token;

    private EditText dateStartView;
    private EditText dateEndView;
    private Calendar dateStart = Calendar.getInstance();
    private Calendar dateEnd = Calendar.getInstance();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_finance);
        setUpToolbar();
        setUpDateFilter();
        setUpRecyclerView();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        recyclerView = findViewById(R.id.for_finance_list);
        token = prefs.getString("token", null);
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

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.for_finance_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        financeAdapter = new FinanceAdapter(new FinanceAdapter.OnFinanceClickListener() {
            @Override
            public void onFinanceClick(Finance finance) {
                FinanceDetailActivity.launch(ForFinanceActivity.this, finance);
            }
        });
        recyclerView.setAdapter(financeAdapter);
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
                loadFinance(dateStart, dateEnd);
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

    private void loadFinance(Calendar dateStart, Calendar dateEnd) {

        if (dateStart.getTimeInMillis() > dateEnd.getTimeInMillis()) {
            showIncorrectDataError();
            return;
        }

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.for_finance_list);
        recyclerView.setVisibility(View.INVISIBLE);

        FssService fssService = ((FssApp) getApplication()).getFssService();
        fssService.finances(token, dateFormat.format(dateStart.getTime()), dateFormat.format(dateEnd.getTime())).enqueue(new Callback<FinanceResponse>() {
            @Override
            public void onResponse(Call<FinanceResponse> call, Response<FinanceResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                List<Finance> finances = response.body().getFinances();
                    showFinance(finances);
            }

            @Override
            public void onFailure(Call<FinanceResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.w(TAG, "Failed to load orders", t);
                Toast.makeText(ForFinanceActivity.this,
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

    private void showFinance(List<Finance> finances) {
        financeAdapter.setFinance(finances);
    }
}
