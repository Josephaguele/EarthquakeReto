package com.example.earthquake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeSearchResultActivity extends AppCompatActivity
{
    private ArrayList<Earthquake> mEarthquakes = new ArrayList<>();

    private EarthquakeRecyclerViewAdapter mEarthquakeAdapter = new EarthquakeRecyclerViewAdapter(mEarthquakes);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_search_result);

        RecyclerView recyclerView = findViewById(R.id.search_result_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mEarthquakeAdapter);

    }

    /*Return to the Search Results Activity, and add a new Live Data Observer that will update
    the Array List of Earthquakes displayed by the Recycler View. Also create a new Mutable
    Live Data that will store the current search query, and a setSearchQuery method that will
    modify that query*/
    MutableLiveData<String> searchQuery;

    private void setSearchQuery(String query)
    {
        searchQuery.setValue(query);
    }

    private final Observer<List<Earthquake>> searchQueryResultObserver = updatedEarthquakes ->
    {
        // Update the UI with the updated search query results.
        mEarthquakes. clear();
        if (updatedEarthquakes != null)
            mEarthquakes.addAll(updatedEarthquakes);
        mEarthquakeAdapter.notifyDataSetChanged();
    };

}
