package com.awhlee.contextualactionbar;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MultiSelectManager {
    RVAdapter mAdapter;
    MainActivityFragment mFragment;
    Toolbar mToolbar;
    boolean mEnabled = false;

    List<RVAdapter.ListInfo> mSelected = new ArrayList<>();

    public MultiSelectManager(MainActivityFragment fragment, RVAdapter adapter, Toolbar toolbar) {
        mFragment = fragment;
        mAdapter = adapter;
        mToolbar = toolbar;
    }

    /**
     * Switch modes to/from multi select mode.
     * @param enabled
     */
    public void setIsMultiSelect(boolean enabled) {
        mEnabled = enabled;
        mSelected.clear();

        if (!mEnabled) {
            // Do some application wide mode adjustment
            // Note that we don't need to set the back button since the MainActivity has already
            // done this for us.
            setToolbarTitle("Normal Mode");
            loadMenu(R.menu.menu_main);

            // Ask the adapter to redraw the visible items based ont he change of mode
            mAdapter.redrawVisibleItems();
        } else {
            setToolbarTitle("");

            // Change the back button to the back arrow
            AppCompatActivity activity = (AppCompatActivity)mFragment.getActivity();
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);

            loadMenu(R.menu.menu_main2);
        }
        mAdapter.redrawVisibleItems();
    }

    private void setToolbarTitle(String title) {
        mFragment.getActivity().setTitle(title);
    }

    /**
     * Load the specified menu
     * @param menuId
     */
    private void loadMenu(int menuId) {
        Menu menu = mToolbar.getMenu();
        menu.clear();
        mFragment.getActivity().getMenuInflater().inflate(menuId, menu);
    }

    /**
     * Are we in multi select mode?
     * @return
     */
    public boolean isMultiSelect() {
        return mEnabled;
    }

    /**
     * Return if a data item is part of the selected list.
     * @param info
     * @return
     */
    public boolean isSelected(RVAdapter.ListInfo info) {
        return mSelected.contains(info);
    }

    /**
     * Add a data item to the list of selected items
     * @param info
     */
    public void add(RVAdapter.ListInfo info) {
        mSelected.add(info);
    }

    /**
     * Implement the action exposed by the multi select menu
     */
    public void doOtherSettings() {
        StringBuffer builder = new StringBuffer();
        builder.append("Doing other settings with ");
        builder.append(mSelected.size());
        builder.append(" items");

        Toast.makeText(mFragment.getActivity(), builder.toString(), Toast.LENGTH_SHORT).show();
    }
}
