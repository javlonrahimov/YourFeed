package com.rahimovjavlon1212.yourfeed;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.rahimovjavlon1212.yourfeed.fragments.ExploreFragment;
import com.rahimovjavlon1212.yourfeed.fragments.InterestsFragment;
import com.rahimovjavlon1212.yourfeed.fragments.SearchFragment;
import com.rahimovjavlon1212.yourfeed.fragments.TopicsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerForFragment, new ExploreFragment(getApplicationContext())).commit();
        ((ChipNavigationBar) findViewById(R.id.navigationBar)).setItemSelected(R.id.exploreMenu, true);
        ((ChipNavigationBar) findViewById(R.id.navigationBar)).setOnItemSelectedListener(id -> {
            switch (id) {
                case R.id.exploreMenu: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerForFragment, new ExploreFragment(getApplicationContext())).commit();
                    break;
                }
                case R.id.searchMenu: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerForFragment, new SearchFragment()).commit();
                    break;
                }
                case R.id.topicsMenu: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerForFragment, new TopicsFragment(getApplicationContext())).commit();
                    break;
                }
                case R.id.interestsMenu: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerForFragment, new InterestsFragment()).commit();
                    break;
                }
            }
        });
    }
}
