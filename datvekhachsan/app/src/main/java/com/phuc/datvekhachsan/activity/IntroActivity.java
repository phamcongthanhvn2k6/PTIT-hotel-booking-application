package com.phuc.datvekhachsan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.phuc.datvekhachsan.R;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if (com.phuc.datvekhachsan.util.AuthManager.isLoggedIn(this)) {
            if (com.phuc.datvekhachsan.util.AuthManager.isAdmin(this)) {
                startActivity(new Intent(this, com.phuc.datvekhachsan.activity.admin.AdminDashboardActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
            return;
        }

        findViewById(R.id.startBtn).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}