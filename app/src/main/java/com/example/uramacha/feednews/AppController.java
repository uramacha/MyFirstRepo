package com.example.uramacha.feednews;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * This class will act as singleTon for app.
 */

public class AppController extends Application {

    private static AppController mInstance;

    public static AppController getInstance() {
        return mInstance;
    }

    RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(this);
    }
}
