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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.yourfeed.R;
import com.rahimovjavlon1212.yourfeed.adapters.NewsAdapter;
import com.rahimovjavlon1212.yourfeed.models.NewsModel;
import com.rahimovjavlon1212.yourfeed.network.NewsAsyncTaskLoader;

import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsModel>> {
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private ProgressBar progressBar;
    private TextView noInternet;
    private TextView noData;
    private int page = 1;
    private int SEARCH_LOADER_KEY = 564;
    private String QUERY_KEY = "576";
    private String PAGE_KEY = "156";
    private boolean isNewQuery = false;

    public SearchFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewSearchFragment);
        newsAdapter = new NewsAdapter();
        SearchView searchView = view.findViewById(R.id.searchViewSearchFragment);
        progressBar = view.findViewById(R.id.progressBarSearchFragment);
        noData = view.findViewById(R.id.noDataSearchFragment);
        noInternet = view.findViewById(R.id.noInternetSearchFragment);
        noInternet.setOnClickListener(e -> {
            if (isNetworkAvailable()) {
                Objects.requireNonNull(getActivity()).getSupportLoaderManager().initLoader(SEARCH_LOADER_KEY, null, this);
                noInternet.setVisibility(View.INVISIBLE);
            }
        });
        if (!isNetworkAvailable()) {
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            noInternet.setVisibility(View.VISIBLE);
            noData.setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(getActivity()).getSupportLoaderManager().initLoader(SEARCH_LOADER_KEY, null, this);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                isNewQuery = true;
                if (!isNetworkAvailable()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    noInternet.setVisibility(View.VISIBLE);
                } else {
                    makeOperationSearchQuery(query, page++);
                }
                newsAdapter.onMoreClicked = () -> {
                    isNewQuery = false;
                    if (!isNetworkAvailable()) {
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        makeOperationSearchQuery(query, page++);
                    }
                };
                recyclerView.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Loader<List<NewsModel>> onCreateLoader(int id, @Nullable Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        if (args != null) {
            return new NewsAsyncTaskLoader(Objects.requireNonNull(getContext()), args.getString(QUERY_KEY, ""), "", args.getInt(PAGE_KEY));
        }
        return new NewsAsyncTaskLoader(Objects.requireNonNull(getContext()), "", "", page++);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsModel>> loader, List<NewsModel> data) {
        if (data.isEmpty()) {
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            noData.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            if (isNewQuery) {
                newsAdapter.setData(data);
            } else {
                newsAdapter.addData(data);
            }
            recyclerView.setAdapter(newsAdapter);
            if (page > 1) {
                recyclerView.scrollToPosition(newsAdapter.startPosition - 1);
            }
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsModel>> loader) {
    }

    private void makeOperationSearchQuery(String query, int page) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(QUERY_KEY, query);
        queryBundle.putInt(PAGE_KEY, page);
        LoaderManager loaderManager = Objects.requireNonNull(getActivity()).getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(SEARCH_LOADER_KEY);
        if (loader == null) {
            loaderManager.initLoader(SEARCH_LOADER_KEY, queryBundle, this);
        } else {
            loaderManager.restartLoader(SEARCH_LOADER_KEY, queryBundle, this);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
