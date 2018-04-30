package com.a5airi.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a5airi.popularmovies.Adapter.ReviewAdapter;
import com.a5airi.popularmovies.Adapter.TrailerAdapter;

import com.a5airi.popularmovies.httpHandler.ReviewVideo;
import com.a5airi.popularmovies.model.JsonUtils;
//import com.a5airi.popularmovies.moviesDB.MoviesContract;
import com.a5airi.popularmovies.model.ReviewJson;
import com.a5airi.popularmovies.model.ReviewResult;
import com.a5airi.popularmovies.model.TrailerJson;
import com.a5airi.popularmovies.model.TrailerResult;
import com.a5airi.popularmovies.model.singleDetails.MovieDetails;
import com.a5airi.popularmovies.moviesDB.MoviesContract;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity
        implements TrailerAdapter.Trailer_onclickHandler , LoaderManager.LoaderCallbacks<Cursor> {


    public static final String MOVIE_EXTRA = "movieDetails";
    public static final String ID_EXTRA = "movieId";
    public static final String Position_EXTRA = "position";
    private static final int ReviewTrailer_LOADER = 22;
    private String SelectedMovieID;
    static String url = "https://api.themoviedb.org/3/movie/" ;
    ImageView cover_imageview , intro_imageview;
    TextView title , release_date , summary , user_rating , trailerTextView , ReviewText;
    JsonUtils jsondata;
    CheckBox FavoriteCheckBox ;
    RecyclerView TrailerRecyclerView , ReviewRecycler;
    ReviewAdapter reviewAdapter;
    TrailerAdapter  trailerAdapter;
    String API_KEY ;
    ReviewJson reviewJson;
    TrailerJson trailerJson;
    List<ReviewResult> reviewResultList;
    List<TrailerResult> trailerJsonList;
    ReviewVideo reviewVideo;
    String ExraID;
    MovieDetails movieDetails;
//    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        cover_imageview = (ImageView) findViewById(R.id.cover_imageView);
        intro_imageview = (ImageView) findViewById(R.id.intro_imageView);
        title = (TextView) findViewById(R.id.title_view);
        release_date = (TextView) findViewById(R.id.release_date_view);
        summary = (TextView) findViewById(R.id.sammary_view);
        user_rating = (TextView) findViewById(R.id.user_rating_view);
        FavoriteCheckBox = (CheckBox) findViewById(R.id.favorite_CheckBox);
        trailerTextView = (TextView) findViewById(R.id.trailer);
        ReviewText = (TextView) findViewById(R.id.reviews);


        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
         reviewVideo = retrofit.create(ReviewVideo.class);

        Intent i = getIntent();
        jsondata  = (JsonUtils) i.getSerializableExtra(MOVIE_EXTRA);
        ExraID = i.getStringExtra(ID_EXTRA);
//        position = i.getIntExtra(Position_EXTRA , -1);
        if (jsondata != null && isNetworkConnected()) {
            getRetrofitReview(jsondata.getId());
            getRetrofitTrailer(jsondata.getId());
            setDetailesView();
        }else if (ExraID != null && isNetworkConnected()){
            getRetrofitReview(ExraID);
            getRetrofitTrailer(ExraID);
            getMovieDetails(ExraID);

        }
        Loader<Cursor> loader = getSupportLoaderManager().getLoader(ReviewTrailer_LOADER);
        if (loader == null){
            getSupportLoaderManager().initLoader(ReviewTrailer_LOADER, null, this);
        }else {
            getSupportLoaderManager().restartLoader(ReviewTrailer_LOADER , null , this);
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    private void setDetailesView(){

        Picasso.with(this)
                .load(jsondata.getCover_image())
                .into(cover_imageview);

        Picasso.with(this)
                .load(jsondata.getIntro_image())
                .into(intro_imageview);

        title.setText(jsondata.getTitle());
        release_date.setText(getString(R.string.ReleaseDate) + "\n" + jsondata.getrelease_date());
        user_rating.setText(getString(R.string.UserRating) + "\n" + jsondata.getvote_average());
        summary.setText(jsondata.getSummary());


    }
    private void setView(MovieDetails details){

        Picasso.with(this)
                .load(details.getPosterPath())
                .into(cover_imageview);

        Picasso.with(this)
                .load(details.getBackdropPath())
                .into(intro_imageview);

        title.setText(details.getTitle());
        release_date.setText(getString(R.string.ReleaseDate) + "\n"  + details.getReleaseDate());
        user_rating.setText(getString(R.string.UserRating) + "\n"  + details.getVoteAverage());
        summary.setText(details.getOverview());

    }


    public void getMovieDetails(String movieid) {

        Call<MovieDetails> call = reviewVideo.getDetails(movieid , API_KEY);

        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                movieDetails = response.body();
                setView(movieDetails);
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {

            }
        });


    }
    public void getRetrofitReview(String movieid) {

        Call<ReviewJson> call = reviewVideo.getReviewData(movieid , API_KEY);

        call.enqueue(new Callback<ReviewJson>() {
            @Override
            public void onResponse(Call<ReviewJson> call, Response<ReviewJson> response) {
                reviewJson = response.body();
                reviewResultList = reviewJson.getReviewResults();
                set_ReviewView ();
            }

            @Override
            public void onFailure(Call<ReviewJson> call, Throwable t) {

            }
        });


    }

    public void getRetrofitTrailer(String movieid) {


        Call<TrailerJson> call = reviewVideo.getTrailerData(movieid , API_KEY);

        call.enqueue(new Callback<TrailerJson>() {
            @Override
            public void onResponse(Call<TrailerJson> call, Response<TrailerJson> response) {
                trailerJson = response.body();
                trailerJsonList = trailerJson.getTrailerResults();
                set_TrailerView();
            }

            @Override
            public void onFailure(Call<TrailerJson> call, Throwable t) {

            }
        });


    }
    public  void set_ReviewView (){
        ReviewRecycler = (RecyclerView) findViewById(R.id.Reviewrecycler_view);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getBaseContext() , LinearLayout.VERTICAL , false );
        ReviewRecycler.setHasFixedSize(true);
        ReviewRecycler.setLayoutManager(linearLayout);
        reviewAdapter = new ReviewAdapter(DetailsActivity.this , reviewResultList);
        ReviewRecycler.setAdapter(reviewAdapter);

    }

    public  void set_TrailerView (){
        TrailerRecyclerView = (RecyclerView) findViewById(R.id.Trailerrecycler_view);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getBaseContext() , LinearLayout.HORIZONTAL , false );
        TrailerRecyclerView.setHasFixedSize(true);
        TrailerRecyclerView.setLayoutManager(linearLayout);
        trailerAdapter = new TrailerAdapter(DetailsActivity.this , trailerJsonList , this);
        TrailerRecyclerView.setAdapter(trailerAdapter);
    }

    @Override
    public void trailer_handler(String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }


    public void onCheckclick(View view) {
                if (FavoriteCheckBox.isChecked()){
                    Uri selectedContent = MoviesContract.MoviesDataBase.CONTENT_URI;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MoviesContract.MoviesDataBase.COLUMN_MOVIE_ID , jsondata.getId());
                    contentValues.put(MoviesContract.MoviesDataBase.COLUMN_PHOTO_PATH, jsondata.getCover_image());
                    contentValues.put(MoviesContract.MoviesDataBase.COLUMN_BACKGROUND_PHOTO_PATH, jsondata.getIntro_image());
                    contentValues.put(MoviesContract.MoviesDataBase.COLUMN_TITLE, jsondata.getTitle());
                    contentValues.put(MoviesContract.MoviesDataBase.COLUMN_USER_RATE , jsondata.getvote_average());
                    contentValues.put(MoviesContract.MoviesDataBase.COLUMN_RELEASE_DATE , jsondata.getrelease_date());
                    contentValues.put(MoviesContract.MoviesDataBase.COLUMN_SUMMARY , jsondata.getSummary());
                    Uri uri = getContentResolver().insert(selectedContent, contentValues);

                    if(uri != null) {
                        Toast.makeText(getBaseContext(), "added to your favorites", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Uri uri = MoviesContract.MoviesDataBase.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(SelectedMovieID).build();
                    getContentResolver().delete(uri, null, null);
//                    Intent intent = new Intent();
//                    intent.putExtra(MainActivity.Position_result , position);
//                    setResult(200 , intent);
                    Toast.makeText(getBaseContext(), "removed from favorites", Toast.LENGTH_LONG).show();
                    finish();
                }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MoviesContract.MoviesDataBase.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e("detailes", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       if (data != null) {
           while (data.moveToNext()) {
               int movieID_Index = data.getColumnIndex(MoviesContract.MoviesDataBase.COLUMN_MOVIE_ID);
               int TableID_Index = data.getColumnIndex(MoviesContract.MoviesDataBase._ID);

               String id = data.getString(movieID_Index);
               String TableID = data.getString(TableID_Index);
               if (jsondata != null){
                   if (id.equals(jsondata.getId())){
                       FavoriteCheckBox.setChecked(true);
                       SelectedMovieID = TableID;
                    }

               }else if (ExraID !=null){
                   if (id.equals(ExraID)){
                       FavoriteCheckBox.setChecked(true);
                       SelectedMovieID = TableID;
                       if (!isNetworkConnected()){
                           setOfflineMode(data);
                       }
                    }
               }


           }
       }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void setOfflineMode(Cursor cursor){
        trailerTextView.setVisibility(View.INVISIBLE);
        ReviewText.setVisibility(View.INVISIBLE);

        int moviePhoto_index = cursor.getColumnIndex(MoviesContract.MoviesDataBase.COLUMN_PHOTO_PATH);
        int movieBGphoto_Index = cursor.getColumnIndex(MoviesContract.MoviesDataBase.COLUMN_BACKGROUND_PHOTO_PATH);
        int movieTitile_Index = cursor.getColumnIndex(MoviesContract.MoviesDataBase.COLUMN_TITLE);
        int movieRate_Index = cursor.getColumnIndex(MoviesContract.MoviesDataBase.COLUMN_USER_RATE);
        int movieDate_Index = cursor.getColumnIndex(MoviesContract.MoviesDataBase.COLUMN_RELEASE_DATE);
        int moviesummary_Index = cursor.getColumnIndex(MoviesContract.MoviesDataBase.COLUMN_SUMMARY);

        String moviePhoto = cursor.getString(moviePhoto_index);
        String movieBGphoto = cursor.getString(movieBGphoto_Index);
        String movieTitile = cursor.getString(movieTitile_Index);
        String movieRate = cursor.getString(movieRate_Index);
        String movieDate = cursor.getString(movieDate_Index);
        String moviesummary = cursor.getString(moviesummary_Index);

        Picasso.with(this)
                .load(moviePhoto)
                .into(cover_imageview);

        Picasso.with(this)
                .load(movieBGphoto)
                .into(intro_imageview);

        title.setText(movieTitile);
        release_date.setText(getString(R.string.ReleaseDate) + "\n"  + movieDate);
        user_rating.setText(getString(R.string.UserRating) + "\n"  + movieRate);
        summary.setText(moviesummary);
    }
}
