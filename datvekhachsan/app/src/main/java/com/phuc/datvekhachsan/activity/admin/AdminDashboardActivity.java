package com.phuc.datvekhachsan.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.network.AdminApiService;
import com.phuc.datvekhachsan.network.RetrofitClient;
import com.phuc.datvekhachsan.util.AuthManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardActivity extends BaseAdminActivity {

    private TextView txtTotalRevenue, txtTotalUsers, txtTotalHotels, txtTotalBookings;
    private ProgressBar progressBarStats;
    private GridLayout statsGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_admin_dashboard);
        setAdminTitle("Thống kê (Dashboard)");

        txtTotalRevenue = findViewById(R.id.txtTotalRevenue);
        txtTotalUsers = findViewById(R.id.txtTotalUsers);
        txtTotalHotels = findViewById(R.id.txtTotalHotels);
        txtTotalBookings = findViewById(R.id.txtTotalBookings);
        progressBarStats = findViewById(R.id.progressBarStats);
        statsGrid = findViewById(R.id.statsGrid);

        TextView textViewGreeting = findViewById(R.id.textViewAdminGreeting);
        if (AuthManager.isLoggedIn(this)) {
            String fullName = AuthManager.getFullName(this);
            textViewGreeting.setText("Chào mừng, " + fullName);
        }

        fetchStats();
    }

    private void fetchStats() {
        progressBarStats.setVisibility(View.VISIBLE);
        statsGrid.setVisibility(View.GONE);

        AdminApiService apiService = RetrofitClient.getClient(this).create(AdminApiService.class);
        apiService.getDashboardStats().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressBarStats.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject stats = response.body();
                    
                    long totalUsers = stats.has("totalUsers") ? stats.get("totalUsers").getAsLong() : 0;
                    long totalHotels = stats.has("totalHotels") ? stats.get("totalHotels").getAsLong() : 0;
                    long totalBookings = stats.has("totalBookings") ? stats.get("totalBookings").getAsLong() : 0;
                    double totalRevenue = stats.has("totalRevenue") ? stats.get("totalRevenue").getAsDouble() : 0;

                    java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
                    
                    txtTotalUsers.setText(String.valueOf(totalUsers));
                    txtTotalHotels.setText(String.valueOf(totalHotels));
                    txtTotalBookings.setText(String.valueOf(totalBookings));
                    txtTotalRevenue.setText(formatter.format(totalRevenue) + "đ");
                    
                    statsGrid.setVisibility(View.VISIBLE);
                    setupChart(totalUsers, totalHotels, totalBookings);
                } else {
                    Toast.makeText(AdminDashboardActivity.this, "Lỗi tải dữ liệu thống kê", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressBarStats.setVisibility(View.GONE);
                Toast.makeText(AdminDashboardActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupChart(long users, long hotels, long bookings) {
        com.github.mikephil.charting.charts.BarChart barChart = findViewById(R.id.barChart);
        
        java.util.ArrayList<com.github.mikephil.charting.data.BarEntry> entries = new java.util.ArrayList<>();
        entries.add(new com.github.mikephil.charting.data.BarEntry(0f, users));
        entries.add(new com.github.mikephil.charting.data.BarEntry(1f, hotels));
        entries.add(new com.github.mikephil.charting.data.BarEntry(2f, bookings));
        
        com.github.mikephil.charting.data.BarDataSet dataSet = new com.github.mikephil.charting.data.BarDataSet(entries, "Số lượng");
        
        int colorUsers = android.graphics.Color.parseColor("#4318FF");
        int colorHotels = android.graphics.Color.parseColor("#FFB547");
        int colorBookings = android.graphics.Color.parseColor("#EE5D50");
        dataSet.setColors(colorUsers, colorHotels, colorBookings);
        
        com.github.mikephil.charting.data.BarData barData = new com.github.mikephil.charting.data.BarData(dataSet);
        barData.setBarWidth(0.5f);
        barChart.setData(barData);
        
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        
        com.github.mikephil.charting.components.XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(new String[]{"Người dùng", "Khách sạn", "Đặt phòng"}));
        
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);
        
        barChart.animateY(1000);
        barChart.invalidate();
    }
}
