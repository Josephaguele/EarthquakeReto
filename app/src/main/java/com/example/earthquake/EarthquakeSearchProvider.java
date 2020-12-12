package com.example.earthquake;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* To begin searching the Earthquake project, we create the EarthquakeSearchProvider class that
 extends ContentProvider. It will be used exclusively to generate search suggestions for your
 Search View. The ContentProvider class has some abstract methods that must be implemented which
 include getType, query,insert, delete, and update methods.*/

public class EarthquakeSearchProvider extends ContentProvider
{
    @Override
    public boolean onCreate()
    {// Rather than accessing a SQlite database directly, we use the Room Database created to
        // perform searches. Confirm we can access it within the onCreate handler and return true
        EarthquakeDatabaseAccessor.getInstance(getContext().getApplicationContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        if (uriMatcher.match(uri) == SEARCH_SUGGESTIONS)
        {
            String searchQuery = "%" + uri.getLastPathSegment() + "%";

            EarthquakeDAO earthquakeDAO = EarthquakeDatabaseAccessor.getInstance(getContext()
                    .getApplicationContext()).earthquakeDAO();

            Cursor c = earthquakeDAO.generateSearchSuggestions(searchQuery);

            // Return a cursor of search suggestions.
            return c;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    { // Override the Content Provider's getType method to return the MIME type for search suggestions
        switch (uriMatcher.match(uri))
        {
            case SEARCH_SUGGESTIONS:
                return SearchManager.SUGGEST_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


    /* Adding a UriMatcher, which can be used to handle requests made using different URI pattersn.s
     Because we are using this Content Provider exclusively for search suggestions, you only need
     to include matches for those query types:*/
    private static final int SEARCH_SUGGESTIONS = 1;

    // Allocate the UriMatcher object, recognize search requests.
    private static final UriMatcher uriMatcher;
    static
    {
        uriMatcher = new UriMatcher (UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.professionalandroid.provider.earthquake",
                SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGESTIONS);
        uriMatcher.addURI("com.professionalandroid.provider.earthquake",
                SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGESTIONS);
        uriMatcher.addURI("com.professionalandroid.provider.earthquake",
                SearchManager.SUGGEST_URI_PATH_SHORTCUT, SEARCH_SUGGESTIONS);
        uriMatcher.addURI("com.professionalandroid.provider.earthquake",
                SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", SEARCH_SUGGESTIONS);
    }

}
