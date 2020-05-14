package com.rahimovjavlon1212.yourfeed.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rahimovjavlon1212.yourfeed.R;
import com.rahimovjavlon1212.yourfeed.adapters.MyPagerAdapter;
import com.rahimovjavlon1212.yourfeed.database.TopicsDatabase;
import com.rahimovjavlon1212.yourfeed.models.TopicModel;

import java.util.ArrayList;
import java.util.Objects;

import static com.rahimovjavlon1212.yourfeed.utils.Utils.DATABASE_NAME;

public class ExploreFragment extends Fragment {

    private ArrayList<TopicModel> topicModels;
    private TopicsDatabase topicsDatabase;

    public ExploreFragment() {
    }

    public ExploreFragment(Context context) {
        topicsDatabase = Room.databaseBuilder(context, TopicsDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        topicsDatabase = Room.databaseBuilder(Objects.requireNonNull(getContext()), TopicsDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        topicModels = new ArrayList<>();
        topicModels.addAll(topicsDatabase.getTopicDao().getTopics());


        if (topicModels.size() == 0) {
            view.findViewById(R.id.placeholderExploreFragment).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.placeholderExploreFragment).setVisibility(View.INVISIBLE);
            ViewPager viewPager = view.findViewById(R.id.viewPagerExploreFragment);
            MyPagerAdapter myPagerAdapter = new MyPagerAdapter(Objects.requireNonNull(getFragmentManager()), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, topicModels);
            viewPager.setAdapter(myPagerAdapter);
            TabLayout tabLayout = view.findViewById(R.id.tabLayoutExploreFragment);
            tabLayout.setupWithViewPager(viewPager);
            View tabItem = inflater.inflate(R.layout.tab_item, container, false);
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    tabItem = inflater.inflate(R.layout.tab_item, container, false);
                    tab.setCustomView(tabItem);
                    ((TextView) tabItem.findViewById(R.id.tabTitle)).setText(tab.getText());
                    if (i == 0) {
                        ((TextView) tabItem.findViewById(R.id.tabTitle)).setTextColor(Objects.requireNonNull(getContext()).getResources().getColor(android.R.color.white));
                        tabItem.setBackground(getContext().getDrawable(R.drawable.selected_tab_background));
                    } else {
                        ((TextView) tabItem.findViewById(R.id.tabTitle)).setTextColor(Objects.requireNonNull(getContext()).getResources().getColor(android.R.color.white));
                        tabItem.setBackground(getContext().getDrawable(R.drawable.unselected_tab_background));
                    }
                }
            }
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                    ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                    int tabChildCount = vgTab.getChildCount();
                    for (int i = 0; i < tabChildCount; i++) {
                        View tabViewChild = vgTab.getChildAt(i);
                        if (tabViewChild instanceof FrameLayout) {
                            ((TextView) tabViewChild.findViewById(R.id.tabTitle)).setTextColor(Objects.requireNonNull(getContext()).getResources().getColor(android.R.color.white));
                            tabViewChild.setBackground(getContext().getDrawable(R.drawable.selected_tab_background));
                        }
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                    ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                    int tabChildCount = vgTab.getChildCount();
                    for (int i = 0; i < tabChildCount; i++) {
                        View tabViewChild = vgTab.getChildAt(i);
                        if (tabViewChild instanceof FrameLayout) {
                            ((TextView) tabViewChild.findViewById(R.id.tabTitle)).setTextColor(Objects.requireNonNull(getContext()).getResources().getColor(android.R.color.white));
                            tabViewChild.setBackground(getContext().getDrawable(R.drawable.unselected_tab_background));
                        }
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

        return view;
    }
}
