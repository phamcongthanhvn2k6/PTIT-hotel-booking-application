package com.phuc.datvekhachsan.util;

import com.phuc.datvekhachsan.model.Hotel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class SearchEngine {
    private SearchEngine() {}

    public static List<Hotel> search(List<Hotel> baseList, String query, String locationFilter, double minPrice, double maxPrice, double minRating, String sort) {
        String q = (query == null) ? "" : query.trim().toLowerCase(Locale.ROOT);
        List<HotelMatch> matches = new ArrayList<>();

        for (Hotel h : baseList) {
            // location filter
            if (locationFilter != null && !locationFilter.isEmpty() && !locationFilter.equals("All") && !locationFilter.equals("All Locations")) {
                if (!h.getLocation().equalsIgnoreCase(locationFilter)) continue;
            }

            // price filter
            if (minPrice > 0 && h.getPricePerNight() < minPrice) continue;
            if (maxPrice > 0 && h.getPricePerNight() > maxPrice) continue;

            // rating filter
            if (minRating > 0 && h.getRating() < minRating) continue;

            int score = 0;
            if (q.isEmpty()) {
                score = 1; // default
            } else {
                String name = h.getName().toLowerCase(Locale.ROOT);
                String loc = h.getLocation().toLowerCase(Locale.ROOT);
                String desc = h.getDescription().toLowerCase(Locale.ROOT);

                if (name.contains(q)) score += 50;
                if (loc.contains(q)) score += 30;
                if (desc.contains(q)) score += 10;

                String[] tokens = q.split("\\s+");
                for (String t : tokens) {
                    if (name.startsWith(t)) score += 5;
                    if (desc.contains(t)) score += 2;
                }
            }

            matches.add(new HotelMatch(h, score));
        }

        // Sorting
        if ("price_asc".equals(sort)) {
            Collections.sort(matches, Comparator.comparingDouble(m -> m.hotel.getPricePerNight()));
        } else if ("price_desc".equals(sort)) {
            Collections.sort(matches, (a, b) -> Double.compare(b.hotel.getPricePerNight(), a.hotel.getPricePerNight()));
        } else if ("rating_desc".equals(sort)) {
            Collections.sort(matches, (a, b) -> Double.compare(b.hotel.getRating(), a.hotel.getRating()));
        } else {
            // relevance
            Collections.sort(matches, (a, b) -> Integer.compare(b.score, a.score));
        }

        List<Hotel> out = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (HotelMatch m : matches) {
            if (!seen.contains(m.hotel.getName())) {
                out.add(m.hotel);
                seen.add(m.hotel.getName());
            }
        }
        return out;
    }

    private static class HotelMatch {
        Hotel hotel;
        int score;

        HotelMatch(Hotel h, int s) { hotel = h; score = s; }
    }
}
