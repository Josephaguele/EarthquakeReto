package com.example.earthquake;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/*A new EarthquakeViewModel that extends AndroidViewModel and includes a
MutableLiveData variable that represents a List of Earthquakes. This View Model will be
cached and maintained across configuration changes. Create a getEarthquakes method that
will check if our Earthquake List Live Data has been populated already, and if not, will load
the Earthquakes from the feed:*/
public class EarthquakeViewModel extends AndroidViewModel
{
    private static final String TAG = "EarthquakeUpdate";
    private MutableLiveData<List<Earthquake>> earthquakes;

    public EarthquakeViewModel(Application application)
    {
        super(application);
    }

    public LiveData<List<Earthquake>> getEarthquakes()
    {
        if (earthquakes == null)
        {
            earthquakes = new MutableLiveData<List<Earthquake>>();
            loadEarthquakes();
        }
       return earthquakes;
    }

    // Asynchronously load the Earthquakes from the feed.
    public void loadEarthquakes(){}
}
