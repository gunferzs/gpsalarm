package ru.gunferzs.gpsalarm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Created by GunFerzs on 07.09.2017.
 */

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();
    }

    public static Context getContext() {
        return context;
    }
}
