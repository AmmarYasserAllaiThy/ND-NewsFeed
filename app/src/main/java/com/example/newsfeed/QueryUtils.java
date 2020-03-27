package com.example.newsfeed;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String TAG = QueryUtils.class.getSimpleName();

    public static List<News> fetchNewsData(String requestUrl) {
        HttpHandler httpHandler = new HttpHandler();
        URL url = httpHandler.createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = httpHandler.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        return parseJSONData(jsonResponse);
    }

    private static List<News> parseJSONData(String JSON) {
        if (TextUtils.isEmpty(JSON)) return null;

        List<News> news = new ArrayList<>();

        try {
            JSONObject rootJsonObject = new JSONObject(JSON);
            JSONObject response = rootJsonObject.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                news.add(new News(
                        result.getString("type").equals("liveblog"),
                        result.getString("sectionName"),
                        result.getString("webPublicationDate"),
                        result.getString("webTitle"),
                        result.getString("webUrl")
                ));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Problem parsing the News JSON results", e);
        }

        return news;
    }
}