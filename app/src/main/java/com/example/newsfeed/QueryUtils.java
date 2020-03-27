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

    private static final String
            LOG_TAG = QueryUtils.class.getSimpleName() + "_DEBUG",
            RESPONSE = "response",
            RESULTS = "results",
            TYPE = "type",
            LIVE_BLOG = "liveblog",
            SECTION_NAME = "sectionName",
            TAGS = "tags",
            WEB_PUBLICATION_DATE = "webPublicationDate",
            WEB_TITLE = "webTitle",
            WEB_URL = "webUrl";

    public static List<News> fetchNewsData(String requestUrl) {
        HttpHandler httpHandler = new HttpHandler();
        URL url = httpHandler.createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = httpHandler.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return parseJSONData(jsonResponse);
    }

    private static List<News> parseJSONData(String JSON) {
        if (TextUtils.isEmpty(JSON)) {
            Log.d(LOG_TAG, "JSON is empty");
            return null;
        }

        List<News> news = new ArrayList<>();

        try {
            JSONArray results = new JSONObject(JSON).getJSONObject(RESPONSE).getJSONArray(RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                JSONArray tags = result.getJSONArray(TAGS);
                String authorName = null;

                if (tags.length() > 0) authorName = tags.getJSONObject(0).getString(WEB_TITLE);

                news.add(new News(
                        result.getString(TYPE).equals(LIVE_BLOG),
                        result.getString(SECTION_NAME),
                        authorName,
                        result.getString(WEB_PUBLICATION_DATE),
                        result.getString(WEB_TITLE),
                        result.getString(WEB_URL)
                ));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the News JSON results", e);
        }

        return news;
    }
}