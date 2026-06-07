package com.phuc.datvekhachsan.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.activity.LoginActivity;
import com.phuc.datvekhachsan.activity.MainActivity;
import com.phuc.datvekhachsan.util.AuthManager;

public class BaseAdminActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    private TextView txtAdminTitle;
    private ImageView btnAdminRightAction;
    private FrameLayout contentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Cấu hình không viền
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    protected void setContentLayout(int layoutResID) {
        // Nạp layout gốc (chứa Drawer và Toolbar)
        super.setContentView(R.layout.layout_admin_base);

        drawerLayout = findViewById(R.id.drawerLayoutAdmin);
        navigationView = findViewById(R.id.navigationViewAdmin);
        txtAdminTitle = findViewById(R.id.txtAdminTitle);
        btnAdminRightAction = findViewById(R.id.btnAdminRightAction);
        contentFrame = findViewById(R.id.adminContentFrame);

        // Nạp layout con vào FrameLayout
        LayoutInflater.from(this).inflate(layoutResID, contentFrame, true);

        // Cài đặt nút mở Menu
        findViewById(R.id.btnMenuAdmin).setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        initNavigation();
        updateAdminInfo();
    }

    protected void setAdminTitle(String title) {
        if (txtAdminTitle != null) {
            txtAdminTitle.setText(title);
        }
    }

    protected void setLeftAction(int iconResId, View.OnClickListener listener) {
        ImageView btnMenuAdmin = findViewById(R.id.btnMenuAdmin);
        if (btnMenuAdmin != null) {
            btnMenuAdmin.setImageResource(iconResId);
            btnMenuAdmin.setOnClickListener(listener);
        }
    }

    protected void setRightAction(int iconResId, View.OnClickListener listener) {
        if (btnAdminRightAction != null) {
            btnAdminRightAction.setImageResource(iconResId);
            btnAdminRightAction.setVisibility(View.VISIBLE);
            btnAdminRightAction.setOnClickListener(listener);
        }
    }

    private void initNavigation() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawer(GravityCompat.START);
            
            if (id == R.id.nav_admin_dashboard && !(this instanceof AdminDashboardActivity)) {
                startActivity(new Intent(this, AdminDashboardActivity.class));
            } else if (id == R.id.nav_admin_users && !(this instanceof AdminUserListActivity)) {
                startActivity(new Intent(this, AdminUserListActivity.class));
            } else if (id == R.id.nav_admin_hotels && !(this instanceof AdminHotelListActivity)) {
                startActivity(new Intent(this, AdminHotelListActivity.class));
            } else if (id == R.id.nav_admin_bookings && !(this instanceof AdminBookingListActivity)) {
                startActivity(new Intent(this, AdminBookingListActivity.class));
            } else if (id == R.id.nav_admin_logout) {
                new AlertDialog.Builder(this)
                        .setTitle("Xác nhận đăng xuất")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                        .setPositiveButton("Đăng xuất", (dialog, which) -> {
                            AuthManager.logout(this);
                            Intent intent = new Intent(this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            } else if (id == R.id.nav_admin_switch_user) {
                startActivity(new Intent(this, MainActivity.class));
            }
            return true;
        });
    }

    protected void updateAdminInfo() {
        if (navigationView == null) return;
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderName = headerView.findViewById(R.id.navHeaderName);
        TextView navHeaderEmail = headerView.findViewById(R.id.navHeaderEmail);

        if (AuthManager.isLoggedIn(this)) {
            String fullName = AuthManager.getFullName(this);
            String username = AuthManager.getUsername(this);
            navHeaderName.setText(fullName);
            navHeaderEmail.setText(username);
        }
    }
}
