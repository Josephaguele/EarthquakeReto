package com.example.earthquake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EarthquakeListFragment.OnListFragmentInteractionListener
{
    private static final String TAG_LIST_FRAGMENT = "TAG_LIST_FRAGMENT";
    private static final int MENU_PREFERENCES = Menu.FIRST+1;
    private static final int SHOW_PREFERENCES  = 1;


    EarthquakeListFragment mEarthquakeListFragment;
    EarthquakeViewModel earthquakeViewModel; // making reference to EarthquakeViewModel class



    @Override
    protected void onCreate(Bundle savedInstanceState)
    { super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_main);

        FragmentManager fm = getSupportFragmentManager();

        /*In order to maintain a consistent UI state between configuration changes, all the Fragments added
         to your UI will automatically be restored when an Activity is re-created following an orientation
        change or unexpected termination.
        This is particularly important if you are populating your Activity layout with Fragments within
        the onCreate handler—in which case you must check if the Fragments have already been added to
        avoid creating multiple copies.
        You can do this either by checking for Fragments before adding them, or if this is an Activity restart
        by checking if the savedInstanceState is null:*/
        if (savedInstanceState == null)
        {
            FragmentTransaction ft = fm.beginTransaction();

            mEarthquakeListFragment = new EarthquakeListFragment();
            ft.add(R.id.main_activity_frame, mEarthquakeListFragment,TAG_LIST_FRAGMENT);

            ft.commitNow();
        } else
        {
            mEarthquakeListFragment =(EarthquakeListFragment) fm.findFragmentByTag(TAG_LIST_FRAGMENT);
        }

        // Retrieve the Earthquake View Model for this Activity.
        earthquakeViewModel = ViewModelProviders.of(this).get(EarthquakeViewModel.class);
    } // end method onCreate

    @Override
    public void onListFragmentRefreshRequested()
    {
        updateEarthquakes();
    }

    private void updateEarthquakes()
    {
        // Request the ViewModel update the earthquakes from the USGS feed.
        earthquakeViewModel.loadEarthquakes();
    }

    // ADDING MENU ITEM TO DISPLAY THE PREFERENCES ACTIVITY. This will open the Preferences Activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Use the Search Manager to find the SearchableInfo related
        // to the Search Result Activity
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(
                new ComponentName(getApplicationContext(),
                        EarthquakeSearchResultActivity.class));

        SearchView searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(searchableInfo);
        searchView.setIconifiedByDefault(false);

        return true;
    }


    /*Override the onOptionsItemSelected method to display the PreferencesActivity when
    the new Menu Item from Step 11 is selected. To launch the Preferences Activity, create an
    explicit Intent, and pass it in to the startActivityForResult method. This will launch the
    Activity and alert the EarthquakeMainActivity class when the Preferences Activity is finished
    via the onActivityResult handler.*/
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.settings_menu_item:
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivityForResult(intent, SHOW_PREFERENCES);
                return true;
        }
        return false;
    }

} // end class MainActivity
