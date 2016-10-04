package com.awhlee.contextualactionbar;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    MainActivityFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setTitle("Normal Mode");

        mMainFragment = new MainActivityFragment();
        mMainFragment.setActivity(this, toolbar);
        showFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // This is a settings item outside of multi select mode.
            return true;
        } else if (id == android.R.id.home) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // Returning false here means that the click will go down the chain
            // We want it to hit the main activity fragment.
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFragment() {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_activity_fragment, mMainFragment);
            transaction.commitAllowingStateLoss();
        } catch (IllegalStateException ise) {
            Log.e("Error", "MainActivity() - exception on commit: " + ise, ise);
        }
    }
}
