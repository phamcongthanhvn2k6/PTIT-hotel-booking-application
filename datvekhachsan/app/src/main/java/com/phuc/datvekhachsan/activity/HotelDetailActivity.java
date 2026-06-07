package com.phuc.datvekhachsan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.adapter.AmenityTagAdapter;
import com.phuc.datvekhachsan.adapter.FacilityAdapter;
import com.phuc.datvekhachsan.adapter.DetailImageSliderAdapter;
import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Hotel;
import androidx.viewpager2.widget.ViewPager2;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.google.gson.JsonObject;
import com.phuc.datvekhachsan.network.ApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;

public class HotelDetailActivity extends AppCompatActivity {
    private boolean isFavorite = false;
    private ImageView btnFavorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Hotel hotel = (Hotel) getIntent().getSerializableExtra("hotel");
        if (hotel == null) return;

        setupViews(hotel);
    }

    private void setupViews(Hotel hotel) {
        ViewPager2 hotelImageSlider = findViewById(R.id.hotelImageSlider);
        TextView hotelNameTxt = findViewById(R.id.hotelNameTxt);
        TextView ratingTxt = findViewById(R.id.ratingTxt);
        TextView locationTxt = findViewById(R.id.locationTxt);
        TextView priceTxt = findViewById(R.id.priceTxt);
        TextView descriptionTxt = findViewById(R.id.descriptionTxt);
        RecyclerView amenitiesView = findViewById(R.id.amenitiesView);
        RecyclerView facilitiesView = findViewById(R.id.facilitiesView);

        // Set data
        if (hotel.getImageUrl() != null && !hotel.getImageUrl().isEmpty()) {
            java.util.List<Object> imgs = java.util.Collections.singletonList(hotel.getImageUrl());
            hotelImageSlider.setAdapter(new DetailImageSliderAdapter(imgs));
        } else if (hotel.getImageResIds() != null && !hotel.getImageResIds().isEmpty()) {
            java.util.List<Object> imgs = new java.util.ArrayList<>(hotel.getImageResIds());
            hotelImageSlider.setAdapter(new DetailImageSliderAdapter(imgs));
        }
        hotelNameTxt.setText(hotel.getName());
        ratingTxt.setText(String.valueOf(hotel.getRating()));
        locationTxt.setText(getString(R.string.location_with_pin, hotel.getLocation()));

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        priceTxt.setText(getString(R.string.price_per_night_format, formatter.format(hotel.getPricePerNight())));

        descriptionTxt.setText(hotel.getDescription());

        // Back button
        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        // Book now
        findViewById(R.id.bookNowBtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, RoomBookingActivity.class);
            intent.putExtra("hotel", hotel);
            startActivity(intent);
        });

        // Amenities tags
        if (hotel.getAmenities() != null) {
            amenitiesView.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            amenitiesView.setAdapter(new AmenityTagAdapter(hotel.getAmenities()));
        }

        // Facilities
        if (hotel.getFacilities() != null) {
            facilitiesView.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            facilitiesView.setAdapter(new FacilityAdapter(hotel.getFacilities()));
        }

        // Map Button
        findViewById(R.id.btnMap).setOnClickListener(v -> {
            String uriStr = "geo:0,0?q=" + android.net.Uri.encode(hotel.getLocation());
            if (hotel.getLatitude() != null && hotel.getLongitude() != null) {
                uriStr = "geo:" + hotel.getLatitude() + "," + hotel.getLongitude() + "?q=" + android.net.Uri.encode(hotel.getName());
            }
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(uriStr));
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                android.content.Intent fallbackIntent = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(uriStr));
                startActivity(fallbackIntent);
            }
        });

        // Favorite Button
        btnFavorite = findViewById(R.id.btnFavorite);
        if (com.phuc.datvekhachsan.util.AuthManager.isLoggedIn(this)) {
            checkFavoriteStatus(hotel.getId());
            btnFavorite.setOnClickListener(v -> toggleFavorite(hotel.getId()));
        } else {
            btnFavorite.setOnClickListener(v -> {
                Toast.makeText(this, "Vui lòng đăng nhập để lưu khách sạn yêu thích", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            });
        }
    }

    private void checkFavoriteStatus(Long hotelId) {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        apiService.getFavoriteHotels().enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Hotel h : response.body()) {
                        if (h.getId().equals(hotelId)) {
                            isFavorite = true;
                            updateFavoriteUI();
                            return;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable t) {}
        });
    }

    private void toggleFavorite(Long hotelId) {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        if (isFavorite) {
            apiService.removeFavoriteHotel(hotelId).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        isFavorite = false;
                        updateFavoriteUI();
                        Toast.makeText(HotelDetailActivity.this, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {}
            });
        } else {
            apiService.addFavoriteHotel(hotelId).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        isFavorite = true;
                        updateFavoriteUI();
                        Toast.makeText(HotelDetailActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {}
            });
        }
    }

    private void updateFavoriteUI() {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }
    }
}

