package com.phuc.datvekhachsan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Booking;
import com.phuc.datvekhachsan.network.AdminApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminBookingAdapter extends RecyclerView.Adapter<AdminBookingAdapter.BookingViewHolder> {

    private Context context;
    private List<Booking> bookingList;

    public AdminBookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        
        String roomDisplay = booking.getRoom() != null && booking.getRoom().getName() != null 
                ? booking.getRoom().getName() : String.valueOf(booking.getRoomId());
        String userDisplay = booking.getUser() != null && booking.getUser().getFullName() != null 
                ? booking.getUser().getFullName() : String.valueOf(booking.getUserId());
                
        holder.txtBookingHotel.setText("Phòng: " + roomDisplay + " (Khách: " + userDisplay + ")");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        String dates = "";
        if (booking.getCheckInDateObj() != null && booking.getCheckOutDateObj() != null) {
            dates = sdf.format(booking.getCheckInDateObj()) + " - " + sdf.format(booking.getCheckOutDateObj());
        }
        holder.txtBookingDates.setText(dates);
        
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
        holder.txtBookingPrice.setText("Tổng tiền: " + formatter.format(booking.getTotalPrice()) + "đ");

        holder.txtBookingStatus.setText(booking.getStatus());
        if ("COMPLETED".equals(booking.getStatus())) {
            holder.txtBookingStatus.setTextColor(Color.parseColor("#05CD99"));
            holder.txtBookingStatus.setBackgroundColor(Color.parseColor("#E6FAF5"));
            holder.btnChangeStatus.setVisibility(View.GONE);
        } else if ("CANCELLED".equals(booking.getStatus())) {
            holder.txtBookingStatus.setTextColor(Color.parseColor("#EE5D50"));
            holder.txtBookingStatus.setBackgroundColor(Color.parseColor("#FDEFEF"));
            holder.btnChangeStatus.setVisibility(View.GONE);
        } else {
            holder.txtBookingStatus.setTextColor(Color.parseColor("#FFB547"));
            holder.txtBookingStatus.setBackgroundColor(Color.parseColor("#FFF9F1"));
            holder.btnChangeStatus.setVisibility(View.VISIBLE);
        }

        holder.btnChangeStatus.setOnClickListener(v -> {
            String[] options = {"COMPLETED (Duyệt)", "CANCELLED (Hủy)"};
            new AlertDialog.Builder(context)
                    .setTitle("Cập nhật trạng thái")
                    .setItems(options, (dialog, which) -> {
                        String newStatus = which == 0 ? "COMPLETED" : "CANCELLED";
                        updateStatus(booking, newStatus, position);
                    })
                    .show();
        });
    }

    private void updateStatus(Booking booking, String newStatus, int position) {
        AdminApiService apiService = RetrofitClient.getClient(context).create(AdminApiService.class);
        JsonObject request = new JsonObject();
        request.addProperty("status", newStatus);

        apiService.updateBookingStatus(booking.getId(), request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    booking.setStatus(newStatus);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi cập nhật trạng thái", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView txtBookingHotel, txtBookingDates, txtBookingPrice, txtBookingStatus;
        Button btnChangeStatus;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBookingHotel = itemView.findViewById(R.id.txtBookingHotel);
            txtBookingDates = itemView.findViewById(R.id.txtBookingDates);
            txtBookingPrice = itemView.findViewById(R.id.txtBookingPrice);
            txtBookingStatus = itemView.findViewById(R.id.txtBookingStatus);
            btnChangeStatus = itemView.findViewById(R.id.btnChangeStatus);
        }
    }
}
