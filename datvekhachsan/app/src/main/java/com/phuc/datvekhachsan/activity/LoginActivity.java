package com.phuc.datvekhachsan.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.network.ApiService;
import com.phuc.datvekhachsan.model.User;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        findViewById(R.id.loginBtn).setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            JsonObject request = new JsonObject();
            request.addProperty("username", username);
            request.addProperty("password", password);

            com.phuc.datvekhachsan.network.ApiService apiService = 
                com.phuc.datvekhachsan.network.RetrofitClient.getClient(this).create(com.phuc.datvekhachsan.network.ApiService.class);
            
            apiService.login(request).enqueue(new retrofit2.Callback<JsonObject>() {
                @Override
                public void onResponse(retrofit2.Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        JsonObject body = response.body();
                        if (body.has("token")) {
                            String token = body.get("token").getAsString();
                            
                            // Lưu Token vào SharedPreferences
                            android.content.SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
                            prefs.edit().putString("jwt_token", token).apply();

                            String role = body.has("role") && !body.get("role").isJsonNull() ? body.get("role").getAsString() : "ROLE_USER";

                            User user = new User();
                            user.setUsername(username);
                            user.setRole(role);
                            user.setFullName(username); // Fallback so isLoggedIn() doesn't fail
                            com.phuc.datvekhachsan.util.AuthManager.login(LoginActivity.this, user);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            
                            if (com.phuc.datvekhachsan.util.AuthManager.isAdmin(LoginActivity.this)) {
                                startActivity(new Intent(LoginActivity.this, com.phuc.datvekhachsan.activity.admin.AdminDashboardActivity.class));
                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.code() == 403) {
                        try {
                            String errorStr = response.errorBody() != null ? response.errorBody().string() : "";
                            JsonObject errorBody = new com.google.gson.JsonParser().parse(errorStr).getAsJsonObject();
                            String message = errorBody.has("message") ? errorBody.get("message").getAsString() : "Tài khoản của bạn đã bị khóa.";
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Tài khoản của bạn đã bị khóa do vi phạm chính sách.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        findViewById(R.id.goToRegisterTxt).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
