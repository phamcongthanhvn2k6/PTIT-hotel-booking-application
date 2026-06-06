package com.phuc.datvekhachsan.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.phuc.datvekhachsan.model.Booking;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BookingManager {
    private static final String PREFS_NAME = "BookingPrefs";
    private static final String KEY_PREFIX = "bookings_";

    private BookingManager() {}

    public static synchronized void addBooking(Context context, Booking booking, String username) {
        if (username == null || username.isEmpty()) return;

        List<Booking> bookings = getBookings(context, username);
        bookings.add(0, booking); // Add new booking to the top

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(bookings);
        prefs.edit().putString(KEY_PREFIX + username, json).apply();
    }

    public static synchronized List<Booking> getBookings(Context context, String username) {
        if (username == null || username.isEmpty()) return new ArrayList<>();

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_PREFIX + username, null);
        if (json == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Booking>>() {}.getType();
        try {
            List<Booking> list = gson.fromJson(json, listType);
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
