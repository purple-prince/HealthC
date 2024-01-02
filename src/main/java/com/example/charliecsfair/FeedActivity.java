package com.example.charliecsfair;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
import android.app.Fragment;
import android.app.FragmentManager;

import android.app.FragmentTransaction;
import android.os.Bundle;
//import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FeedActivity extends AppCompatActivity {

    BottomNavigationView tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        tabs = findViewById(R.id.bottomNavigationView);

        changeFragment(new FeedFragment());

        tabs.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.miFeed:
                    //changeFragment(new HomeFragment());
                    changeFragment(new FeedFragment());
                    break;
                case R.id.miSearch:
                    changeFragment(new SearchFragment());
                    break;
                case R.id.miAccount:
                    changeFragment(new AccountFragment());
                    break;
            }
            return true;
        });
    }

    public void changeFragment(Fragment newFragment) {
        FragmentManager viewController = getFragmentManager();
        FragmentTransaction transaction = viewController.beginTransaction();
        transaction.replace(R.id.flFragment, newFragment);
        transaction.commit();
    }
}