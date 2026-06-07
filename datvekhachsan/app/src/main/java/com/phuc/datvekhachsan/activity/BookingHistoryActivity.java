package com.phuc.datvekhachsan.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.adapter.BookingHistoryAdapter;
import com.phuc.datvekhachsan.model.Booking;
import com.phuc.datvekhachsan.util.AuthManager;
import com.phuc.datvekhachsan.util.BookingManager;

import java.util.List;

public class BookingHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.bookingHistoryRecycler);
        View emptyState = findViewById(R.id.emptyStateLayout);

        com.phuc.datvekhachsan.network.ApiService apiService = com.phuc.datvekhachsan.network.RetrofitClient.getClient(this).create(com.phuc.datvekhachsan.network.ApiService.class);
        apiService.getMyHistory().enqueue(new retrofit2.Callback<List<Booking>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Booking>> call, retrofit2.Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Booking> bookings = response.body();
                    if (bookings.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyState.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyState.setVisibility(View.GONE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(BookingHistoryActivity.this));
                        recyclerView.setAdapter(new com.phuc.datvekhachsan.adapter.BookingHistoryAdapter(bookings));
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyState.setVisibility(View.VISIBLE);
                    android.widget.Toast.makeText(BookingHistoryActivity.this, "Không thể lấy lịch sử", android.widget.Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Booking>> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
                android.widget.Toast.makeText(BookingHistoryActivity.this, "Lỗi mạng: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
}
