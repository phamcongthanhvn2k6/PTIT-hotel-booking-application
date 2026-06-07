package com.phuc.datvekhachsan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Booking;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {
    private final List<Booking> bookings;

    public BookingHistoryAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView hotelImage;
        TextView hotelNameTxt, locationTxt, roomTypeTxt, roomsSelectedTxt, checkInDateTxt, bookingTimeTxt, totalPriceTxt;
        com.google.android.material.button.MaterialButton btnReview;

        public ViewHolder(View itemView) {
            super(itemView);
            hotelImage = itemView.findViewById(R.id.hotelImage);
            hotelNameTxt = itemView.findViewById(R.id.hotelNameTxt);
            locationTxt = itemView.findViewById(R.id.locationTxt);
            roomTypeTxt = itemView.findViewById(R.id.roomTypeTxt);
            roomsSelectedTxt = itemView.findViewById(R.id.roomsSelectedTxt);
            checkInDateTxt = itemView.findViewById(R.id.checkInDateTxt);
            bookingTimeTxt = itemView.findViewById(R.id.bookingTimeTxt);
            totalPriceTxt = itemView.findViewById(R.id.totalPriceTxt);
            btnReview = itemView.findViewById(R.id.btnReview);
        }

        void bind(Booking booking) {
            hotelImage.setImageResource(booking.getHotelImageResId());
            hotelNameTxt.setText(booking.getHotelName());
            locationTxt.setText(itemView.getContext().getString(R.string.location_with_pin, booking.getHotelLocation()));
            roomTypeTxt.setText("Loại phòng: " + booking.getRoomType());
            roomsSelectedTxt.setText("Phòng: " + booking.getRoomNames());
            checkInDateTxt.setText("📅 Nhận phòng: " + booking.getCheckInDate());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            bookingTimeTxt.setText("Đặt ngày: " + sdf.format(new Date(booking.getBookingTime())));

            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            totalPriceTxt.setText(itemView.getContext().getString(R.string.price_currency_format, formatter.format(booking.getTotalPrice())));

            // Logic to show/hide Review button
            if (booking.getCheckOutDateObj() != null && booking.getCheckOutDateObj().before(new Date())) {
                btnReview.setVisibility(View.VISIBLE);
                btnReview.setOnClickListener(v -> showReviewDialog(booking));
            } else {
                btnReview.setVisibility(View.GONE);
            }
        }

        private void showReviewDialog(Booking booking) {
            android.content.Context context = itemView.getContext();
            android.view.View dialogView = android.view.LayoutInflater.from(context).inflate(R.layout.dialog_review, null);
            androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setCancelable(false)
                    .create();

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            android.widget.RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
            android.widget.EditText commentInput = dialogView.findViewById(R.id.commentInput);
            View btnCancel = dialogView.findViewById(R.id.btnCancel);
            com.google.android.material.button.MaterialButton btnSubmitReview = dialogView.findViewById(R.id.btnSubmitReview);

            btnCancel.setOnClickListener(v -> dialog.dismiss());

            btnSubmitReview.setOnClickListener(v -> {
                String comment = commentInput.getText().toString().trim();
                int rating = (int) ratingBar.getRating();
                Long hotelId = booking.getHotelId();

                if (hotelId == null) {
                    android.widget.Toast.makeText(context, "Không xác định được khách sạn!", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

                btnSubmitReview.setEnabled(false);
                btnSubmitReview.setText("Đang gửi...");

                com.google.gson.JsonObject request = new com.google.gson.JsonObject();
                request.addProperty("hotelId", hotelId);
                request.addProperty("rating", rating);
                request.addProperty("comment", comment);

                com.phuc.datvekhachsan.network.ApiService apiService = com.phuc.datvekhachsan.network.RetrofitClient.getClient(context).create(com.phuc.datvekhachsan.network.ApiService.class);
                apiService.createReview(request).enqueue(new retrofit2.Callback<com.google.gson.JsonObject>() {
                    @Override
                    public void onResponse(retrofit2.Call<com.google.gson.JsonObject> call, retrofit2.Response<com.google.gson.JsonObject> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            android.widget.Toast.makeText(context, "Cảm ơn bạn đã đánh giá!", android.widget.Toast.LENGTH_SHORT).show();
                            btnReview.setText("Đã đánh giá");
                            btnReview.setEnabled(false);
                            btnReview.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.GRAY));
                        } else {
                            android.widget.Toast.makeText(context, "Lỗi khi gửi đánh giá", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<com.google.gson.JsonObject> call, Throwable t) {
                        dialog.dismiss();
                        android.widget.Toast.makeText(context, "Lỗi mạng: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                    }
                });
            });

            dialog.show();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(bookings.get(position));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }
}
