package com.example.uramacha.feednews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Main class for the News Feed Display in the screen
 */

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private SwipeRefreshLayout refreshLayout;

    private String TAG = getClass().getSimpleName();

    private ArrayList<FeedItem> allFeedItems = new ArrayList<>();
    private String title = "";
    private CustomFeedAdapter adapter;

    private static final String FRAGMENT_TAG = "fragment_tag";
    private static final String KEY_OBJECT = "Obj";
    private static final String KEY_TITLE = "title";
    private static boolean taskIsRunning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeds_activity);

        if (!Utils.isConnected(this)) {
            Toast.makeText(this, "Please connect to network", Toast.LENGTH_SHORT).show();
        }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        HeadlessFragment headlessFragment = (HeadlessFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (headlessFragment == null) {
            headlessFragment = new HeadlessFragment();
            getSupportFragmentManager().beginTransaction().add(headlessFragment, FRAGMENT_TAG).commit();
        }


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                taskIsRunning = true;
                refreshLayout.setRefreshing(true);
                callFeedService();

            }
        });


        if (savedInstanceState != null) {
            allFeedItems = (ArrayList<FeedItem>) savedInstanceState.getSerializable(KEY_OBJECT);
            title = savedInstanceState.getString(KEY_TITLE);
            getSupportActionBar().setTitle(title);
        }
        adapter = new CustomFeedAdapter(MainActivity.this, allFeedItems);
        recyclerView.setAdapter(adapter);

        if (taskIsRunning || savedInstanceState == null) {
            refreshLayout.setRefreshing(true);
        } else {
            refreshLayout.setRefreshing(false);
        }

    }

    public void callFeedService() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Utils.webServiceFeedUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        taskIsRunning = false;
                        refreshLayout.setRefreshing(false);

                        Log.d(TAG, "onResponse " + response);

                        allFeedItems = parseResponseData(response);
                        adapter = new CustomFeedAdapter(MainActivity.this, allFeedItems);
                        recyclerView.setAdapter(adapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse " + error.getMessage());

                        taskIsRunning = false;
                        refreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Network error, please swipe down to refresh", Toast.LENGTH_LONG).show();

                    }
                });

//        AppController.getInstance().mRequestQueue.add(stringRequest);

        /**
         * Use Async Task If Volley Library is unresponsive or not working
         */

        new FeedAsyncTask(getApplicationContext(),Utils.webServiceFeedUrl).execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(KEY_OBJECT, allFeedItems);
        outState.putString(KEY_TITLE, "" + getSupportActionBar().getTitle());
    }

    /**
     * Show popup to confirm before closing the app
     */
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to close?");
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

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
                getSupportActionBar().setTitle(pageTitle);
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
            return null;
        }

        return allFeedItems;
    }


    BroadcastReceiver servreDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "onReceive " + action);

            if (action.equalsIgnoreCase(FeedAsyncTask.TASK_STARTED)) {
                taskIsRunning = true;
                refreshLayout.setRefreshing(true);
            } else if (action.equalsIgnoreCase(FeedAsyncTask.TASK_ENDED)) {
                taskIsRunning = false;
                refreshLayout.setRefreshing(false);
            } else if (action.equalsIgnoreCase(FeedAsyncTask.EXCEPTION)) {
                refreshLayout.setRefreshing(false);
                taskIsRunning = false;
                Toast.makeText(getApplicationContext(), "Netwok Error Please try again", Toast.LENGTH_LONG).show();
            } else if (action.equalsIgnoreCase(FeedAsyncTask.RESPONSE_TITLE)) {
                getSupportActionBar().setTitle(intent.getStringExtra(FeedAsyncTask.RESPONSE_TITLE));
            } else if (action.equalsIgnoreCase(FeedAsyncTask.RESPONSE)) {

                refreshLayout.setRefreshing(false);
                taskIsRunning = false;

                Bundle b = intent.getBundleExtra("bundle");
                allFeedItems = (ArrayList<FeedItem>) b.getSerializable("Obj");

                adapter = new CustomFeedAdapter(MainActivity.this, allFeedItems);
                recyclerView.setAdapter(adapter);
            }

        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFiler = new IntentFilter();
        intentFiler.addAction(FeedAsyncTask.EXCEPTION);
        intentFiler.addAction(FeedAsyncTask.RESPONSE_TITLE);
        intentFiler.addAction(FeedAsyncTask.RESPONSE);
        intentFiler.addAction(FeedAsyncTask.TASK_STARTED);
        intentFiler.addAction(FeedAsyncTask.TASK_ENDED);
        registerReceiver(servreDataReceiver, intentFiler);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(servreDataReceiver);
    }
}
