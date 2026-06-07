package com.phuc.datvekhachsan.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.adapter.AdminHotelAdapter;
import com.phuc.datvekhachsan.model.Hotel;
import com.phuc.datvekhachsan.network.AdminApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHotelListActivity extends AppCompatActivity {

    private RecyclerView rvAdminHotels;
    private ProgressBar progressBar;
    private AdminHotelAdapter adapter;
    private List<Hotel> hotelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hotel_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        rvAdminHotels = findViewById(R.id.rvAdminHotels);
        progressBar = findViewById(R.id.progressBar);

        hotelList = new ArrayList<>();
        adapter = new AdminHotelAdapter(this, hotelList);
        rvAdminHotels.setLayoutManager(new LinearLayoutManager(this));
        rvAdminHotels.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnAddHotel).setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminHotelEditorActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHotels();
    }

    private void loadHotels() {
        progressBar.setVisibility(View.VISIBLE);
        AdminApiService apiService = RetrofitClient.getClient(this).create(AdminApiService.class);
        apiService.getAllHotels().enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    hotelList.clear();
                    hotelList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminHotelListActivity.this, "Lỗi tải danh sách khách sạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminHotelListActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
