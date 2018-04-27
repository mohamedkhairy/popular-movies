package com.a5airi.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.a5airi.popularmovies.Adapter.ReviewAdapter;
import com.a5airi.popularmovies.Adapter.TrailerAdapter;
import com.a5airi.popularmovies.Adapter.viewAdapter;
import com.a5airi.popularmovies.httpHandler.HttpHandler;
import com.a5airi.popularmovies.httpHandler.ReviewVideo;
import com.a5airi.popularmovies.model.JsonUtils;
//import com.a5airi.popularmovies.moviesDB.MoviesContract;
import com.a5airi.popularmovies.model.ReviewJson;
import com.a5airi.popularmovies.model.ReviewResult;
import com.a5airi.popularmovies.model.TrailerJson;
import com.a5airi.popularmovies.model.TrailerResult;
import com.a5airi.popularmovies.moviesDB.MoviesContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity
        implements TrailerAdapter.Trailer_onclickHandler {


    public static final String MOVIE_EXTRA = "movieDetails";
    ImageView cover_imageview , intro_imageview;
    TextView title , release_date , summary , user_rating ;
    JsonUtils jsondata;
    CheckBox FavoriteCheckBox ;
//    MoviesContract.MoviesDataBase moviesDataBase;
    String json_review , json_trailer;
    String ReviewUrl , TrailerUrl;
    private static final String BUNDLE_review = "review";
    private static final String BUNDLE_trailer = "trailer";
    HttpHandler httpHandler;
    List<JsonUtils> ReviewList = new ArrayList<>();
    List<JsonUtils> TrailerList = new ArrayList<>();
    JsonUtils jsonUtils;
    Bundle bundle = new Bundle();
    private static final int ReviewTrailer_LOADER = 22;

    RecyclerView TrailerRecyclerView , ReviewRecycler;
    ReviewAdapter reviewAdapter;
    TrailerAdapter  trailerAdapter;
    String API_KEY = "af0c4d656b90649d188f51b053cd24b4";
    static String url = "https://api.themoviedb.org/3/movie/" ;
    ReviewJson reviewJson;
    TrailerJson trailerJson;
    List<ReviewResult> reviewResultList;
    List<TrailerResult> trailerJsonList;
    ReviewResult reviewResult;
    TrailerResult trailerResult;
    ReviewVideo reviewVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT < 16) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }else {
//            View decorView = getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);
//
////            ActionBar actionBar = getSupportActionBar();
////            actionBar.hide();
//        }
        setContentView(R.layout.activity_details);

        cover_imageview = (ImageView) findViewById(R.id.cover_imageView);
        intro_imageview = (ImageView) findViewById(R.id.intro_imageView);
        title = (TextView) findViewById(R.id.title_view);
        release_date = (TextView) findViewById(R.id.release_date_view);
        summary = (TextView) findViewById(R.id.sammary_view);
        user_rating = (TextView) findViewById(R.id.user_rating_view);
        FavoriteCheckBox = (CheckBox) findViewById(R.id.favorite_CheckBox);


        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
         reviewVideo = retrofit.create(ReviewVideo.class);




        Intent i = getIntent();
        jsondata  = (JsonUtils) i.getSerializableExtra(MOVIE_EXTRA);

        getRetrofitReview(jsondata.getId());
        getRetrofitTrailer(jsondata.getId());

        setDetailesView();


    }



    private void setDetailesView(){




        Picasso.with(this)
                .load(jsondata.getCover_image())
                .into(cover_imageview);

        Picasso.with(this)
                .load(jsondata.getIntro_image())
                .into(intro_imageview);

        title.setText(jsondata.getTitle());
        release_date.setText("Release Date\n" + jsondata.getrelease_date());
        user_rating.setText("User Rating\n" + jsondata.getvote_average());
        summary.setText(jsondata.getSummary());


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
                Log.d("review" , "error");
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
                Log.d("review" , "error 222");
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
        Log.d("base" , "on check 11");
        FavoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Log.d("base" , "on check 22");
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
                    Log.d("base" , "on check 33");
                    // Display the URI that's returned with a Toast
                    // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
                    if(uri != null) {
                        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                    }

                    // Finish activity (this returns back to MainActivity)
                    finish();
                }else {

                }

            }
        });


    }
}
