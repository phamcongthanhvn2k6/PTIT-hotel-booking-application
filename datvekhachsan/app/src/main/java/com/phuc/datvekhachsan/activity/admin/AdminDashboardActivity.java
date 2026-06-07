package com.phuc.datvekhachsan.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.util.AuthManager;

public class AdminDashboardActivity extends BaseAdminActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_admin_dashboard);
        setAdminTitle("Thống kê (Dashboard)");

        findViewById(R.id.cardManageUsers).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminUserListActivity.class));
        });

        findViewById(R.id.cardManageHotels).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminHotelListActivity.class));
        });

        TextView textViewGreeting = findViewById(R.id.textViewAdminGreeting);
        if (AuthManager.isLoggedIn(this)) {
            String fullName = AuthManager.getFullName(this);
            textViewGreeting.setText("Chào mừng, " + fullName);
        }
    }
}
