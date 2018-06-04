package com.alonelegion.workproject01;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alonelegion.workproject01.adapters.WorkOrder;
import com.alonelegion.workproject01.adapters.WorkOrdersAdapter;
import com.alonelegion.workproject01.api.FssService;
import com.alonelegion.workproject01.api.WorkOrdersResponse;
import com.alonelegion.workproject01.service.FssApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WorkOrdersActivity extends AppCompatActivity {

    private static final String TAG = WorkOrdersActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private WorkOrdersAdapter workOrdersAdapter;
    private String token;

    private EditText dateStartView;
    private EditText dateEndView;
    private Calendar dateStart = Calendar.getInstance();
    private Calendar dateEnd = Calendar.getInstance();

    private List<WorkOrder> orders;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_orders);
        setUpDateFilter();
        setUpRecyclerView();
        setUpToolbar();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        recyclerView = findViewById(R.id.workorders_list);
        token = prefs.getString("token", null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.work_orders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_work:
                openFilter();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openOrders() {
        Intent intent = new Intent(this, OrdersActivity.class);
        startActivity(intent);
    }

    private void openFilter() {
        if (orders == null || orders.isEmpty()) {
            Toast.makeText(this, R.string.error_no_orders, Toast.LENGTH_LONG).show();
            return;
        }
        HashSet<String> cities = new HashSet<>();
        HashSet<String> statuses = new HashSet<>();
        for (int i = 0; i < orders.size(); i++) {
            WorkOrder order = orders.get(i);
            cities.add(order.getCity());
            statuses.add(order.getStat());
        }
        FilterWorkActivity.start(this, cities, statuses);
    }

    // Получаем результат из фильтра
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == FilterWorkActivity.RC_FILTER) {
            String city = data.getStringExtra("city");
            String status = data.getStringExtra("status");
            filter(city, status);
        }
    }

    private void filter(String city, String status) {
        List<WorkOrder> filteredOrders = new ArrayList<>();
        for (WorkOrder order : orders) {
            if (order.getCity().equals(city) && order.getStat().equals(status)){
                filteredOrders.add(order);
            }
        }
        workOrdersAdapter.setWorkOrders(filteredOrders);
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
        recyclerView = findViewById(R.id.workorders_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        workOrdersAdapter = new WorkOrdersAdapter(new WorkOrdersAdapter.OnWorkOrderClickListener() {
            @Override
            public void onWorkOrderClick(WorkOrder workOrder) {
                WorkOrderDetailActivity.launch(WorkOrdersActivity.this, workOrder);
            }
        });
        recyclerView.setAdapter(workOrdersAdapter);
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
                loadOrders(dateStart, dateEnd);
            }
        });
    }

    private void showDatePickerDialog(Calendar date, DatePickerDialog.OnDateSetListener listener) {
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

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.workorders_list);
        recyclerView.setVisibility(View.INVISIBLE);

        FssService fssService = ((FssApp) getApplication()).getFssService();
        fssService.orders(token, dateFormat.format(dateStart.getTime()),
                dateFormat.format(dateEnd.getTime())).enqueue(
                new Callback<WorkOrdersResponse>() {
                    @Override
                    public void onResponse(Call<WorkOrdersResponse> call, Response<WorkOrdersResponse> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        orders = response.body().getWorkOrders();
                        if (!orders.isEmpty()) {
                            showOrders(orders);
                        }else {
                            showEmptyData();
                        }
                    }

                    @Override
                    public void onFailure(Call<WorkOrdersResponse> call, Throwable t) {
                        Log.w(TAG, "Failed to load orders", t);
                    }
                });
    }

    private void showEmptyData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkOrdersActivity.this);
        builder.setTitle("Заявок не найдено")
                .setMessage("За выбранный период заявок не обнаруженно")
                .setCancelable(true);
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

    private void showOrders(List<WorkOrder> orders) {
        workOrdersAdapter.setWorkOrders(orders);
    }

}
