package com.example.uramacha.feednews.main.presenter;

import com.example.uramacha.feednews.adapter.FeedItem;
import com.example.uramacha.feednews.main.model.MainInteractor;
import com.example.uramacha.feednews.main.model.MainInteractorImpl;
import com.example.uramacha.feednews.main.view.MainView;

import java.util.ArrayList;

/**
 * Implementation of the Presenter.
 */

public class MainPresenterImpl implements MainPresenter {

    private static MainView mainView;
    private final MainInteractor mainInteractor;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
        this.mainInteractor = new MainInteractorImpl(this);
    }


    @Override
    public void onLoadData(ArrayList<FeedItem> allItems) {
        mainView.loadNewsFeed(allItems);
    }

    @Override
    public void setTaskStatus(boolean status) {
        mainView.setTaskStatus(status);
    }

    @Override
    public void setRefreshLayoutVisibility(boolean visibility) {
        if (visibility)
            mainView.showRefreshLayout();
        else
            mainView.hideRefreshLayout();
    }

    @Override
    public void startTask() {
        mainInteractor.startTask();
    }

    @Override
    public void onUpdateActionBar(String string) {
        mainView.updateActionBar(string);
    }

    @Override
    public void showNetworkError() {
        mainView.showNetworkError();
    }
}
