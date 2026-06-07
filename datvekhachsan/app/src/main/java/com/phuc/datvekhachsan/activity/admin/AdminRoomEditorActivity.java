package com.phuc.datvekhachsan.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Room;
import com.phuc.datvekhachsan.network.AdminApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRoomEditorActivity extends AppCompatActivity {

    private EditText edtRoomNumber, edtRoomType, edtRoomPrice;
    private Spinner spinnerRoomStatus;
    private ProgressBar progressBar;
    private TextView txtTitle;
    private ImageView btnBack;
    private Button btnSaveRoom;

    private Long hotelId;
    private Long roomId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_room_editor);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        edtRoomNumber = findViewById(R.id.edtRoomNumber);
        edtRoomType = findViewById(R.id.edtRoomType);
        edtRoomPrice = findViewById(R.id.edtRoomPrice);
        spinnerRoomStatus = findViewById(R.id.spinnerRoomStatus);
        progressBar = findViewById(R.id.progressBar);
        txtTitle = findViewById(R.id.txtTitle);
        btnBack = findViewById(R.id.btnBack);
        btnSaveRoom = findViewById(R.id.btnSaveRoom);

        btnBack.setOnClickListener(v -> finish());
        btnSaveRoom.setOnClickListener(v -> saveRoom());

        // Setup Spinner
        String[] statuses = {"AVAILABLE", "BOOKED", "MAINTENANCE"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoomStatus.setAdapter(adapter);

        hotelId = getIntent().getLongExtra("HOTEL_ID", -1);
        
        if (getIntent().hasExtra("ROOM_ID")) {
            roomId = getIntent().getLongExtra("ROOM_ID", -1);
            if (roomId != -1) {
                txtTitle.setText("Sửa Phòng");
                
                String number = getIntent().getStringExtra("ROOM_NUMBER");
                String type = getIntent().getStringExtra("ROOM_TYPE");
                double price = getIntent().getDoubleExtra("ROOM_PRICE", 0);
                String status = getIntent().getStringExtra("ROOM_STATUS");
                
                if (number != null) edtRoomNumber.setText(number);
                if (type != null) edtRoomType.setText(type);
                if (price > 0) edtRoomPrice.setText(String.valueOf(price));
                if (status != null) {
                    for (int i = 0; i < statuses.length; i++) {
                        if (statuses[i].equals(status)) {
                            spinnerRoomStatus.setSelection(i);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void saveRoom() {
        String roomNumber = edtRoomNumber.getText().toString().trim();
        String roomType = edtRoomType.getText().toString().trim();
        String priceStr = edtRoomPrice.getText().toString().trim();
        String status = spinnerRoomStatus.getSelectedItem().toString();

        if (roomNumber.isEmpty() || roomType.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if ((roomId == null || roomId == -1) && hotelId == -1) {
            Toast.makeText(this, "Lỗi: Không xác định được khách sạn", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        progressBar.setVisibility(View.VISIBLE);

        Room room = new Room(Room.RoomStatus.valueOf(status), roomNumber);
        room.setRoomType(roomType);
        room.setPricePerNight(price);

        AdminApiService apiService = RetrofitClient.getClient(this).create(AdminApiService.class);
        Call<Room> call;

        if (roomId != null && roomId != -1) {
            call = apiService.updateRoom(roomId, room);
        } else {
            call = apiService.addRoom(hotelId, room);
        }

        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(AdminRoomEditorActivity.this, "Lưu phòng thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminRoomEditorActivity.this, "Lỗi lưu phòng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminRoomEditorActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
