package com.example.newsfeed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<News>>,
        ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String
            LOG_TAG = MainActivity.class.getSimpleName() + "_DEBUG",
            GUARDIAN_REQUEST_URL = "http://content.guardianapis.com/search",
            API_KEY = "api-key",
            API_VALUE = "eb13dc4f-d12b-46cd-b2e7-07a51dec7f8b", //My API key. TODO: replace with yours
            SHOW_TAGS = "show-tags",
            CONTRIBUTOR = "contributor";

    private ProgressBar progress;
    private TextView tv;

    private NewsAdapter newsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = findViewById(R.id.progress);
        ListView listView = findViewById(R.id.listView);
        tv = findViewById(R.id.tv);

        // Configure Adapter, EmptyView, and ItemClickListener of listView
        newsAdapter = new NewsAdapter(this, new ArrayList<>());
        listView.setAdapter(newsAdapter);
        listView.setEmptyView(tv);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            News news = newsAdapter.getItem(position);
            if (news != null)
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(news.getWebUrl())));
        });

        tv.setOnClickListener(v -> populateList());

        populateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityReceiver.setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, int id) {
        if (isConnected) getSupportLoaderManager().initLoader(id, null, this);
        else {
            progress.setVisibility(View.GONE);
            tv.setText(getString(R.string.no_internet_connection));
            Log.d(LOG_TAG, getString(R.string.no_internet_connection));
        }
    }

    private void populateList() {
        progress.setVisibility(View.VISIBLE);
        onNetworkConnectionChanged(ConnectivityReceiver.isConnected(this), (int) (Math.random() * 100));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String pageSize = sharedPreferences.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default)
        );
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        String type = sharedPreferences.getString(
                getString(R.string.settings_type_key),
                getString(R.string.settings_type_default)
        );
        String sectionId = sharedPreferences.getString(
                getString(R.string.settings_section_id_key),
                getString(R.string.settings_section_id_default)
        );
        // extra TODO: Add sharedPreferences item for q (https://open-platform.theguardian.com/documentation/search)


        Uri uri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = uri.buildUpon();

        uriBuilder.appendQueryParameter(API_KEY, API_VALUE);
        uriBuilder.appendQueryParameter(SHOW_TAGS, CONTRIBUTOR);
        uriBuilder.appendQueryParameter(getString(R.string.settings_page_size_key), pageSize);
        uriBuilder.appendQueryParameter(getString(R.string.settings_order_by_key), orderBy);
        // extra TODO: Add query parameter for q
        if (!type.equals(getString(R.string.all)))
            uriBuilder.appendQueryParameter(getString(R.string.settings_type_key), type);
        if (!sectionId.equals(getString(R.string.all)))
            uriBuilder.appendQueryParameter(getString(R.string.settings_section_id_key), sectionId);

        Log.d(LOG_TAG, uriBuilder.toString());

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        Log.d(LOG_TAG, "onLoadFinished");

        newsAdapter.clear();
        if (data != null && !data.isEmpty()) newsAdapter.addAll(data);
        else Log.d(LOG_TAG, getString(R.string.no_data_found));
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        newsAdapter.clear();
    }
}