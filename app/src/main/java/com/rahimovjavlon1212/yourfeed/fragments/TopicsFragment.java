package com.rahimovjavlon1212.yourfeed.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.rahimovjavlon1212.yourfeed.R;
import com.rahimovjavlon1212.yourfeed.adapters.TopicsAdapter;
import com.rahimovjavlon1212.yourfeed.database.TopicsDatabase;
import com.rahimovjavlon1212.yourfeed.models.TopicModel;
import com.rahimovjavlon1212.yourfeed.network.TopicsAsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.rahimovjavlon1212.yourfeed.utils.Utils.DATABASE_NAME;

public class TopicsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<TopicModel>> {

    private RecyclerView recyclerView;
    private TopicsAdapter topicsAdapter;
    private ProgressBar progressBar;
    private TextView noInternet;
    private TextView noData;
    private TopicsDatabase topicsDatabase;

    public TopicsFragment() {
    }

    public TopicsFragment(Context context) {
        topicsDatabase = Room.databaseBuilder(context, TopicsDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTopicsFragment);
        topicsAdapter = new TopicsAdapter(getContext());
        progressBar = view.findViewById(R.id.progressBarTopicsFragment);
        noInternet = view.findViewById(R.id.noInternetTopicsFragment);
        noData = view.findViewById(R.id.noDataTopicsFragment);
        if (isNetworkAvailable()) {
            Objects.requireNonNull(getActivity()).getSupportLoaderManager().initLoader(2589, null, this);
            noInternet.setVisibility(View.INVISIBLE);
            noData.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            noInternet.setVisibility(View.VISIBLE);
            noInternet.setOnClickListener(e -> {
                if (isNetworkAvailable()) {
                    Objects.requireNonNull(getActivity()).getSupportLoaderManager().initLoader(2589, null, this);
                    noData.setVisibility(View.INVISIBLE);
                    noInternet.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });
        }
        return view;
    }

    @NonNull
    @Override
    public Loader<List<TopicModel>> onCreateLoader(int id, @Nullable Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        return new TopicsAsyncTaskLoader(Objects.requireNonNull(getContext()));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<TopicModel>> loader, List<TopicModel> data) {
        topicsAdapter.setData(getFilteredData(data));
        recyclerView.setAdapter(topicsAdapter);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<TopicModel>> loader) {

    }

    private List<TopicModel> getFilteredData(List<TopicModel> allData) {
        List<TopicModel> resultData = new ArrayList<>();
        List<TopicModel> dataInsideDatabase = topicsDatabase.getTopicDao().getTopics();
        List<String> strings= new ArrayList<>();
        for (int i = 0; i < dataInsideDatabase.size(); i++) {
            strings.add(dataInsideDatabase.get(i).getSectionId());
        }
        for (int i = 0; i < allData.size(); i++) {
            if (!strings.contains(allData.get(i).getSectionId())) {
                resultData.add(allData.get(i));
            }
        }

        if (resultData.isEmpty()) {
            noData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        return resultData;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
