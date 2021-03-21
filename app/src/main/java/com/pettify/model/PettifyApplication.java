package com.pettify.model;

import android.app.Application;
import android.content.Context;

public class PettifyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

}
