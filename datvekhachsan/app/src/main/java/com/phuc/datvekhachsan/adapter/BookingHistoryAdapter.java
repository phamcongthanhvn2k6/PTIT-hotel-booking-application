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
