package com.phuc.datvekhachsan.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.adapter.AdminRoomAdapter;
import com.phuc.datvekhachsan.model.Room;
import com.phuc.datvekhachsan.network.AdminApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRoomListActivity extends AppCompatActivity {

    private RecyclerView rvAdminRooms;
    private ProgressBar progressBar;
    private AdminRoomAdapter adapter;
    private List<Room> roomList;
    private Long hotelId = -1L;
    private String hotelName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_room_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if (getIntent().hasExtra("HOTEL_ID")) {
            hotelId = getIntent().getLongExtra("HOTEL_ID", -1);
            hotelName = getIntent().getStringExtra("HOTEL_NAME");
            ((TextView) findViewById(R.id.txtHotelTitle)).setText("Phòng: " + hotelName);
        }

        rvAdminRooms = findViewById(R.id.rvAdminRooms);
        progressBar = findViewById(R.id.progressBar);

        roomList = new ArrayList<>();
        adapter = new AdminRoomAdapter(this, roomList);
        rvAdminRooms.setLayoutManager(new LinearLayoutManager(this));
        rvAdminRooms.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnAddRoom).setOnClickListener(v -> {
            Toast.makeText(this, "Thêm phòng", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, AdminRoomEditorActivity.class);
            // intent.putExtra("HOTEL_ID", hotelId);
            // startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hotelId != -1) {
            loadRooms();
        }
    }

    private void loadRooms() {
        progressBar.setVisibility(View.VISIBLE);
        AdminApiService apiService = RetrofitClient.getClient(this).create(AdminApiService.class);
        apiService.getRoomsByHotel(hotelId).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    roomList.clear();
                    roomList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminRoomListActivity.this, "Lỗi tải danh sách phòng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminRoomListActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
