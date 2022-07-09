package com.example.scanqrlite;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Language{
    Locale locale;
    Context context;

    public Language(Context context) {
        this.context = context;
    }

    public void Language(){
        SharedPreferences sp_language = context.getSharedPreferences("language", Context.MODE_PRIVATE );
        String check = sp_language.getString("language", "English");
        Log.e("TAG@:",check);
        switch (check) {
            case "English":
                locale = new Locale("en");
                ChangeLanguage(locale);
                break;
            case "German":
                locale = new Locale("de");
                ChangeLanguage(locale);
                break;
            case "Chinese":
                locale = new Locale("zh");
                ChangeLanguage(locale);
                break;
            case "Korean":
                locale = new Locale("ko");
                ChangeLanguage(locale);
                break;
            case "Vietnamese":
                locale = new Locale("vi");
                ChangeLanguage(locale);
                break;
        }

    }
    private void ChangeLanguage(Locale locale) {
        Locale.setDefault(locale);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }
}
