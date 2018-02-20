package com.example.uramacha.feednews.main.presenter;

import com.example.uramacha.feednews.adapter.FeedItem;

import java.util.ArrayList;

/**
 * It is an interface between View and Model/Interactor.
 */

public interface MainPresenter {

    void onUpdateActionBar(String string);

    void startTask();

    void setTaskStatus(boolean status);

    void setRefreshLayoutVisibility(boolean visibility);

    void onLoadData(ArrayList<FeedItem> allItems);

	void onDestroy();

    void showNetworkError();
}
