package com.phuc.datvekhachsan.adapter;

import android.content.Context;
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
import com.phuc.datvekhachsan.model.User;
import com.phuc.datvekhachsan.network.AdminApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;

    public AdminUserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.txtUsername.setText(user.getUsername());
        holder.txtEmail.setText(user.getEmail() != null ? user.getEmail() : "Chưa có email");
        holder.txtRole.setText(user.getRole() != null ? user.getRole() : "ROLE_USER");

        if ("ROLE_ADMIN".equals(user.getRole())) {
            holder.txtRole.setTextColor(android.graphics.Color.parseColor("#05CD99")); // Xanh lá
        } else {
            holder.txtRole.setTextColor(android.graphics.Color.parseColor("#4318FF")); // Xanh dương
        }

        if ("LOCKED".equals(user.getStatus())) {
            holder.txtStatus.setText("BỊ KHÓA");
            holder.txtStatus.setTextColor(android.graphics.Color.parseColor("#EE5D50"));
            holder.txtStatus.setBackgroundColor(android.graphics.Color.parseColor("#FDECEB"));
            holder.btnLockUser.setImageResource(R.drawable.ic_lock); // Hoặc bạn có thể dùng icon unlock nếu có
        } else {
            holder.txtStatus.setText("HOẠT ĐỘNG");
            holder.txtStatus.setTextColor(android.graphics.Color.parseColor("#05CD99"));
            holder.txtStatus.setBackgroundColor(android.graphics.Color.parseColor("#E6F9F4"));
            holder.btnLockUser.setImageResource(R.drawable.ic_lock);
        }

        holder.btnChangeRole.setOnClickListener(v -> {
            String newRole = "ROLE_ADMIN".equals(user.getRole()) ? "ROLE_USER" : "ROLE_ADMIN";
            new AlertDialog.Builder(context)
                    .setTitle("Đổi quyền")
                    .setMessage("Bạn có chắc muốn cấp quyền " + newRole + " cho " + user.getUsername() + "?")
                    .setPositiveButton("Đồng ý", (dialog, which) -> changeRole(user, newRole, position))
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        holder.btnLockUser.setOnClickListener(v -> {
            String newStatus = "LOCKED".equals(user.getStatus()) ? "ACTIVE" : "LOCKED";
            String action = "ACTIVE".equals(newStatus) ? "Mở khóa" : "Khóa";
            new AlertDialog.Builder(context)
                    .setTitle(action + " tài khoản")
                    .setMessage("Bạn có chắc muốn " + action.toLowerCase() + " tài khoản " + user.getUsername() + "?")
                    .setPositiveButton("Đồng ý", (dialog, which) -> changeStatus(user, newStatus, position))
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void changeRole(User user, String newRole, int position) {
        AdminApiService apiService = RetrofitClient.getClient(context).create(AdminApiService.class);
        JsonObject request = new JsonObject();
        request.addProperty("role", newRole);

        apiService.updateUserRole(user.getId(), request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user.setRole(newRole);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Đã đổi quyền thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi khi đổi quyền", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeStatus(User user, String newStatus, int position) {
        AdminApiService apiService = RetrofitClient.getClient(context).create(AdminApiService.class);
        JsonObject request = new JsonObject();
        request.addProperty("status", newStatus);

        apiService.updateUserStatus(user.getId(), request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    user.setStatus(newStatus);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Đã cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi khi cập nhật trạng thái", Toast.LENGTH_SHORT).show();
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
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtEmail, txtRole, txtStatus;
        Button btnChangeRole;
        android.widget.ImageButton btnLockUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtRole = itemView.findViewById(R.id.txtRole);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnChangeRole = itemView.findViewById(R.id.btnChangeRole);
            btnLockUser = itemView.findViewById(R.id.btnLockUser);
        }
    }
}
