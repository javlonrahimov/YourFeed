package com.rahimovjavlon1212.yourfeed.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
    private String SECTION_KEY = "576";
    private String PAGE_KEY = "156";
    private int page = 1;

    public WorkerFragment(TopicModel topicModel) {
        this.topicModel = topicModel;
    }

    public WorkerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewWorkerFragment);
        newsAdapter = new NewsAdapter();
        progressBar = view.findViewById(R.id.progressBarWorkerFragment);
        noData = view.findViewById(R.id.noDataWorkerFragment);
        noInternet = view.findViewById(R.id.noInternetWorkerFragment);
        noInternet.setOnClickListener(e -> {
            noInternet.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            if (isNetworkAvailable()) {
                newsAdapter.onMoreClicked = () -> makeOperationSearchQuery(topicModel.getSectionId(), page++, true);
                makeOperationSearchQuery(topicModel.getSectionId(), page++, false);
                noInternet.setVisibility(View.INVISIBLE);
            } else {
                noInternet.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        if (!isNetworkAvailable()) {
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            noInternet.setVisibility(View.VISIBLE);
            noData.setVisibility(View.INVISIBLE);
        } else {
            newsAdapter.onMoreClicked = () -> makeOperationSearchQuery(topicModel.getSectionId(), page++, true);
            makeOperationSearchQuery(topicModel.getSectionId(), page++, false);
        }
        return view;
    }

    @NonNull
    @Override
    public Loader<List<NewsModel>> onCreateLoader(int id, @Nullable Bundle args) {
        if (args != null) {
            return new NewsAsyncTaskLoader(Objects.requireNonNull(getContext()), "", args.getString(SECTION_KEY), args.getInt(PAGE_KEY));
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

    private void makeOperationSearchQuery(String query, int page, boolean fromMore) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SECTION_KEY, query);
        queryBundle.putInt(PAGE_KEY, page);
        LoaderManager loaderManager = Objects.requireNonNull(getActivity()).getSupportLoaderManager();
        int WORKER_LOADER_KEY = query.hashCode();
        if (fromMore) {
            loaderManager.restartLoader(WORKER_LOADER_KEY, queryBundle, this);
        } else
            loaderManager.initLoader(WORKER_LOADER_KEY, queryBundle, this);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
