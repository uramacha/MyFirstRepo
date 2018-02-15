package com.example.uramacha.feednews;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by URAMACHA on 2/13/2018.
 * Web Service call to load the data synchronously
 */

public class FeedAsyncTask extends AsyncTask<Object, String, ArrayList<FeedItem>> {

    String TAG = getClass().getSimpleName();

    Context context;

    String urlStr = "";

    public static final int SERVER_EXCEPTION = 1;
    public static final int RESPONSE_EXCEPTION = 2;

    public static final String RESPONSE_TITLE = "title";
    public static final String RESPONSE = "response";
    public static final String EXCEPTION = "exception";
    public static final String TASK_STARTED = "started";
    public static final String TASK_ENDED = "ended";

    public FeedAsyncTask(Context context, String urlStr) {
        this.context = context;
        this.urlStr = urlStr;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "onPreExecute ");
        Intent intent = new Intent();
        intent.setAction(TASK_STARTED);
        context.sendBroadcast(intent);
    }

    @Override
    protected ArrayList<FeedItem> doInBackground(Object... params) {

        URL url;

        Log.d(TAG, "doInBackground " + urlStr);
        String line;
        StringBuilder sbResponse = new StringBuilder();

        try {

            url = new URL(urlStr);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(10000);


            InputStream is = urlConnection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedReader br = new BufferedReader(new InputStreamReader(bis));


            while ((line = br.readLine()) != null) {
                sbResponse.append(line);
            }


        } catch (Exception e) {
            Log.d(TAG, "Exception in Server " + e.getMessage());
            publishProgress("" + SERVER_EXCEPTION);
            return null;
        }

        return parseResponseData(sbResponse.toString());
    }

    private ArrayList<FeedItem> parseResponseData(String s) {

        String pageTitle = "";

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
                publishProgress(pageTitle);
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

                allFeedItems.add(feedItemModel);
            }


        } catch (Exception e) {
            Log.d(TAG, "Exception in parsing " + e.getMessage());
            publishProgress("" + RESPONSE_EXCEPTION);
            return null;
        }

        return allFeedItems;
    }

    @Override
    protected void onPostExecute(ArrayList<FeedItem> s) {
        super.onPostExecute(s);

        if (s == null) return;

        Bundle b = new Bundle();
        b.putSerializable("Obj", s);

        Intent intent = new Intent();
        intent.setAction(RESPONSE);
        intent.putExtra("bundle", b);
        context.sendBroadcast(intent);

    }


    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        Log.d(TAG, "onProgressUpdate " + values[0]);

        Intent intent = new Intent();

        if (values[0].equalsIgnoreCase("1") || values[0].equalsIgnoreCase("2")) {
            Log.d(TAG, "onProgressUpdate got exception");
            intent.setAction(EXCEPTION);
        } else {
            intent.setAction(RESPONSE_TITLE);
            intent.putExtra(FeedAsyncTask.RESPONSE_TITLE, values[0]);
        }

        context.sendBroadcast(intent);

    }

}
