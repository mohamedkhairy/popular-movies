package com.a5airi.popularmovies.moviesDB;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MoviesContentProvider extends ContentProvider{

    public static final int favoriteT = 100;
    public static final int favoriteT_ID = 101;

//    public static final int reviewsT = 200;
//    public static final int reviewsT_ID = 201;
//
//    public static final int trailerT = 300;
//    public static final int trailerT_ID = 301;

    private static final UriMatcher MoviesUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_FAVORITES, favoriteT);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_FAVORITES + "/#", favoriteT_ID);

        return uriMatcher;
    }


    private MoviesDbHelper moviesDbHelper;
    @Override
    public boolean onCreate() {
        Log.d("base" , "CP 1111");
        Context context = getContext();
        moviesDbHelper = new MoviesDbHelper(context);
        Log.d("base" , "CP 22222");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();

        int match = MoviesUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case favoriteT:
                // Insert new values into the database
                // Inserting values into tasks table
                long id = db.insert(MoviesContract.MoviesDataBase.FAVORITE_TABLE, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.MoviesDataBase.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
//        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
