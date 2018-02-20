package com.example.uramacha.feednews.main.model;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.uramacha.feednews.AppController;
import com.example.uramacha.feednews.adapter.FeedItem;
import com.example.uramacha.feednews.main.presenter.MainPresenter;
import com.example.uramacha.feednews.main.presenter.MainPresenterImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.uramacha.feednews.Utils.webServiceFeedUrl;

/**
 * It is Model to request the data from server.
 */

public class MainInteractorImpl implements MainInteractor {

    private final String TAG = getClass().getSimpleName();

    private final MainPresenter mainPresenter;

    public MainInteractorImpl(MainPresenterImpl mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void startTask() {

        mainPresenter.setTaskStatus(true);
        mainPresenter.setRefreshLayoutVisibility(true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                webServiceFeedUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mainPresenter.setTaskStatus(false);
                        mainPresenter.setRefreshLayoutVisibility(false);

                        if (response == null) return;

                        Log.d(TAG, "onResponse " + response);

                        ArrayList<FeedItem> allFeedItems = parseResponseData(response);

                        mainPresenter.onLoadData(allFeedItems);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse " + error.getMessage());

                        mainPresenter.setTaskStatus(false);
                        mainPresenter.setRefreshLayoutVisibility(false);
                        mainPresenter.showNetworkError();

                    }
                });

        AppController.getInstance().mRequestQueue.add(stringRequest);

    }

    private ArrayList<FeedItem> parseResponseData(String s) {

        String pageTitle;

        String title = "";
        String description = "";
        String imageHref = "";

        JSONObject mainFeed;
        JSONArray allItemsFeed;
        ArrayList<FeedItem> allFeedItems = new ArrayList<>();

        try {
            mainFeed = new JSONObject(s);

            if (mainFeed.has("title")) {
                pageTitle = mainFeed.getString("title");
                mainPresenter.onUpdateActionBar(pageTitle);
            }

            allItemsFeed = new JSONArray(mainFeed.getString("rows"));

            for (int i = 0; i < allItemsFeed.length(); i++) {
                JSONObject itemJson = new JSONObject(allItemsFeed.get(i).toString());

                if (itemJson.has("title")) {
                    title = itemJson.getString("title");
                }
                if (itemJson.has("description")) {
                    description = itemJson.getString("description");
                }
                if (itemJson.has("imageHref")) {
                    imageHref = itemJson.getString("imageHref");
                }

                FeedItem feedItemModel = new FeedItem(title, description, imageHref);
                if (isVallidFeed(feedItemModel))
                    allFeedItems.add(feedItemModel);
            }


        } catch (Exception e) {
            Log.d(TAG, "Exception in parsing " + e.getMessage());
            return null;
        }

        return allFeedItems;
    }


    private boolean isVallidFeed(FeedItem feedItem) {

        return !(feedItem.getTitle().equalsIgnoreCase("null") &&
                feedItem.getDescription().equalsIgnoreCase("null") &&
                feedItem.getImagehref().equalsIgnoreCase("null"));
    }
}
