package com.rahimovjavlon1212.yourfeed.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.yourfeed.R;
import com.rahimovjavlon1212.yourfeed.adapters.InterestsAdapter;

public class InterestsFragment extends Fragment {

    public InterestsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interests, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewInterestsFragment);
        InterestsAdapter interestsAdapter = new InterestsAdapter(getContext());
        recyclerView.setAdapter(interestsAdapter);
        return view;
    }

}
