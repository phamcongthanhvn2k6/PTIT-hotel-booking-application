package com.phuc.datvekhachsan.activity;

import android.os.Bundle;
import android.view.WindowManager;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.adapter.CheckInDateAdapter;
import com.phuc.datvekhachsan.adapter.RoomGridAdapter;
import com.phuc.datvekhachsan.adapter.RoomTypeAdapter;
import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Hotel;
import com.phuc.datvekhachsan.model.Room;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RoomBookingActivity extends AppCompatActivity {
    private Hotel hotel;
    private CheckInDateAdapter checkInDateAdapter;
    private RoomTypeAdapter roomTypeAdapter;
    private RoomGridAdapter roomGridAdapter;
    private int selectedRoomCount = 0;
    private String selectedRoomNamesStr = "";
    private TextView userGreeting;
    private Button loginToggleBtn;

    private List<Room> allRooms = new ArrayList<>();
    private List<Room> filteredRooms = new ArrayList<>();
    private com.google.android.material.chip.ChipGroup chipGroupFloor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_booking);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        hotel = (Hotel) getIntent().getSerializableExtra("hotel");
        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        // Setup user greeting and quick login/logout
        userGreeting = findViewById(R.id.userGreeting);
        loginToggleBtn = findViewById(R.id.loginToggleBtn);
        loginToggleBtn.setOnClickListener(v -> {
            if (com.phuc.datvekhachsan.util.AuthManager.isLoggedIn(this)) {
                com.phuc.datvekhachsan.util.AuthManager.logout(this);
                updateUserViews(userGreeting, loginToggleBtn);
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        initDateSelection();
        initRoomTypes();
        initRoomGrid();
        initFloorFilter();

        Button confirmBtn = findViewById(R.id.confirmBookingBtn);
        confirmBtn.setOnClickListener(v -> {
            // If not logged in, redirect to login
            if (!com.phuc.datvekhachsan.util.AuthManager.isLoggedIn(this)) {
                Toast.makeText(this, getString(R.string.login_required_to_book), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }

            if (checkInDateAdapter == null || !checkInDateAdapter.hasSelection()) {
                Toast.makeText(this, "Vui lòng chọn ngày nhận phòng!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (roomTypeAdapter == null || !roomTypeAdapter.hasSelection()) {
                Toast.makeText(this, "Vui lòng chọn loại phòng!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedRoomCount == 0) {
                Toast.makeText(this, "Vui lòng chọn ít nhất một phòng cụ thể!", Toast.LENGTH_SHORT).show();
                return;
            }

            String hotelName = hotel != null ? hotel.getName() : "Khách sạn";
            int imageRes = hotel != null ? hotel.getImageResId() : R.drawable.hotel;
            String location = hotel != null ? hotel.getLocation() : "Việt Nam";
            String rType = roomTypeAdapter.getSelectedRoomType();
            String date = checkInDateAdapter.getSelectedDate();
            double pricePerRoom = hotel != null ? hotel.getPricePerNight() : 1000000;
            double total = selectedRoomCount * pricePerRoom;
            String username = com.phuc.datvekhachsan.util.AuthManager.getUsername(this);

            com.phuc.datvekhachsan.model.Booking booking = new com.phuc.datvekhachsan.model.Booking(
                    hotelName, imageRes, location, selectedRoomNamesStr, rType, date, total, System.currentTimeMillis()
            );

            // Save booking history
            com.phuc.datvekhachsan.util.BookingManager.addBooking(this, booking, username);

            // Show success dialog
            showSuccessDialog(booking);
        });
    }

    private void updateUserViews(TextView userGreeting, Button loginToggleBtn) {
        if (com.phuc.datvekhachsan.util.AuthManager.isLoggedIn(this)) {
            String name = com.phuc.datvekhachsan.util.AuthManager.getFullName(this);
            userGreeting.setText(getString(R.string.greeting_user_format, name));
            loginToggleBtn.setText(getString(R.string.logout));
        } else {
            userGreeting.setText(getString(R.string.greeting_guest));
            loginToggleBtn.setText(getString(R.string.login));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserViews(userGreeting, loginToggleBtn);
        // Update confirm button label based on login state
        Button confirmBtn = findViewById(R.id.confirmBookingBtn);
        if (com.phuc.datvekhachsan.util.AuthManager.isLoggedIn(this)) {
            confirmBtn.setText(getString(R.string.confirm_booking));
        } else {
            confirmBtn.setText(getString(R.string.login_to_book));
        }
    }

    private void initDateSelection() {
        RecyclerView dateRecycler = findViewById(R.id.dateRecyclerview);
        dateRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<String> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE/dd/MMM");
        for (int i = 0; i < 14; i++) {
            dates.add(today.plusDays(i).format(formatter));
        }
        checkInDateAdapter = new CheckInDateAdapter(dates);
        dateRecycler.setAdapter(checkInDateAdapter);
    }

    private void initRoomTypes() {
        RecyclerView roomTypeRecycler = findViewById(R.id.roomTypeRecyclerview);
        roomTypeRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<String> types = Arrays.asList(
                "Standard", "Superior", "Deluxe", "Suite", "VIP Suite", "Penthouse"
        );
        roomTypeAdapter = new RoomTypeAdapter(types);
        roomTypeRecycler.setAdapter(roomTypeAdapter);
    }

    private void initRoomGrid() {
        RecyclerView roomRecycler = findViewById(R.id.roomRecyclerview);
        roomRecycler.setLayoutManager(new LinearLayoutManager(this));
        roomRecycler.setNestedScrollingEnabled(false);

        TextView totalPriceTxt = findViewById(R.id.totalPriceTxt);
        TextView numberSelectedTxt = findViewById(R.id.numberSelectedTxt);

        allRooms.clear();
        // Generate 50 rooms across 10 floors (5 rooms per floor)
        for (int floor = 1; floor <= 10; floor++) {
            for (int roomNum = 1; roomNum <= 5; roomNum++) {
                String roomName = "Phòng " + (floor * 100 + roomNum);
                Room.RoomStatus status;
                if ((floor + roomNum) % 3 == 0) {
                    status = Room.RoomStatus.UNAVAILABLE;
                } else {
                    status = Room.RoomStatus.AVAILABLE;
                }
                allRooms.add(new Room(status, roomName));
            }
        }

        filteredRooms.clear();
        filteredRooms.addAll(allRooms);

        double pricePerRoom = (hotel != null) ? hotel.getPricePerNight() : 1000000;
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        roomGridAdapter = new RoomGridAdapter(filteredRooms, (selectedNames, count) -> {
            selectedRoomNamesStr = selectedNames;
            selectedRoomCount = count;
            numberSelectedTxt.setText(getString(R.string.selected_rooms_count_format, count));
            double total = count * pricePerRoom;
            totalPriceTxt.setText(getString(R.string.price_currency_format, formatter.format(total)));
        });
        roomRecycler.setAdapter(roomGridAdapter);
    }

    private void initFloorFilter() {
        chipGroupFloor = findViewById(R.id.chipGroupFloor);
        if (chipGroupFloor == null) return;

        chipGroupFloor.setOnCheckedStateChangeListener((group, checkedIds) -> {
            int checkedId = group.getCheckedChipId();
            if (checkedId != View.NO_ID) {
                com.google.android.material.chip.Chip chip = group.findViewById(checkedId);
                if (chip != null && chip.getTag() != null) {
                    int index = (int) chip.getTag();
                    applyFloorFilterByIndex(index);
                }
            } else {
                applyFloorFilterByIndex(0);
            }
        });

        String[] floorFilters = new String[]{
                "Tất cả tầng",
                "Tầng 1 - 3",
                "Tầng 4 - 6",
                "Tầng 7 - 10"
        };

        for (int i = 0; i < floorFilters.length; i++) {
            com.google.android.material.chip.Chip chip = new com.google.android.material.chip.Chip(this);
            chip.setText(floorFilters[i]);
            chip.setCheckable(true);
            chip.setId(View.generateViewId());
            chip.setTag(i);
            chipGroupFloor.addView(chip);
            if (i == 0) {
                chip.setChecked(true);
            }
        }
    }

    private int getFloorFromRoomName(String roomName) {
        try {
            String numStr = roomName.replace("Phòng", "").trim();
            int num = Integer.parseInt(numStr);
            return num / 100;
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private void applyFloorFilterByIndex(int index) {
        filteredRooms.clear();
        if (index == 0) {
            filteredRooms.addAll(allRooms);
        } else if (index == 1) {
            for (Room r : allRooms) {
                int floor = getFloorFromRoomName(r.getName());
                if (floor >= 1 && floor <= 3) {
                    filteredRooms.add(r);
                }
            }
        } else if (index == 2) {
            for (Room r : allRooms) {
                int floor = getFloorFromRoomName(r.getName());
                if (floor >= 4 && floor <= 6) {
                    filteredRooms.add(r);
                }
            }
        } else if (index == 3) {
            for (Room r : allRooms) {
                int floor = getFloorFromRoomName(r.getName());
                if (floor >= 7 && floor <= 10) {
                    filteredRooms.add(r);
                }
            }
        }
        if (roomGridAdapter != null) {
            roomGridAdapter.updateRooms(filteredRooms);
        }
    }

    private void showSuccessDialog(com.phuc.datvekhachsan.model.Booking booking) {
        android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_booking_success, null);
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView tvHotel = dialogView.findViewById(R.id.dialogHotelName);
        TextView tvLoc = dialogView.findViewById(R.id.dialogLocation);
        TextView tvDate = dialogView.findViewById(R.id.dialogCheckInDate);
        TextView tvType = dialogView.findViewById(R.id.dialogRoomType);
        TextView tvRooms = dialogView.findViewById(R.id.dialogRoomsSelected);
        TextView tvPrice = dialogView.findViewById(R.id.dialogTotalPrice);
        View btnHome = dialogView.findViewById(R.id.btnGoHome);

        tvHotel.setText(booking.getHotelName());
        tvLoc.setText("📍 " + booking.getHotelLocation());
        tvDate.setText(booking.getCheckInDate());
        tvType.setText(booking.getRoomType());
        tvRooms.setText(booking.getRoomNames());

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvPrice.setText(getString(R.string.price_currency_format, formatter.format(booking.getTotalPrice())));

        btnHome.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(RoomBookingActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }
}
