package com.pharm.activities;

import android.app.Application;

import com.pharm.helper.FontsOverride;

/**
 * Created by Avinash
 * Base Application
 */
public class App extends Application {

    private static App instance;

    public App() {
        instance = this;
    }

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "roboto_thin.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "roboto_thin.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "roboto_thin.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "roboto_thin.ttf");
    }
}
