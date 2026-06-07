package com.phuc.datvekhachsan.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.adapter.AdminBookingAdapter;
import com.phuc.datvekhachsan.model.Booking;
import com.phuc.datvekhachsan.network.AdminApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminBookingListActivity extends AppCompatActivity {

    private RecyclerView rvAdminBookings;
    private ProgressBar progressBar;
    private AdminBookingAdapter adapter;
    private List<Booking> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        rvAdminBookings = findViewById(R.id.rvAdminBookings);
        progressBar = findViewById(R.id.progressBar);

        bookingList = new ArrayList<>();
        adapter = new AdminBookingAdapter(this, bookingList);
        rvAdminBookings.setLayoutManager(new LinearLayoutManager(this));
        rvAdminBookings.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        loadBookings();
    }

    private void loadBookings() {
        progressBar.setVisibility(View.VISIBLE);
        AdminApiService apiService = RetrofitClient.getClient(this).create(AdminApiService.class);
        apiService.getAllBookings().enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    bookingList.clear();
                    bookingList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminBookingListActivity.this, "Lỗi tải danh sách Booking", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminBookingListActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
