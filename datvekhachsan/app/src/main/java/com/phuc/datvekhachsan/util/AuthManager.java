package com.phuc.datvekhachsan.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.phuc.datvekhachsan.activity.LoginActivity;

public final class AuthManager {
    private static final String PREFS = "UserPrefs";
    private static final String KEY_FULLNAME = "fullName";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";

    private AuthManager() {}

    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.contains(KEY_FULLNAME) && !prefs.getString(KEY_FULLNAME, "").isEmpty();
    }

    public static String getFullName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getString(KEY_FULLNAME, "");
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, "");
    }

    public static String getEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getString(KEY_EMAIL, "");
    }

    public static String getRole(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getString(KEY_ROLE, "ROLE_USER");
    }

    public static boolean isAdmin(Context context) {
        return "ROLE_ADMIN".equals(getRole(context));
    }

    public static void login(Context context, com.phuc.datvekhachsan.model.User user) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_FULLNAME, user.getFullName())
                .putString(KEY_USERNAME, user.getUsername())
                .putString(KEY_EMAIL, user.getEmail())
                .putString(KEY_ROLE, user.getRole())
                .apply();
    }

    public static void requireLogin(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static void logout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}
