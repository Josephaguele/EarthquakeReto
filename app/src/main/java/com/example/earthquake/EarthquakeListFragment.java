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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EarthquakeListFragment  extends Fragment
{
    private ArrayList<Earthquake> mEarthquakes = new ArrayList<>();

    private RecyclerView mRecyclerView; // getting reference to the recyclerView object

    // getting reference to the EarthquakeRecyclerViewAdapter and calling a new RecyclerViewAdapter
    // passing in the reference to the list of earthquakes
    private EarthquakeRecyclerViewAdapter mEarthquakeAdapter = new EarthquakeRecyclerViewAdapter(mEarthquakes);

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
    {
        View view = inflater.inflate(R.layout.fragment_earthquake_list, container, false);

        // getting the recyclerView to identify the recycler view layout in xml
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
            if (!mEarthquakes.contains(earthquake)) // if the mEarthquake variable does not contain
            { // the earthquake item, add the item to it.
                mEarthquakes.add(earthquake);
                mEarthquakeAdapter.notifyItemInserted(mEarthquakes.indexOf(earthquake));
            }
        }
    }
}
