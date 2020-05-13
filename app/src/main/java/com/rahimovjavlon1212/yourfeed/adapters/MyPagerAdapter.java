package com.rahimovjavlon1212.yourfeed.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.rahimovjavlon1212.yourfeed.fragments.WorkerFragment;
import com.rahimovjavlon1212.yourfeed.models.TopicModel;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<TopicModel> tabTitles;

    public MyPagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<TopicModel> topicModels) {
        super(fm, behavior);
        this.tabTitles = topicModels;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new WorkerFragment(tabTitles.get(position));
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position).getSectionName();
    }
}
