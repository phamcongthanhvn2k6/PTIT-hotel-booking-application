package com.phuc.datvekhachsan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Booking;

import java.text.NumberFormat;
import java.util.Locale;

public class PaymentSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Booking booking = (Booking) getIntent().getSerializableExtra("booking");
        String method = getIntent().getStringExtra("payment_method");

        if (booking != null) {
            TextView tvHotel = findViewById(R.id.tvSuccessHotelName);
            TextView tvDate = findViewById(R.id.tvSuccessDate);
            TextView tvRoom = findViewById(R.id.tvSuccessRoom);
            TextView tvTotal = findViewById(R.id.tvSuccessTotal);
            TextView tvMethod = findViewById(R.id.tvSuccessMethod);

            tvHotel.setText(booking.getHotelName());
            tvDate.setText("Ngày check-in: " + booking.getCheckInDate());
            tvRoom.setText("Phòng: " + booking.getRoomNames() + " (" + booking.getRoomType() + ")");
            
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvTotal.setText(getString(R.string.price_currency_format, formatter.format(booking.getTotalPrice())));
            
            if (method != null) {
                tvMethod.setText("Phương thức: " + method);
            }
        }

        Button btnHome = findViewById(R.id.btnSuccessHome);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to payment page
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
