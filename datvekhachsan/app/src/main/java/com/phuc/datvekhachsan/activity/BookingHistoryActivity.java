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

        String username = AuthManager.getUsername(this);
        List<Booking> bookings = BookingManager.getBookings(this, username);

        if (bookings.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new BookingHistoryAdapter(bookings));
        }
    }
}
