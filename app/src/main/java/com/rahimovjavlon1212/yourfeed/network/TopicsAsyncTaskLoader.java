package com.rahimovjavlon1212.yourfeed.network;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

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
        String stringUrl = "https://content.guardianapis.com/sections?api-key=test";
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Toast.makeText(getContext(), "Something went wrong !!!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "Something went wrong !!!", Toast.LENGTH_SHORT).show();
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
            }else {
                Toast.makeText(getContext(), "Bad request !!!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getContext(), "Something went wrong !!!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "Something went wrong !!!", Toast.LENGTH_SHORT).show();
        }

        return new ArrayList<>(extractFeatureFromJSON(jsonResponse));
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}