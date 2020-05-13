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

public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsModel>> {
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private ProgressBar progressBar;
    private TextView noInternet;
    private TextView noData;
    private int page = 1;
    private int OPERATION_SEARCH_LOADER = 564;
    private String OPERATION_QUERY_URL_EXTRA = "56";
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
        noData.setVisibility(View.INVISIBLE);
        noInternet.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        if (!isNetworkAvailable()) {
            recyclerView.setVisibility(View.INVISIBLE);
            noInternet.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            noInternet.setOnClickListener(e -> {
                noInternet.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                if (isNetworkAvailable()) {
                    getActivity().getSupportLoaderManager().initLoader(OPERATION_SEARCH_LOADER, null, this);
                } else {
                    noInternet.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            getActivity().getSupportLoaderManager().initLoader(OPERATION_SEARCH_LOADER, null, this);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                isNewQuery = true;
                if (!isNetworkAvailable()) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noInternet.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    noInternet.setOnClickListener(e -> {
                        noInternet.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        if (isNetworkAvailable()) {
                            makeOperationSearchQuery(query, page++);
                        } else {
                            noInternet.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    makeOperationSearchQuery(query, page++);
                    recyclerView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                }
                newsAdapter.onMoreClicked = () -> {
                    isNewQuery = false;
                    if (!isNetworkAvailable()) {
                        recyclerView.setVisibility(View.INVISIBLE);
                        noInternet.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        noInternet.setOnClickListener(e -> {
                            noInternet.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            if (isNetworkAvailable()) {
                                makeOperationSearchQuery(query, page++);
                                noInternet.setVisibility(View.INVISIBLE);
                            } else {
                                noInternet.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        noInternet.setVisibility(View.INVISIBLE);
                        makeOperationSearchQuery(query, page++);
                    }
                };
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
        if (args != null) {
            return new NewsAsyncTaskLoader(getContext(), args.getString(OPERATION_QUERY_URL_EXTRA, ""), "", args.getInt("KEY"));
        }
        return new NewsAsyncTaskLoader(getContext(), "", "", page++);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsModel>> loader, List<NewsModel> data) {
        if (data.isEmpty()) {
            noData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            noData.setVisibility(View.INVISIBLE);
            if (isNewQuery) {
                newsAdapter.setData(data);
            } else {
                newsAdapter.addData(data);
            }
        }
        noInternet.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(newsAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        if (page > 1) {
            recyclerView.scrollToPosition(newsAdapter.startPosition - 1);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsModel>> loader) {

    }

    private void makeOperationSearchQuery(String query, int page) {
        noData.setVisibility(View.INVISIBLE);
        Bundle queryBundle = new Bundle();
        queryBundle.putString(OPERATION_QUERY_URL_EXTRA, query);
        queryBundle.putInt("KEY", page);
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(OPERATION_SEARCH_LOADER);
        if (loader == null) {
            loaderManager.initLoader(OPERATION_SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(OPERATION_SEARCH_LOADER, queryBundle, this);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
