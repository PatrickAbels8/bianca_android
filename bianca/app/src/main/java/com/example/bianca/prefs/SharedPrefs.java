package com.example.bianca.prefs;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.core.app.ServiceCompat;

import com.example.bianca.R;

public class SharedPrefs {
    static SharedPreferences getPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setLoggedIn(Context context, boolean loggedIn){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(PreferencesUtility.LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    public static boolean getLoggedIn(Context context){
        return getPreferences(context).getBoolean(PreferencesUtility.LOGGED_IN_PREF, false);
    }

    public static void setUsername(Context context, String name){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(PreferencesUtility.USERNAME_PREF, name);
        editor.apply();
    }

    public static String getUsername(Context context){
        return getPreferences(context).getString(PreferencesUtility.USERNAME_PREF, "");
    }

    public static void setUserid(Context context, int id){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(PreferencesUtility.USERID_PREF, id);
        editor.apply();
    }

    public static int getUserid(Context context){
        return getPreferences(context).getInt(PreferencesUtility.USERID_PREF, -1);
    }

    public static String getLanguage(Context context){
        return getPreferences(context).getString(PreferencesUtility.LANGUAGE_PREF, context.getString(R.string.lang_de));
    }

    public static void setLanguage(Context context, String lang){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(PreferencesUtility.LANGUAGE_PREF, lang);
        editor.apply();
    }
}
