package com.phuc.datvekhachsan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Room;
import com.phuc.datvekhachsan.network.AdminApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRoomAdapter extends RecyclerView.Adapter<AdminRoomAdapter.RoomViewHolder> {

    private Context context;
    private List<Room> roomList;

    public AdminRoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.txtRoomNumber.setText("Phòng " + room.getName());
        holder.txtRoomType.setText("Loại phòng: " + room.getRoomType());
        
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
        holder.txtRoomPrice.setText("Giá: " + formatter.format(room.getPricePerNight()) + "đ");

        holder.txtRoomStatus.setText("Trạng thái: " + (room.getStatus() != null ? room.getStatus().name() : ""));
        if (room.getStatus() != null && "AVAILABLE".equalsIgnoreCase(room.getStatus().name())) {
            holder.txtRoomStatus.setTextColor(Color.parseColor("#05CD99"));
        } else {
            holder.txtRoomStatus.setTextColor(Color.parseColor("#EE5D50"));
        }

        holder.btnEditRoom.setOnClickListener(v -> {
            // Intent intent = new Intent(context, AdminRoomEditorActivity.class);
            // intent.putExtra("ROOM_ID", room.getId());
            // context.startActivity(intent);
            Toast.makeText(context, "Sửa phòng", Toast.LENGTH_SHORT).show();
        });

        holder.btnDeleteRoom.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa Phòng")
                    .setMessage("Bạn có chắc muốn xóa phòng " + room.getName() + " không?")
                    .setPositiveButton("Xóa", (dialog, which) -> deleteRoom(room.getId(), position))
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void deleteRoom(Long id, int position) {
        AdminApiService apiService = RetrofitClient.getClient(context).create(AdminApiService.class);
        apiService.deleteRoom(id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    roomList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Đã xóa phòng", Toast.LENGTH_SHORT).show();
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
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView txtRoomNumber, txtRoomType, txtRoomPrice, txtRoomStatus;
        ImageView btnEditRoom, btnDeleteRoom;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRoomNumber = itemView.findViewById(R.id.txtRoomNumber);
            txtRoomType = itemView.findViewById(R.id.txtRoomType);
            txtRoomPrice = itemView.findViewById(R.id.txtRoomPrice);
            txtRoomStatus = itemView.findViewById(R.id.txtRoomStatus);
            btnEditRoom = itemView.findViewById(R.id.btnEditRoom);
            btnDeleteRoom = itemView.findViewById(R.id.btnDeleteRoom);
        }
    }
}
