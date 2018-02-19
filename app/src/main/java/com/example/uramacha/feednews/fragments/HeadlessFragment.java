package com.example.uramacha.feednews.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.uramacha.feednews.main.view.MainActivity;

/**
 * Fragment is used to handle the orientation changes
 */

public class HeadlessFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

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

        ((MainActivity)context).startTask();
    }


}
