package com.example.earthquake;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

/*The App will be persisting instances of the Earthquake class, so  we annotate it with the @Entity annotation
* The id field is the primary key that cannot be null*/
@Entity // this defines the structure of the database that will be used to store instances of the annotated class
public class Earthquake
{
    /**class fields**/
    @NonNull
    @PrimaryKey
    private String mId;
    private Date mDate;
    private String mDetails;
    private Location mLocation;
    private double mMagnitude;
    private String mLink;

    // getters
    public String getId() { return mId; }
    public Date getDate() { return mDate; }
    public String getDetails() { return mDetails; }
    public Location getLocation() { return mLocation; }
    public double getMagnitude() { return mMagnitude; }
    public String getLink() { return mLink; }

    // constructor with 6 parameters
    public Earthquake(String id, Date date, String details,
                      Location location,
                      double magnitude, String link) {
        mId = id;
        mDate = date;
        mDetails = details;
        mLocation = location;
        mMagnitude = magnitude;
        mLink = link;
    }

    // Output the string that will be used to represent each earthquake in the earthquake list.
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm", Locale.US);
        String dateString = sdf.format(mDate);
        return dateString + ": " + mMagnitude + " " + mDetails;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Earthquake)
            return (((Earthquake)obj).getId().contentEquals(mId));
        else
            return false;
    }


}
