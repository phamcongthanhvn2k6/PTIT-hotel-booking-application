package com.phuc.datvekhachsan.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.adapter.HotelListAdapter;
import com.phuc.datvekhachsan.model.Hotel;
import com.phuc.datvekhachsan.network.ApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteHotelsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_hotels);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        recyclerView = findViewById(R.id.recyclerViewFavorites);
        progressBar = findViewById(R.id.progressBarFavorites);
        btnBack = findViewById(R.id.btnBackFavorites);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        apiService.getFavoriteHotels().enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                progressBar.setVisibility(View.GONE);
                View emptyState = findViewById(R.id.layoutEmptyState);
                if (response.isSuccessful() && response.body() != null) {
                    List<Hotel> hotels = response.body();
                    if (hotels.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyState.setVisibility(View.VISIBLE);
                        findViewById(R.id.btnGoHome).setOnClickListener(v -> {
                            android.content.Intent intent = new android.content.Intent(FavoriteHotelsActivity.this, MainActivity.class);
                            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });
                    } else {
                        emptyState.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(new HotelListAdapter(hotels, true));
                    }
                } else if (response.code() == 401 || response.code() == 403) {
                    Toast.makeText(FavoriteHotelsActivity.this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                    // Hiển thị luôn trạng thái trống nếu chưa đăng nhập
                    recyclerView.setVisibility(View.GONE);
                    emptyState.setVisibility(View.VISIBLE);
                    findViewById(R.id.btnGoHome).setOnClickListener(v -> {
                        android.content.Intent intent = new android.content.Intent(FavoriteHotelsActivity.this, MainActivity.class);
                        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    });
                } else {
                    Toast.makeText(FavoriteHotelsActivity.this, "Không thể tải danh sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FavoriteHotelsActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
