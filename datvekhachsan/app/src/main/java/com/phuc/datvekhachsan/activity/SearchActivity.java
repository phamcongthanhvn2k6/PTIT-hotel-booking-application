package com.phuc.datvekhachsan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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

public class SearchActivity extends AppCompatActivity {
    private EditText searchInput;
    private ImageView backBtn;
    private ImageView clearSearchBtn;
    private ChipGroup chipGroupLocation;
    private ChipGroup chipGroupPrice;
    private ChipGroup chipGroupSort;
    private ChipGroup chipGroupRating;
    private RecyclerView resultsRecycler;
    private View emptyStateLayout;
    
    private HotelListAdapter adapter;
    private final Handler handler = new Handler();
    private Runnable searchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.searchInput);
        backBtn = findViewById(R.id.backBtn);
        clearSearchBtn = findViewById(R.id.clearSearchBtn);
        chipGroupLocation = findViewById(R.id.chipGroupLocation);
        chipGroupPrice = findViewById(R.id.chipGroupPrice);
        chipGroupSort = findViewById(R.id.chipGroupSort);
        chipGroupRating = findViewById(R.id.chipGroupRating);
        resultsRecycler = findViewById(R.id.searchResultsRecycler);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);

        resultsRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HotelListAdapter(new ArrayList<>(), true);
        resultsRecycler.setAdapter(adapter);

        backBtn.setOnClickListener(v -> finish());
        
        clearSearchBtn.setOnClickListener(v -> {
            searchInput.setText("");
            clearSearchBtn.setVisibility(View.GONE);
        });

        setupFilters();
        setupSearch();

        // Read query from intent extra
        String initialQuery = getIntent().getStringExtra("query");
        if (initialQuery != null) {
            searchInput.setText(initialQuery);
            clearSearchBtn.setVisibility(initialQuery.isEmpty() ? View.GONE : View.VISIBLE);
        }

        runSearch();
    }

    private void setupFilters() {
        // 1. Locations Chips
        List<String> locs = new ArrayList<>();
        locs.add("Tất cả");
        Set<String> seen = new HashSet<>();
        for (Hotel h : MockData.getPopularHotels()) {
            if (seen.add(h.getLocation())) locs.add(h.getLocation());
        }
        for (Hotel h : MockData.getRecommendedHotels()) {
            if (seen.add(h.getLocation())) locs.add(h.getLocation());
        }

        for (int i = 0; i < locs.size(); i++) {
            String loc = locs.get(i);
            Chip chip = new Chip(this);
            chip.setText(loc);
            chip.setCheckable(true);
            chip.setId(View.generateViewId());
            chipGroupLocation.addView(chip);
            if (i == 0) {
                chip.setChecked(true); // Select "Tất cả" by default
            }
        }
        chipGroupLocation.setOnCheckedStateChangeListener((group, checkedIds) -> runSearch());

        // 1.5. Price Chips
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
        chipGroupPrice.setOnCheckedStateChangeListener((group, checkedIds) -> runSearch());

        // 2. Sort Chips
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
        chipGroupSort.setOnCheckedStateChangeListener((group, checkedIds) -> runSearch());

        // 3. Rating Chips
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
        chipGroupRating.setOnCheckedStateChangeListener((group, checkedIds) -> runSearch());
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                if (searchTask != null) handler.removeCallbacks(searchTask);
                clearSearchBtn.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
            @Override public void afterTextChanged(Editable s) {
                searchTask = () -> runSearch();
                handler.postDelayed(searchTask, 300);
            }
        });
    }

    private void runSearch() {
        String query = searchInput.getText().toString().trim();

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

        List<Hotel> results = SearchEngine.search(query, location, minPrice, maxPrice, minRating, sortSel);
        
        // Show/hide empty state
        if (results.isEmpty()) {
            resultsRecycler.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            resultsRecycler.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
            
            adapter = new HotelListAdapter(results, true);
            resultsRecycler.setAdapter(adapter);
        }
    }
}
