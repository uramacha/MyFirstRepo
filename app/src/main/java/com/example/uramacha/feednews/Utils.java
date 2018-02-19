package com.example.uramacha.feednews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * All the constants variable and methods are added.
 */

public class Utils {

    public static final String FRAGMENT_TAG = "fragment_tag";
    public static final String KEY_OBJECT = "key_object";
    public static final String KEY_TITLE = "key_title";
    public static boolean taskIsRunning =  false;

    public static final String webServiceFeedUrl = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
