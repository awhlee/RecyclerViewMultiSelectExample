package com.awhlee.contextualactionbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class MainActivityFragment extends Fragment {
    RVAdapter mAdapter;
    MultiSelectManager mMultiSelectManager;

    public MainActivityFragment() {}

    public void setActivity(AppCompatActivity activity, Toolbar toolbar) {
        setHasOptionsMenu(true);
        mAdapter = new RVAdapter(activity);
        mMultiSelectManager = new MultiSelectManager(this, mAdapter, toolbar);
        mAdapter.setMultiSelectManager(mMultiSelectManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.other_settings) {
            // This is an action that we exposed in multi select mode so let the mulit select
            // manager deal with it with its underlying selected items list.
            mMultiSelectManager.doOtherSettings();
            return true;
        } else if (id == android.R.id.home) {
            // The user hit the back button so let's clear the multi select state.
            mMultiSelectManager.setIsMultiSelect(false);
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Set up the recycler view.
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.main_recycler_view);
        rv.setAdapter(mAdapter);

        return view;
    }
}
