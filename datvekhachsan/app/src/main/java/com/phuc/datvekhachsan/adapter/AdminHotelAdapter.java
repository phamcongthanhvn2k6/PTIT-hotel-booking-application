package com.phuc.datvekhachsan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Hotel;
import com.phuc.datvekhachsan.network.AdminApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;
// import com.phuc.datvekhachsan.activity.admin.AdminHotelEditorActivity;
// import com.phuc.datvekhachsan.activity.admin.AdminRoomListActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHotelAdapter extends RecyclerView.Adapter<AdminHotelAdapter.HotelViewHolder> {

    private Context context;
    private List<Hotel> hotelList;

    public AdminHotelAdapter(Context context, List<Hotel> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.txtHotelName.setText(hotel.getName());
        holder.txtHotelLocation.setText(hotel.getLocation());
        
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
        holder.txtHotelPrice.setText(formatter.format(hotel.getPricePerNight()) + "đ / đêm");

        if (hotel.getImageUrl() != null && !hotel.getImageUrl().isEmpty()) {
            Glide.with(context).load(hotel.getImageUrl()).error(R.drawable.ic_hotel).into(holder.imgHotel);
        } else {
            holder.imgHotel.setImageResource(R.drawable.ic_hotel);
        }

        // Bấm vào card -> Sang trang danh sách phòng
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.phuc.datvekhachsan.activity.admin.AdminRoomListActivity.class);
            intent.putExtra("HOTEL_ID", hotel.getId());
            intent.putExtra("HOTEL_NAME", hotel.getName());
            context.startActivity(intent);
        });

        // Bấm nút sửa -> Sang trang Hotel Editor
        holder.btnEditHotel.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.phuc.datvekhachsan.activity.admin.AdminHotelEditorActivity.class);
            intent.putExtra("HOTEL_ID", hotel.getId());
            context.startActivity(intent);
        });

        // Bấm nút xóa -> Hỏi xác nhận -> Xóa
        holder.btnDeleteHotel.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa Khách Sạn")
                    .setMessage("Bạn có chắc muốn xóa khách sạn " + hotel.getName() + " không? Việc này không thể hoàn tác.")
                    .setPositiveButton("Xóa", (dialog, which) -> deleteHotel(hotel.getId(), position))
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void deleteHotel(Long id, int position) {
        AdminApiService apiService = RetrofitClient.getClient(context).create(AdminApiService.class);
        apiService.deleteHotel(id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    hotelList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi khi xóa", Toast.LENGTH_SHORT).show();
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
        return hotelList.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotel, btnEditHotel, btnDeleteHotel;
        TextView txtHotelName, txtHotelLocation, txtHotelPrice;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            txtHotelName = itemView.findViewById(R.id.txtHotelName);
            txtHotelLocation = itemView.findViewById(R.id.txtHotelLocation);
            txtHotelPrice = itemView.findViewById(R.id.txtHotelPrice);
            btnEditHotel = itemView.findViewById(R.id.btnEditHotel);
            btnDeleteHotel = itemView.findViewById(R.id.btnDeleteHotel);
        }
    }
}
