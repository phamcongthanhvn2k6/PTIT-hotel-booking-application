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
            if (selectedId == -1) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            btnConfirm.setEnabled(false);
            btnConfirm.setText("Đang xử lý...");

            String method = "Thanh toán tại nơi check-in";
            if (selectedId == R.id.rbPayOnline) {
                method = "Thanh toán online";
            }

            // Call API to save booking
            com.google.gson.JsonObject request = new com.google.gson.JsonObject();
            
            com.google.gson.JsonArray roomIdsArray = new com.google.gson.JsonArray();
            if (booking.getRoomIds() != null) {
                for (Long rId : booking.getRoomIds()) {
                    roomIdsArray.add(rId);
                }
            } else if (booking.getRoomId() != null) {
                roomIdsArray.add(booking.getRoomId());
            }
            request.add("roomIds", roomIdsArray);
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            request.addProperty("checkInDate", sdf.format(booking.getCheckInDateObj()));
            request.addProperty("checkOutDate", sdf.format(booking.getCheckOutDateObj()));
            request.addProperty("totalPrice", booking.getTotalPrice());

            final String finalMethod = method;

            com.phuc.datvekhachsan.network.ApiService apiService = com.phuc.datvekhachsan.network.RetrofitClient.getClient(this).create(com.phuc.datvekhachsan.network.ApiService.class);
            apiService.createBooking(request).enqueue(new retrofit2.Callback<com.google.gson.JsonObject>() {
                @Override
                public void onResponse(retrofit2.Call<com.google.gson.JsonObject> call, retrofit2.Response<com.google.gson.JsonObject> response) {
                    if (response.isSuccessful()) {
                        String username = com.phuc.datvekhachsan.util.AuthManager.getUsername(PaymentActivity.this);
                        com.phuc.datvekhachsan.util.BookingManager.addBooking(PaymentActivity.this, booking, username);

                        Intent intent = new Intent(PaymentActivity.this, PaymentSuccessActivity.class);
                        intent.putExtra("booking", booking);
                        intent.putExtra("payment_method", finalMethod);
                        startActivity(intent);
                        finish(); // Close this payment activity
                    } else {
                        btnConfirm.setEnabled(true);
                        btnConfirm.setText("Xác nhận thanh toán");
                        try {
                            String errorStr = response.errorBody() != null ? response.errorBody().string() : "Lỗi không xác định";
                            if (errorStr.contains("\"message\"")) {
                                com.google.gson.JsonObject errorObj = com.google.gson.JsonParser.parseString(errorStr).getAsJsonObject();
                                errorStr = errorObj.get("message").getAsString();
                            }
                            Toast.makeText(PaymentActivity.this, errorStr, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(PaymentActivity.this, "Lỗi đặt phòng trên hệ thống", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<com.google.gson.JsonObject> call, Throwable t) {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setText("Xác nhận thanh toán");
                    Toast.makeText(PaymentActivity.this, "Lỗi kết nối mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
