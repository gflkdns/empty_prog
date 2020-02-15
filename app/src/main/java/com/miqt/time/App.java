package com.miqt.time;

import android.app.Application;

import com.analysys.track.AnalysysTracker;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AnalysysTracker.init(this, "7752552892442721d", "baidu");
    }
}
