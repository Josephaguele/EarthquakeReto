package com.example.earthquake;

import android.app.Application;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
    public void loadEarthquakes()
    {
        new AsyncTask<Void, Void, List<Earthquake>>()
        {
            @Override
            protected List<Earthquake> doInBackground(Void... voids) {
                // Result ArrayList of parsed earthquakes.
                ArrayList<Earthquake> earthquakes = new ArrayList<>(0);

                // Get the XML
                URL url;
                try
                {
                    String quakeFeed = getApplication().getString(R.string.earthquake_feed);
                    url = new URL (quakeFeed);

                    URLConnection connection = url.openConnection();

                    HttpURLConnection httpConnection = (HttpURLConnection)connection;
                    int responseCode  = httpConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK)
                    {
                        InputStream in = httpConnection.getInputStream();

                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db = dbf.newDocumentBuilder();

                        // Parse teh earthquake feed.
                        Document dom = db.parse(in);
                        Element docEle = dom.getDocumentElement();

                        // Get a list of each earthquake entry.
                        NodeList nl = docEle.getElementsByTagName("entry");
                        if (nl != null && nl.getLength() > 0)
                        {
                            for (int i = 0; i < nl.getLength(); i++)
                            {
                                /// Check to se if our loading has been cancelled, in which case
                                // return what we have so far
                                if (isCancelled())
                                {
                                    Log.d(TAG,"Loading cancelled");
                                    return earthquakes;
                                }
                                Element entry = (Element)nl.item(i);
                                Element id = (Element) entry.getElementsByTagName("id").item(0);
                                Element title = (Element) entry.getElementsByTagName("title").item(0);
                                Element g = (Element) entry.getElementsByTagName("georss:point").item(0);
                                Element when = (Element) entry.getElementsByTagName("updated").item(0);
                                Element link = (Element)entry.getElementsByTagName("link").item(0);

                                String idString = id.getFirstChild().getNodeValue();
                                String details = title.getFirstChild().getNodeValue();
                                String hostname = "http://earthquake.usgs.gov";
                                String linkString = hostname + link.getAttribute("href");
                                String point = g.getFirstChild().getNodeValue();
                                String dt = when.getFirstChild().getNodeValue();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                                Date qdate = new GregorianCalendar(0,0,0).getTime();
                                try
                                {
                                    qdate = sdf.parse(dt);
                                }catch (ParseException e)
                                {
                                    Log.e(TAG, "Date parsing exception.", e);
                                }

                                String[] location = point.split(" ");
                                Location l = new Location("dummyGPS");
                                l.setLatitude(Double.parseDouble(location[0]));
                                l.setLongitude(Double.parseDouble(location[1]));

                                String magnitudeString = details.split(" ")[1];
                                int end = magnitudeString.length() - 1;
                                double magnitude = Double.parseDouble(magnitudeString.substring(0, end));

                                if (details.contains("-"))
                                    details = details.split("-")[1].trim();
                                else
                                    details = "";
                                final Earthquake earthquake = new Earthquake(idString, qdate, details, l, magnitude, linkString);

                                // Add the new earthquake to our result array.
                                earthquakes.add(earthquake);

                            }
                        }
                    }httpConnection.disconnect();
                } catch (MalformedURLException e)
                {
                    Log.e(TAG,"MalformedURLException", e);
                } catch (IOException e)
                {
                    Log.e(TAG, "IOException", e);
                }catch (ParserConfigurationException e)
                {
                    Log.e(TAG,"Parse Configuration Exception", e);
                }catch (SAXException e)
                {
                    Log.e(TAG,"SAX Exception", e);
                }
                // Return our result array.
                return earthquakes;
            }

            @Override
            protected void onPostExecute(List<Earthquake> data) {
                // Update the Live Data with the new list.
                earthquakes.setValue(data);
            }
        }.execute();
    }
}
