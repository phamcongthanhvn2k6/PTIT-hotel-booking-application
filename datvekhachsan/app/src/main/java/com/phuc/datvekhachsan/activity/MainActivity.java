package com.phuc.datvekhachsan.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.adapter.BannerAdapter;
import com.phuc.datvekhachsan.adapter.HotelListAdapter;
import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.data.MockData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        initBanner();
        initPopularHotels();
        initRecommendedHotels();
        initSearch();
    }

    private void initSearch() {
        android.widget.EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH ||
                actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                (event != null && event.getAction() == android.view.KeyEvent.ACTION_DOWN && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) {
                
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    android.content.Intent intent = new android.content.Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("query", query);
                    startActivity(intent);
                }
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLoginState();
    }

    private void updateLoginState() {
        android.widget.TextView textViewGreeting = findViewById(R.id.textViewGreeting);
        android.widget.TextView btnLoginRegister = findViewById(R.id.btnLoginRegister);
        android.view.View btnBookingHistory = findViewById(R.id.btnBookingHistory);

        if (com.phuc.datvekhachsan.util.AuthManager.isLoggedIn(this)) {
            String fullName = com.phuc.datvekhachsan.util.AuthManager.getFullName(this);
            textViewGreeting.setText(getString(R.string.greeting_user_format, fullName));
            btnLoginRegister.setText(R.string.logout);
            btnLoginRegister.setOnClickListener(v -> {
                com.phuc.datvekhachsan.util.AuthManager.logout(this);
                updateLoginState();
            });
            btnBookingHistory.setVisibility(View.VISIBLE);
            btnBookingHistory.setOnClickListener(v -> {
                startActivity(new android.content.Intent(MainActivity.this, com.phuc.datvekhachsan.activity.BookingHistoryActivity.class));
            });
        } else {
            textViewGreeting.setText(R.string.greeting_guest);
            btnLoginRegister.setText(R.string.login);
            btnLoginRegister.setOnClickListener(v -> {
                startActivity(new android.content.Intent(MainActivity.this, LoginActivity.class));
            });
            btnBookingHistory.setVisibility(View.GONE);
        }
    }

    private void initBanner() {
        RecyclerView bannerRecyclerView = findViewById(R.id.bannerRecyclerView);
        bannerRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bannerRecyclerView.setAdapter(new BannerAdapter(MockData.getBanners()));
        findViewById(R.id.progressBarSlider).setVisibility(View.GONE);
    }

    private void initPopularHotels() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPopularHotels);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new HotelListAdapter(MockData.getPopularHotels()));
        findViewById(R.id.progressBarPopular).setVisibility(View.GONE);

        findViewById(R.id.btnViewAllPopular).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, HotelListActivity.class);
            intent.putExtra("type", "popular");
            startActivity(intent);
        });
    }

    private void initRecommendedHotels() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRecommended);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new HotelListAdapter(MockData.getRecommendedHotels()));
        findViewById(R.id.progressBarRecommended).setVisibility(View.GONE);

        findViewById(R.id.btnViewAllRecommended).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, HotelListActivity.class);
            intent.putExtra("type", "recommended");
            startActivity(intent);
        });
    }
}
