package com.rahimovjavlon1212.yourfeed.network;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.rahimovjavlon1212.yourfeed.R;
import com.rahimovjavlon1212.yourfeed.models.TopicModel;

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

public class TopicsAsyncTaskLoader extends AsyncTaskLoader<List<TopicModel>> {

    public TopicsAsyncTaskLoader(@NonNull Context context) {
        super(context);
    }

    private URL createUrl() {
        URL url;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(getContext().getResources().getString(R.string.scheme))
                .authority(getContext().getResources().getString(R.string.authority))
                .appendPath(getContext().getResources().getString(R.string.sections))
                .appendQueryParameter("api-key", getContext().getResources().getString(R.string.api_key))
                .build();
        try {
            url = new URL(builder.toString());
        } catch (MalformedURLException exception) {
            Log.e("LOG_TAG", "Error with creating URL", exception);
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
            Log.d("LOG_TAG", e.toString());
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

    private ArrayList<TopicModel> extractFeatureFromJSON(String newsJSON) {
        ArrayList<TopicModel> resultList = new ArrayList<>();
        try {
            JSONObject baseJsonObject = new JSONObject(newsJSON);
            JSONObject response = baseJsonObject.getJSONObject("response");
            if (response.getString("status").equals("ok")) {
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject resultObject = results.getJSONObject(i);
                        String sectionName = resultObject.getString("webTitle");
                        String sectionId = resultObject.getString("id");
                        resultList.add(new TopicModel(0, sectionId, sectionName));
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("LOG_TAG", "Problem parsing the news JSON results", e);
        }

        return resultList;
    }

    @Nullable
    @Override
    public List<TopicModel> loadInBackground() {
        URL url = createUrl();
        String jsonResponse = "";
        try {
            if (url != null) {
                jsonResponse = makeHttpRequest(url);
            }
        } catch (IOException e) {
            Log.d("LOG_TAG", e.toString());
        }

        return new ArrayList<>(extractFeatureFromJSON(jsonResponse));
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}