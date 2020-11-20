package com.example.earthquake;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.earthquake.Earthquake;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EarthquakeListFragment  extends Fragment
{
    // stores an array of Earthquakes
    private ArrayList<Earthquake> mEarthquakes = new ArrayList<>();

    private RecyclerView mRecyclerView; // getting reference to the recyclerView object

    // getting reference to the EarthquakeRecyclerViewAdapter and calling a new RecyclerViewAdapter
    // passing in the reference to the list of earthquakes
    private EarthquakeRecyclerViewAdapter mEarthquakeAdapter = new EarthquakeRecyclerViewAdapter(mEarthquakes);

    protected EarthquakeViewModel earthquakeViewModel;

    public EarthquakeListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {   // method use to inflate the layout ---fragment_earthquake_list
        View view = inflater.inflate(R.layout.fragment_earthquake_list, container, false);

        // getting the reference to the recyclerView to identify the recycler view layout in xml
        mRecyclerView = view.findViewById(R.id.list);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // Set the Recycler View adapter
        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mEarthquakeAdapter);
    }


    // This method takes a list of Earthquakes, checks for duplicates, and then adds each new
    // Earthquake to the Array List. It should also notify the Recycler View Adapter that a new
    // item has been inserted.
    public void setEarthquakes(List<Earthquake> earthquakes)
    {
        for (Earthquake earthquake: earthquakes)
        {
            if (!mEarthquakes.contains(earthquake)) // if the mEarthquake list variable does not
            { // contain the earthquake item, add the item to it.
                mEarthquakes.add(earthquake);
                // notify the Recycler view Adapter that a new item has been inserted
                mEarthquakeAdapter.notifyItemInserted(mEarthquakes.indexOf(earthquake));
            }
        }
    }

    /*Within the Earthquake List Fragment, update the onActivityCreated handler. Using
    the View Model Provider’s static of method to retrieve the current instance of your
    Earthquake View Model. Add an Observer to the Live Data returned from your View
    Model—it will set the Earthquake List Fragment Earthquake List when your Activity is
    created, and again whenever the list of parsed Earthquakes is updated:*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        // Retrieve the Earthquake View Model for the parent Activity.
        earthquakeViewModel = ViewModelProviders.of(getActivity()).get(EarthquakeViewModel.class);

        // Get the data from the View Model, adn observe any changes.
        earthquakeViewModel.getEarthquakes().observe(this, new Observer<List<Earthquake>>()
        {
            @Override
            public void onChanged(List<Earthquake> earthquakes)
            {
                // When the View Model changes, update the list
                if (earthquakes != null)
                    setEarthquakes(earthquakes);
            }
        });
    } // end method onActivityCreated
} // end class EarthquakeListFragment
