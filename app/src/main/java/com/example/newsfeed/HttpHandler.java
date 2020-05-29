package com.example.newsfeed;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpHandler {

    private static final String LOG_TAG = HttpHandler.class.getSimpleName() + "_DEBUG";

    public HttpHandler() {
    }

    public String makeHttpRequest(String url) throws IOException {
        URL _url = createUrl(url);
        if (_url != null) return makeHttpRequest(_url);
        return null;
    }

    public String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection httpURLCon = null;
        InputStream is = null;
        String jsonResponse = "";

        try {
            httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon.setRequestMethod("GET");
            httpURLCon.setReadTimeout(10000);
            httpURLCon.setConnectTimeout(15000);
            httpURLCon.connect();

            is = httpURLCon.getInputStream();
            jsonResponse = convertStreamToString(is);

        } catch (IOException e) {
            Log.d(LOG_TAG, "Problem in HTTP connection.", e);

        } finally {
            if (httpURLCon != null) httpURLCon.disconnect();
            if (is != null) is.close();
        }

        return jsonResponse;
    }

    public URL createUrl(String stringUrl) {
        try {
            return new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.d(LOG_TAG, "Problem in createUrl()", exception);
        }
        return null;
    }

    private String convertStreamToString(InputStream is) {
        // TODO: replace Scanner with BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = br.readLine()) != null) sb.append(line).append('\n');

        } catch (IOException e) {
            Log.d(LOG_TAG, "Problem in reading from BufferedReader.", e);

        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.d(LOG_TAG, "Problem in closing InputStream.", e);
            }
        }

        return sb.toString();
    }
}