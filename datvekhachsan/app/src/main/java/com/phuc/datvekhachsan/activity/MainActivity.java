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
import com.phuc.datvekhachsan.model.Hotel;
import com.phuc.datvekhachsan.model.SliderItem;
import com.phuc.datvekhachsan.network.ApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;
import android.widget.Toast;

import java.util.ArrayList;
import com.phuc.datvekhachsan.model.Hotel;
import com.phuc.datvekhachsan.network.ApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        
        List<SliderItem> banners = new ArrayList<>();
        banners.add(new SliderItem(R.drawable.hotel));
        banners.add(new SliderItem(R.drawable.hotel));
        banners.add(new SliderItem(R.drawable.hotel));
        
        bannerRecyclerView.setAdapter(new BannerAdapter(banners));
        findViewById(R.id.progressBarSlider).setVisibility(View.GONE);
    }

    private void initPopularHotels() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPopularHotels);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        apiService.getHotels().enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Hotel> popularHotels = response.body().stream()
                            .filter(h -> h.getRating() >= 4.5)
                            .limit(5)
                            .collect(Collectors.toList());
                    if (popularHotels.isEmpty()) popularHotels = response.body();
                    recyclerView.setAdapter(new HotelListAdapter(popularHotels));
                } else {
                    Toast.makeText(MainActivity.this, "Không thể tải danh sách khách sạn phổ biến", Toast.LENGTH_SHORT).show();
                }
                findViewById(R.id.progressBarPopular).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối khi tải khách sạn phổ biến", Toast.LENGTH_SHORT).show();
                findViewById(R.id.progressBarPopular).setVisibility(View.GONE);
            }
        });

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
                
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        apiService.getHotels().enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Hotel> recommendedHotels = response.body().stream()
                            .filter(h -> h.getPricePerNight() < 2000000)
                            .limit(5)
                            .collect(Collectors.toList());
                    if (recommendedHotels.isEmpty()) recommendedHotels = response.body();
                    recyclerView.setAdapter(new HotelListAdapter(recommendedHotels));
                } else {
                    Toast.makeText(MainActivity.this, "Không thể tải danh sách khách sạn đề xuất", Toast.LENGTH_SHORT).show();
                }
                findViewById(R.id.progressBarRecommended).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối khi tải khách sạn đề xuất", Toast.LENGTH_SHORT).show();
                findViewById(R.id.progressBarRecommended).setVisibility(View.GONE);
            }
        });

        findViewById(R.id.btnViewAllRecommended).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, HotelListActivity.class);
            intent.putExtra("type", "recommended");
            startActivity(intent);
        });
    }
}
