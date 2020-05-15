package com.rahimovjavlon1212.yourfeed.network;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.rahimovjavlon1212.yourfeed.R;
import com.rahimovjavlon1212.yourfeed.models.NewsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NewsAsyncTaskLoader extends AsyncTaskLoader<List<NewsModel>> {

    private String sectionUrl;
    private String searchUrl;
    private int page;

    public NewsAsyncTaskLoader(@NonNull Context context, String searchUrl, String sectionUrl, int page) {
        super(context);
        this.searchUrl = searchUrl;
        this.sectionUrl = sectionUrl;
        this.page = page;
    }

    private URL createUrlSearch(String query, String page) {
        query = query.trim();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(getContext().getResources().getString(R.string.scheme))
                .authority(getContext().getResources().getString(R.string.authority))
                .appendPath(getContext().getResources().getString(R.string.search_path))
                .appendQueryParameter("format", getContext().getResources().getString(R.string.json))
                .appendQueryParameter("show-fields", getContext().getResources().getString(R.string.show_fields))
                .appendQueryParameter("order-by", getContext().getResources().getString(R.string.order_relevance))
                .appendQueryParameter("api-key", getContext().getResources().getString(R.string.api_key))
                .appendQueryParameter("page", page)
                .appendQueryParameter("q", query)
                .build();
        URL url;
        try {
            url = new URL(builder.toString());
        } catch (MalformedURLException exception) {
            return null;
        }
        return url;
    }

    private URL createUrlExplore(String section, String page) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(getContext().getResources().getString(R.string.scheme))
                .authority(getContext().getResources().getString(R.string.authority))
                .appendPath(getContext().getResources().getString(R.string.search_path))
                .appendQueryParameter("format", getContext().getResources().getString(R.string.json))
                .appendQueryParameter("show-fields", getContext().getResources().getString(R.string.show_fields))
                .appendQueryParameter("order-by", getContext().getResources().getString(R.string.order_newest))
                .appendQueryParameter("api-key", getContext().getResources().getString(R.string.api_key))
                .appendQueryParameter("page", page)
                .appendQueryParameter("section", section)
                .build();
        URL url;
        try {
            url = new URL(builder.toString());
        } catch (MalformedURLException exception) {
            return null;
        }
        return url;
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            return jsonResponse;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private ArrayList<NewsModel> extractFeatureFromJSON(String newsJSON) {
        ArrayList<NewsModel> resultList = new ArrayList<>();
        if (newsJSON.isEmpty()) {
            return resultList;
        }
        try {
            JSONObject baseJsonObject = new JSONObject(newsJSON);
            JSONObject response = baseJsonObject.getJSONObject("response");
            if (response.getString("status").equals("ok")) {
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject resultObject = results.getJSONObject(i);
                        JSONObject fields = resultObject.getJSONObject("fields");

                        String date = resultObject.getString("webPublicationDate");
                        String url = resultObject.getString("webUrl");
                        String sectionName = resultObject.getString("sectionName");
                        String imgUrl;
                        try {
                            imgUrl = fields.getString("thumbnail");
                        } catch (JSONException e) {
                            imgUrl = "";
                        }
                        String title = fields.getString("headline");
                        resultList.add(new NewsModel(title, date, imgUrl, url, sectionName));
                        if (i==results.length()-1){
                            return resultList;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            return resultList;
        }
        return resultList;
    }

    @Nullable
    @Override
    public List<NewsModel> loadInBackground() {
        URL url;
        if (!sectionUrl.isEmpty()) {
            url = createUrlExplore(sectionUrl, String.valueOf(page));
        } else {
            url = createUrlSearch(searchUrl, String.valueOf(page));
        }
        String jsonResponse = "";
        try {
            if (url != null) {
                jsonResponse = makeHttpRequest(url);
            }
        } catch (IOException e) {
            return extractFeatureFromJSON(jsonResponse);
        }
        return extractFeatureFromJSON(jsonResponse);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}