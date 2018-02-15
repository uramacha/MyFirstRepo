package com.example.uramacha.feednews;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Fragment is used to handle the orientation changes
 */

public class HeadlessFragment extends Fragment {

    private String TAG = getClass().getSimpleName();

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        Log.d(TAG, "onAttach ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        ((MainActivity)context).callFeedService();
    }


}
