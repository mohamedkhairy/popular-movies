package com.a5airi.popularmovies.moviesDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moviesDB.db";
    private static final int VERSION = 1;


    public MoviesDbHelper(Context context) {
        super(context,DATABASE_NAME, null, VERSION);
        Log.d("base" , "DB 000");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("base" , "DB 1111");
        final String CREATE_FAVORITE_TABLE = "CREATE TABLE "  + MoviesContract.MoviesDataBase.FAVORITE_TABLE + " (" +
                MoviesContract.MoviesDataBase._ID  + " INTEGER PRIMARY KEY, " +
                MoviesContract.MoviesDataBase.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MoviesContract.MoviesDataBase.COLUMN_PHOTO_PATH + " TEXT NOT NULL, " +
                MoviesContract.MoviesDataBase.COLUMN_BACKGROUND_PHOTO_PATH + " TEXT NOT NULL, " +
                MoviesContract.MoviesDataBase.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesDataBase.COLUMN_USER_RATE + " TEXT NOT NULL, " +
                MoviesContract.MoviesDataBase.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.MoviesDataBase.COLUMN_SUMMARY + " TEXT NOT NULL);";

//        final String CREATE_REVIEW_TABLE = "CREATE TABLE "  + MoviesContract.MoviesDataBase.REVIEWS_TABLE + " (" +
//                MoviesContract.MoviesDataBase._ID  + " INTEGER PRIMARY KEY, " +
//                MoviesContract.MoviesDataBase.COLUMN_REVIEW_MOVIES_ID + " TEXT NOT NULL, " +
//                MoviesContract.MoviesDataBase.COLUMN_AUTHER + " TEXT NOT NULL, " +
//                MoviesContract.MoviesDataBase.COLUMN_CONTENT + " TEXT NOT NULL);";
//
//        final String CREATE_TRAILER_TABLE = "CREATE TABLE "  + MoviesContract.MoviesDataBase.TRAILER_TABLE + " (" +
//                MoviesContract.MoviesDataBase._ID  + " INTEGER PRIMARY KEY, " +
//                MoviesContract.MoviesDataBase.COLUMN_TRAILER_MOVIES_ID + " TEXT NOT NULL, " +
//                MoviesContract.MoviesDataBase.COLUMN_VEDIO + " TEXT NOT NULL);";
        Log.d("base" , "DB 222");
        db.execSQL(CREATE_FAVORITE_TABLE);
//        db.execSQL(CREATE_REVIEW_TABLE);
//        db.execSQL(CREATE_TRAILER_TABLE);
        Log.d("base" , "DB 33");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("base" , "DB upgrade");
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesDataBase.FAVORITE_TABLE);
        onCreate(db);
    }
}
