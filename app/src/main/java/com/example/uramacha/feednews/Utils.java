package com.example.uramacha.feednews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * All the constants variable and methods are added.
 */

class Utils {

    public static final String webServiceFeedUrl = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";

    public static boolean isConnected(MainActivity mainActivity) {
        ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
