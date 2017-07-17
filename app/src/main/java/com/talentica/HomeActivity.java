package com.talentica;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.talentica.locationDetection.R;

public class HomeActivity extends AppCompatActivity implements HomeFragment.Callback {

    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_activity);
        rootView = findViewById(R.id.homeActivityContainer);
        addHomeFragment();
    }

    void addHomeFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.homeActivityContainer, HomeFragment.newInstance(), "home_fragment");
        transaction.commit();
    }

    void replaceFragment(Fragment newFragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.homeActivityContainer, newFragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
