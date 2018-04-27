package com.a5airi.popularmovies.moviesDB;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by khairy on 4/4/2018.
 */

public class MoviesContract {

    public static final String AUTHORITY = "com.a5airi.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";
//    public static final String PATH_REVIEWS = "reviews";
//    public static final String PATH_TRAILER = "trailer";

    public static final class MoviesDataBase implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        // first table - favorite movies
        public static final String FAVORITE_TABLE= "favorites";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_PHOTO_PATH = "PhotoPath";
        public static final String COLUMN_BACKGROUND_PHOTO_PATH = "backPhoto";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USER_RATE = "UserRate";
        public static final String COLUMN_RELEASE_DATE = "ReleaseDate";
        public static final String COLUMN_SUMMARY = "Summary";

//        //second table - reviews
//        public static final String REVIEWS_TABLE = "reviews";
//        public static final String COLUMN_REVIEW_MOVIES_ID = "MovieID";
//        public static final String COLUMN_AUTHER = "Auther";
//        public static final String COLUMN_CONTENT = "Content";
//
//        //3rd table - trailer
//        public static final String TRAILER_TABLE = "trailer";
//        public static final String COLUMN_TRAILER_MOVIES_ID = "description";
//        public static final String COLUMN_VEDIO = "vedio";

    }



}
