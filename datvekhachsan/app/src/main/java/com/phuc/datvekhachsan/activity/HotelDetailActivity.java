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
import java.util.Locale;

public class HotelDetailActivity extends AppCompatActivity {
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
        if (hotel.getImageResIds() != null && !hotel.getImageResIds().isEmpty()) {
            hotelImageSlider.setAdapter(new DetailImageSliderAdapter(hotel.getImageResIds()));
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
    }
}

