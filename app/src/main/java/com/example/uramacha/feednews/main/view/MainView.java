package com.example.uramacha.feednews.main.view;

import com.example.uramacha.feednews.adapter.FeedItem;

import java.util.ArrayList;

/**
 * Common Methods for MainActivity.
 */

public interface MainView {

    void showRefreshLayout();

    void hideRefreshLayout();

    void setTaskStatus(boolean status);

    void loadNewsFeed(ArrayList<FeedItem> allItems);

    void updateActionBar(String str);

    void showNetworkError();

}
