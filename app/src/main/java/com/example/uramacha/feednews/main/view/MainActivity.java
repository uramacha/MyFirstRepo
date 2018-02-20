package com.example.uramacha.feednews.main.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.uramacha.feednews.R;
import com.example.uramacha.feednews.adapter.CustomFeedAdapter;
import com.example.uramacha.feednews.adapter.FeedItem;
import com.example.uramacha.feednews.fragments.HeadlessFragment;
import com.example.uramacha.feednews.main.presenter.MainPresenter;
import com.example.uramacha.feednews.main.presenter.MainPresenterImpl;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.uramacha.feednews.Utils.FRAGMENT_TAG;
import static com.example.uramacha.feednews.Utils.KEY_OBJECT;
import static com.example.uramacha.feednews.Utils.KEY_TITLE;
import static com.example.uramacha.feednews.Utils.isConnected;
import static com.example.uramacha.feednews.Utils.taskIsRunning;

/**
 * Main class for the News Feed Display in the screen
 */

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements MainView, SwipeRefreshLayout.OnRefreshListener {

    private String TAG = getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private ArrayList<FeedItem> allFeedItems = new ArrayList<>();
    private String title = "";
    private CustomFeedAdapter adapter;

    MainPresenter mainPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeds_activity);

        ButterKnife.bind(this);

        if (!isConnected(this)) {
            Toast.makeText(this, R.string.please_connect, Toast.LENGTH_SHORT).show();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        refreshLayout.setOnRefreshListener(this);


        HeadlessFragment headlessFragment = (HeadlessFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (headlessFragment == null) {
            headlessFragment = new HeadlessFragment();
            getSupportFragmentManager().beginTransaction().add(headlessFragment, FRAGMENT_TAG).commit();
        }


        mainPresenter = new MainPresenterImpl(this);

        if (taskIsRunning) {
            mainPresenter.setRefreshLayoutVisibility(true);
        } else {
            mainPresenter.setRefreshLayoutVisibility(false);
        }

    }


    /**
     * Restore the state of the view
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        allFeedItems = (ArrayList<FeedItem>) savedInstanceState.getSerializable(KEY_OBJECT);
        title = savedInstanceState.getString(KEY_TITLE);
        getSupportActionBar().setTitle(title);
    }


    /**
     * Save the state of the view
     *
     * @param outState
     */
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

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new CustomFeedAdapter(MainActivity.this, allFeedItems);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void showRefreshLayout() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshLayout() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void loadNewsFeed(ArrayList<FeedItem> allItems) {

        if (allItems == null || allItems.size() == 0) {
            showNetworkError();
        }

        allFeedItems = allItems;
        adapter = new CustomFeedAdapter(MainActivity.this, allFeedItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void setTaskStatus(boolean status) {
        taskIsRunning = status;
    }


    @Override
    public void startTask() {
        mainPresenter.startTask();
    }


    @Override
    public void onRefresh() {
        mainPresenter.startTask();
    }

    @Override
    public void updateActionBar(String str) {
        getSupportActionBar().setTitle(str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDestroy();
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
    }


}
