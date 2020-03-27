package com.example.newsfeed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String GUARDIAN_REQUEST_URL = "http://content.guardianapis.com/search";

    private ProgressBar progress;

    private NewsAdapter newsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = findViewById(R.id.progress);
        ListView listView = findViewById(R.id.listView);
        TextView tv = findViewById(R.id.tv);

        // Configure Adapter, EmptyView, and ItemClickListener of listView
        newsAdapter = new NewsAdapter(this, new ArrayList<>());
        listView.setAdapter(newsAdapter);
        listView.setEmptyView(tv);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            News news = newsAdapter.getItem(position);
            if (news != null)
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(news.getWebUrl())));
        });

        // Check whether connected or not
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = conManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        // init loader if connected
        if (isConnected) getSupportLoaderManager().initLoader(0, null, this);
        else {
            // Show message if not connected
            progress.setVisibility(View.GONE);
            tv.setText(getString(R.string.no_internet_connection));
        }
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


        Uri uri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = uri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", "eb13dc4f-d12b-46cd-b2e7-07a51dec7f8b");
        uriBuilder.appendQueryParameter(getString(R.string.settings_page_size_key), pageSize);
        uriBuilder.appendQueryParameter(getString(R.string.settings_order_by_key), orderBy);
        if (!type.equals(getString(R.string.all)))
            uriBuilder.appendQueryParameter(getString(R.string.settings_type_key), type);
        if (!sectionId.equals(getString(R.string.all)))
            uriBuilder.appendQueryParameter(getString(R.string.settings_section_id_key), sectionId);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        newsAdapter.clear();
        if (data != null && !data.isEmpty()) newsAdapter.addAll(data);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        newsAdapter.clear();
    }
}