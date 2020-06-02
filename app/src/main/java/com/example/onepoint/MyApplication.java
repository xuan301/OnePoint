package com.example.onepoint;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
    super.onCreate();
    // 默认设置为日间模式
    AppCompatDelegate.setDefaultNightMode(
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
}
}


