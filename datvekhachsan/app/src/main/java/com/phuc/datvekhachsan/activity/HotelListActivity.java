package com.phuc.datvekhachsan.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.adapter.HotelListAdapter;
import com.phuc.datvekhachsan.data.MockData;
import com.phuc.datvekhachsan.model.Hotel;
import com.phuc.datvekhachsan.util.SearchEngine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HotelListActivity extends AppCompatActivity {
    private List<Hotel> baseHotels = new ArrayList<>();
    private ChipGroup chipGroupLocation;
    private ChipGroup chipGroupPrice;
    private ChipGroup chipGroupSort;
    private ChipGroup chipGroupRating;
    private RecyclerView recyclerView;
    private View emptyStateLayout;
    private HotelListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        TextView tvListTitle = findViewById(R.id.tvListTitle);
        recyclerView = findViewById(R.id.recyclerViewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chipGroupLocation = findViewById(R.id.chipGroupLocation);
        chipGroupPrice = findViewById(R.id.chipGroupPrice);
        chipGroupSort = findViewById(R.id.chipGroupSort);
        chipGroupRating = findViewById(R.id.chipGroupRating);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);

        String type = getIntent().getStringExtra("type");
        if ("popular".equals(type)) {
            tvListTitle.setText(R.string.hotel_list_title_popular);
            baseHotels = MockData.getPopularHotels();
        } else if ("search".equals(type)) {
            String query = getIntent().getStringExtra("query");
            tvListTitle.setText(getString(R.string.hotel_list_title_search_result, query));
            baseHotels = MockData.searchHotels(query != null ? query : "");
        } else {
            tvListTitle.setText(R.string.hotel_list_title_recommended);
            baseHotels = MockData.getRecommendedHotels();
        }

        adapter = new HotelListAdapter(new ArrayList<>(baseHotels), true);
        recyclerView.setAdapter(adapter);

        setupFilters();
        runFilter();
    }

    private void setupFilters() {
        // 1. Locations Chips (dynamically extract from baseList)
        List<String> locs = new ArrayList<>();
        locs.add("Tất cả");
        Set<String> seen = new HashSet<>();
        for (Hotel h : baseHotels) {
            if (seen.add(h.getLocation())) locs.add(h.getLocation());
        }

        for (int i = 0; i < locs.size(); i++) {
            Chip chip = new Chip(this);
            chip.setText(locs.get(i));
            chip.setCheckable(true);
            chip.setId(View.generateViewId());
            chipGroupLocation.addView(chip);
            if (i == 0) {
                chip.setChecked(true); // Select "Tất cả" by default
            }
        }
        chipGroupLocation.setOnCheckedStateChangeListener((group, checkedIds) -> runFilter());

        // 2. Price Chips
        String[] prices = new String[]{"Tất cả giá", "Dưới 1.5 triệu", "1.5 - 2.5 triệu", "Trên 2.5 triệu"};
        for (int i = 0; i < prices.length; i++) {
            Chip chip = new Chip(this);
            chip.setText(prices[i]);
            chip.setCheckable(true);
            chip.setId(View.generateViewId());
            chipGroupPrice.addView(chip);
            if (i == 0) {
                chip.setChecked(true);
            }
        }
        chipGroupPrice.setOnCheckedStateChangeListener((group, checkedIds) -> runFilter());

        // 3. Sort Chips
        String[] sorts = new String[]{"Phù hợp nhất", "Giá tăng dần", "Giá giảm dần", "Đánh giá"};
        for (int i = 0; i < sorts.length; i++) {
            Chip chip = new Chip(this);
            chip.setText(sorts[i]);
            chip.setCheckable(true);
            chip.setId(View.generateViewId());
            chipGroupSort.addView(chip);
            if (i == 0) {
                chip.setChecked(true);
            }
        }
        chipGroupSort.setOnCheckedStateChangeListener((group, checkedIds) -> runFilter());

        // 4. Rating Chips
        String[] ratings = new String[]{"Tất cả đánh giá", "4.0+ ★", "4.5+ ★"};
        for (int i = 0; i < ratings.length; i++) {
            Chip chip = new Chip(this);
            chip.setText(ratings[i]);
            chip.setCheckable(true);
            chip.setId(View.generateViewId());
            chipGroupRating.addView(chip);
            if (i == 0) {
                chip.setChecked(true);
            }
        }
        chipGroupRating.setOnCheckedStateChangeListener((group, checkedIds) -> runFilter());
    }

    private void runFilter() {
        // Retrieve location filter
        String location = "All";
        int checkedLocId = chipGroupLocation.getCheckedChipId();
        if (checkedLocId != View.NO_ID) {
            Chip chip = findViewById(checkedLocId);
            if (chip != null) {
                String val = chip.getText().toString();
                if (!val.equals("Tất cả")) {
                    location = val;
                }
            }
        }

        // Retrieve price ranges
        double minPrice = 0;
        double maxPrice = 0;
        int checkedPriceId = chipGroupPrice.getCheckedChipId();
        if (checkedPriceId != View.NO_ID) {
            Chip chip = findViewById(checkedPriceId);
            if (chip != null) {
                String val = chip.getText().toString();
                if ("Dưới 1.5 triệu".equals(val)) {
                    maxPrice = 1500000;
                } else if ("1.5 - 2.5 triệu".equals(val)) {
                    minPrice = 1500000;
                    maxPrice = 2500000;
                } else if ("Trên 2.5 triệu".equals(val)) {
                    minPrice = 2500000;
                }
            }
        }

        // Retrieve sorting
        String sortSel = "relevance";
        int checkedSortId = chipGroupSort.getCheckedChipId();
        if (checkedSortId != View.NO_ID) {
            Chip chip = findViewById(checkedSortId);
            if (chip != null) {
                String val = chip.getText().toString();
                if ("Giá tăng dần".equals(val)) sortSel = "price_asc";
                else if ("Giá giảm dần".equals(val)) sortSel = "price_desc";
                else if ("Đánh giá".equals(val)) sortSel = "rating_desc";
            }
        }

        // Retrieve rating
        double minRating = 0;
        int checkedRatingId = chipGroupRating.getCheckedChipId();
        if (checkedRatingId != View.NO_ID) {
            Chip chip = findViewById(checkedRatingId);
            if (chip != null) {
                String val = chip.getText().toString();
                if ("4.0+ ★".equals(val)) minRating = 4.0;
                else if ("4.5+ ★".equals(val)) minRating = 4.5;
            }
        }

        List<Hotel> filteredList = SearchEngine.searchList(baseHotels, "", location, minPrice, maxPrice, minRating, sortSel);

        if (filteredList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);

            adapter = new HotelListAdapter(filteredList, true);
            recyclerView.setAdapter(adapter);
        }
    }
}
