package com.example.fivecrowns;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    /**
     * Stores the context of the application.
     * Note: Storing application instance might create memory leak.
     */
    private static Context context;

    /**
     * Called when application is created
     */
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    /**
     * Get the context of the application
     * @return Application context
     */
    public static Context getAppContext() {
        return MyApplication.context;
    }
}
