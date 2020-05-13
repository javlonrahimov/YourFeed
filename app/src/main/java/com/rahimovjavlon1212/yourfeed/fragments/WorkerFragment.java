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

import com.rahimovjavlon1212.yourfeed.R;
import com.rahimovjavlon1212.yourfeed.adapters.NewsAdapter;
import com.rahimovjavlon1212.yourfeed.models.NewsModel;
import com.rahimovjavlon1212.yourfeed.models.TopicModel;
import com.rahimovjavlon1212.yourfeed.network.NewsAsyncTaskLoader;

import java.util.List;
import java.util.Objects;


public class WorkerFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsModel>> {

    private TopicModel topicModel;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private ProgressBar progressBar;
    private TextView noInternet;
    private TextView noData;
    private int page = 1;

    public WorkerFragment(TopicModel topicModel) {
        this.topicModel = topicModel;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewWorkerFragment);
        progressBar = view.findViewById(R.id.progressBarWorkerFragment);
        noData = view.findViewById(R.id.noDataWorkerFragment);
        noInternet = view.findViewById(R.id.noInternetWorkerFragment);
        noData.setVisibility(View.INVISIBLE);
        noInternet.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        newsAdapter = new NewsAdapter();
        Log.d("HELLO", isNetworkAvailable() + "");
        if (!isNetworkAvailable()) {
            recyclerView.setVisibility(View.INVISIBLE);
            noInternet.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            noInternet.setOnClickListener(e -> {
                noInternet.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                if (isNetworkAvailable()) {
                    newsAdapter.onMoreClicked = () -> makeOperationSearchQuery(topicModel.getSectionId(), page++);
                    makeOperationSearchQuery(topicModel.getSectionId(), page++);
                    noInternet.setVisibility(View.INVISIBLE);
                } else {
                    noInternet.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            noInternet.setVisibility(View.INVISIBLE);
            newsAdapter.onMoreClicked = () -> makeOperationSearchQuery(topicModel.getSectionId(), page++);
            makeOperationSearchQuery(topicModel.getSectionId(), page++);
        }
        return view;
    }

    @NonNull
    @Override
    public Loader<List<NewsModel>> onCreateLoader(int id, @Nullable Bundle args) {
        if (args != null) {
            return new NewsAsyncTaskLoader(Objects.requireNonNull(getContext()), "", args.getString("KEY1"), args.getInt("KEY2"));
        }
        return new NewsAsyncTaskLoader(Objects.requireNonNull(getContext()), "", topicModel.getSectionId(), page++);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsModel>> loader, List<NewsModel> data) {
        if (data.isEmpty()) {
            noData.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            newsAdapter.addData(data);
            progressBar.setVisibility(View.INVISIBLE);
            noInternet.setVisibility(View.INVISIBLE);
            recyclerView.setAdapter(newsAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (page > 1) {
            recyclerView.scrollToPosition(newsAdapter.startPosition - 1);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsModel>> loader) {

    }

    private void makeOperationSearchQuery(String query, int page) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString("KEY1", query);
        queryBundle.putInt("KEY2", page);
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(123);
        if (loader == null) {
            loaderManager.initLoader(123, queryBundle, this);
        } else {
            loaderManager.restartLoader(123, queryBundle, this);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
