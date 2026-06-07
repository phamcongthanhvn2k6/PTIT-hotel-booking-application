package com.phuc.datvekhachsan.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;

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

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        
        findViewById(R.id.btnMenu).setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        initNavigation();

        initBanner();
        initPopularHotels();
        initRecommendedHotels();
        initSearch();
    }

    private void initNavigation() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_profile) {
                if (com.phuc.datvekhachsan.util.AuthManager.isLoggedIn(this)) {
                    startActivity(new android.content.Intent(MainActivity.this, ProfileActivity.class));
                } else {
                    startActivity(new android.content.Intent(MainActivity.this, LoginActivity.class));
                }
            } else if (id == R.id.nav_favorites) {
                if (com.phuc.datvekhachsan.util.AuthManager.isLoggedIn(this)) {
                    startActivity(new android.content.Intent(MainActivity.this, FavoriteHotelsActivity.class));
                } else {
                    startActivity(new android.content.Intent(MainActivity.this, LoginActivity.class));
                }
            } else if (id == R.id.nav_history) {
                if (com.phuc.datvekhachsan.util.AuthManager.isLoggedIn(this)) {
                    startActivity(new android.content.Intent(MainActivity.this, com.phuc.datvekhachsan.activity.BookingHistoryActivity.class));
                } else {
                    startActivity(new android.content.Intent(MainActivity.this, LoginActivity.class));
                }
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Cài đặt", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("Xác nhận đăng xuất")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                        .setPositiveButton("Đăng xuất", (dialog, which) -> {
                            com.phuc.datvekhachsan.util.AuthManager.logout(MainActivity.this);
                            updateLoginState();
                            drawerLayout.closeDrawer(GravityCompat.START);
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
            return true;
        });
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
        
        View headerView = navigationView.getHeaderView(0);
        android.widget.TextView navHeaderName = headerView.findViewById(R.id.navHeaderName);
        android.widget.TextView navHeaderEmail = headerView.findViewById(R.id.navHeaderEmail);

        if (com.phuc.datvekhachsan.util.AuthManager.isLoggedIn(this)) {
            String fullName = com.phuc.datvekhachsan.util.AuthManager.getFullName(this);
            String username = com.phuc.datvekhachsan.util.AuthManager.getUsername(this);
            textViewGreeting.setText(getString(R.string.greeting_user_format, fullName));
            
            // Ẩn nút đăng xuất và lịch sử trên header cho gọn
            btnLoginRegister.setVisibility(View.GONE);
            btnBookingHistory.setVisibility(View.GONE);
            
            navHeaderName.setText(fullName);
            navHeaderEmail.setText(username);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_history).setVisible(true);
        } else {
            textViewGreeting.setText(R.string.greeting_guest);
            btnLoginRegister.setText(R.string.login);
            btnLoginRegister.setVisibility(View.VISIBLE);
            btnLoginRegister.setOnClickListener(v -> {
                startActivity(new android.content.Intent(MainActivity.this, LoginActivity.class));
            });
            btnBookingHistory.setVisibility(View.GONE);
            
            navHeaderName.setText("Khách");
            navHeaderEmail.setText("Vui lòng đăng nhập");
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_history).setVisible(false);
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
