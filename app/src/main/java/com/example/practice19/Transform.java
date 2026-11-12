package com.example.practice19;

import android.content.SharedPreferences;

import static com.example.practice19.UserStaticInfo.LOGIN;
import static com.example.practice19.UserStaticInfo.PASSWORD;

public class Transform {

    public static final String APP_PREFERENCES = "mysettings";

    public static int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static void SaveUser(SharedPreferences sp, String login, String password) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LOGIN, login);
        editor.putString(PASSWORD, password);
        editor.apply();
    }
}
