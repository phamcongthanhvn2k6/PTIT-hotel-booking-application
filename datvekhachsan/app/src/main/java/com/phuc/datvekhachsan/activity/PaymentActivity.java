package com.phuc.datvekhachsan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Booking;

import java.text.NumberFormat;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private Booking booking;
    private RadioGroup rgPaymentMethods;
    private RadioButton rbPayAtCheckIn, rbPayOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        booking = (Booking) getIntent().getSerializableExtra("booking");
        if (booking == null) {
            finish();
            return;
        }

        findViewById(R.id.btnBackPayment).setOnClickListener(v -> finish());

        TextView tvTotal = findViewById(R.id.tvPaymentTotal);
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvTotal.setText(getString(R.string.price_currency_format, formatter.format(booking.getTotalPrice())));

        rgPaymentMethods = findViewById(R.id.rgPaymentMethods);
        rbPayAtCheckIn = findViewById(R.id.rbPayAtCheckIn);
        rbPayOnline = findViewById(R.id.rbPayOnline);

        Button btnConfirm = findViewById(R.id.btnConfirmPayment);
        btnConfirm.setOnClickListener(v -> {
            int selectedId = rgPaymentMethods.getCheckedRadioButtonId();
            String method = "";
            if (selectedId == R.id.rbPayAtCheckIn) {
                method = "Thanh toán tại nơi check-in";
            } else if (selectedId == R.id.rbPayOnline) {
                method = "Thanh toán online";
                Toast.makeText(this, "Chức năng thanh toán online sẽ được cập nhật sau", Toast.LENGTH_SHORT).show();
                // We can still allow them to proceed or block them. The prompt says "sẽ bổ sung sau", meaning it's a placeholder.
                // Let's just pretend it succeeds or we can force them to use check-in for now.
                // Let's allow them to proceed with "Thanh toán online" as a mock.
            }

            // Save booking history since payment is confirmed
            String username = com.phuc.datvekhachsan.util.AuthManager.getUsername(this);
            com.phuc.datvekhachsan.util.BookingManager.addBooking(this, booking, username);

            Intent intent = new Intent(this, PaymentSuccessActivity.class);
            intent.putExtra("booking", booking);
            intent.putExtra("payment_method", method);
            startActivity(intent);
            finish(); // Close this payment activity
        });
    }
}
