package com.example.earthquake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG_LIST_FRAGMENT = "TAG_LIST_FRAGMENT";

    EarthquakeListFragment mEarthquakeListFragment;

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


        // creating some dummy Earthquakes to test our app
        Date now = Calendar.getInstance().getTime();
        List<Earthquake> dummyQuakes = new ArrayList<>(0);
        dummyQuakes.add(new Earthquake("0", now, "San Jose", null, 7.3, null));
        dummyQuakes.add(new Earthquake("1", now, "LA", null, 6.5, null));

        mEarthquakeListFragment.setEarthquakes(dummyQuakes);
    }
}
