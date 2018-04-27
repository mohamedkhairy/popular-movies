package com.a5airi.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.a5airi.popularmovies.Adapter.viewAdapter;
import com.a5airi.popularmovies.httpHandler.ApiFetcher;
import com.a5airi.popularmovies.httpHandler.HttpHandler;
import com.a5airi.popularmovies.model.FirstJson;
import com.a5airi.popularmovies.model.JsonUtils;
//import com.a5airi.popularmovies.model.extractMoviesData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements viewAdapter.movie_onclickHandler ,
        SharedPreferences.OnSharedPreferenceChangeListener{


    private String TAG ="MainActivity";
    private static final int MAIN_LOADER = 11;

//    private static final int TRAILER_LOADER = 33;
    private static final String BUNDLE_KEY = "sort";
    private static final String SaveState_KEY = "callback";
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    viewAdapter adapter;
    List<JsonUtils> DataArrayList = new ArrayList<>();
    Listed_data listed_data ;
    String API_KEY = "af0c4d656b90649d188f51b053cd24b4";
    String url = "https://api.themoviedb.org/3/movie/";
    private String json_str;
    private String json_trailer;
//    extractMoviesData extractMoviesData;
    private Bundle bundle = new Bundle();
    ListPreference listPreference;
    JsonUtils jsonUtils;
    private static Retrofit retrofit;
    FirstJson firstJson;
    String sort;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSharedPreferences();
        getRetrofitmainPage(sort);
    }


    public void getRetrofitmainPage(String moviesSort) {

        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiFetcher apiFetcher = retrofit.create(ApiFetcher.class);
        Call<FirstJson> call = apiFetcher.getmMovies(moviesSort , API_KEY);
        call.enqueue(new Callback<FirstJson>() {
            @Override
            public void onResponse(Call<FirstJson> call, Response<FirstJson> response) {
                firstJson = response.body();
                listed_data = new Listed_data(firstJson.getResults());
                set_view();
            }

            @Override
            public void onFailure(Call<FirstJson> call, Throwable t) {
                Log.d("retro" , t.getMessage());
            }
        });
    }

    private void setupSharedPreferences(){
       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String val = sharedPreferences.getString("chosenSort" , "Top Rated");
        if (val.equals("Popular")){
            sort = "popular";
        }else if (val.equals("Top Rated")){
            sort = "top_rated";
        }else {
            Log.d(TAG ,"shared : 55");
            Toast.makeText(this , val , Toast.LENGTH_LONG).show();
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public  void set_view (){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(MainActivity.this , 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new viewAdapter(MainActivity.this , this , listed_data);
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void movie_handler(int position) {
        JsonUtils jsonUtils = listed_data.getData_json().get(position);
        Intent intent = new Intent(this , DetailsActivity.class);
        intent.putExtra(DetailsActivity.MOVIE_EXTRA ,  jsonUtils);
        startActivity(intent);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

       String value =  sharedPreferences.getString(key , getString(R.string.TopRated));

       switch(value){
            case "Top Rated" :
                getRetrofitmainPage("top_rated");
                break;
            case "Popular" :
                getRetrofitmainPage("popular");
                break;
            case  "Favorites" :
                Toast.makeText(MainActivity.this , "Favorites set" , Toast.LENGTH_LONG).show();
                break;
        }

    }



//
//    @Override
//    public Loader<String> onCreateLoader(final int id, final Bundle args) {
//        Log.d(TAG , "Loder 222");
//        return new AsyncTaskLoader<String>(this) {
//
//
//            @Override
//            protected void onStartLoading() {
//                Log.d(TAG , "Loder 3333");
////                List_data.clear();
//                forceLoad();
//                if (args != null){
//                    url = args.getString(BUNDLE_KEY);
//                    Log.d(TAG , "Loder 444" + url);
//
//                }
////                if (json_str != null){
////                    deliverResult(json_str);
////                }else {
//
////                }
//
//            }
//
//            @Override
//            public String loadInBackground() {
//
//                Log.d(TAG , "Loder 5555" + url);
//                HttpHandler httpHandler = new HttpHandler();
////                JsonUtils jsonUtils;
//
//                Log.d("zzz" ," " + url);
//                    json_str = httpHandler.makeServiceCall(url);
//                Log.d("zzz" ," " + json_str);
//                    JsonData(json_str);
//
//
//                Log.d("uuu" , json_str);
//
//                return json_str ;
//            }
//            @Override
//            public void deliverResult(String data) {
//                json_str = data;
//                super.deliverResult(data);
//            }
//        };
//    }
//
//    @Override
//    public void onLoadFinished(Loader<String> loader, String data) {
////        if (loader.getId() == MAIN_LOADER){
////            JsonData(json_str);
////        }else if (loader.getId() == ReviewTrailer_LOADER){
////            ReviewsData(json_str);
////            TrailerKey(TrailerUrl);
////            new Listed_data(ReviewList , TrailerList);
////        }
//        Log.d(TAG , "!!!!!!!!!!"  + jsonUtils.getId());
//        set_view();
//    }
//
//    @Override
//    public void onLoaderReset(Loader<String> loader) {
//
//    }
//
//    public void GetSort(int ID , Bundle bundle){
//
//
//        LoaderManager loaderManager = getSupportLoaderManager();
//        Loader<String> githubSearchLoader = loaderManager.getLoader(ID);
//        if (githubSearchLoader == null) {
//            loaderManager.initLoader(ID, bundle, this);
//
//        } else {
//            loaderManager.restartLoader(ID, bundle, this);
//        }
//        Log.d(TAG ,"dd : 11");
//    }
//
//    public void JsonData(String json_data) {
//
//        if (json_data != null){
//            try {
//                DataArrayList.clear();
//                JSONObject main_jsonobject = new JSONObject(json_data);
//                JSONArray json_movies = main_jsonobject.getJSONArray("results");
//                Log.d(TAG , "Loder 66");
//                for(int i = 0 ; i < json_movies.length() ; i++ ){
//                    JSONObject movie = json_movies.getJSONObject(i);
//                    String id = movie.getString("id");
//                    String title = movie.getString("title");
//                    String summary = movie.getString("overview");
//                    String vote_average =  movie.getString("vote_average");
//                    String background_image_original ="http://image.tmdb.org/t/p/w780"+ movie.getString("backdrop_path");
//                    String large_cover_image ="http://image.tmdb.org/t/p/w780"+ movie.getString("poster_path");
//                    String release_date = movie.getString("release_date");
//
//                    jsonUtils = new JsonUtils(title  , summary , vote_average , large_cover_image , background_image_original , release_date , id);
//                    DataArrayList.add(jsonUtils);
//                }
//                listed_data = new Listed_data(DataArrayList);
//            } catch (final JSONException e) {
//                Log.e(TAG, "Json parsing error: " + e.getMessage());
//
//            }
//        }
//        else {
//            Log.e(TAG, "json data is null");
//
//        }
//    }

//    private class GetMovies extends AsyncTask<Void , Void , Void>{
//
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            HttpHandler httpHandler = new HttpHandler();
//            String json_str = httpHandler.makeServiceCall(url);
//            JsonUtils jsonUtils;
//
//            if (json_str != null){
//                try {
//                    JSONObject main_jsonobject = new JSONObject(json_str);
//                    JSONArray json_movies = main_jsonobject.getJSONArray("results");
//
//                    for(int i = 0 ; i < json_movies.length() ; i++ ){
//                        JSONObject movie = json_movies.getJSONObject(i);
//                        String title = movie.getString("title");
//                        String summary = movie.getString("overview");
//                        String vote_average =  movie.getString("vote_average");
//                        String background_image_original ="http://image.tmdb.org/t/p/w780"+ movie.getString("backdrop_path");
//                        String large_cover_image ="http://image.tmdb.org/t/p/w780"+ movie.getString("poster_path");
//                        String release_date = movie.getString("release_date");
//
//                        jsonUtils = new JsonUtils(title  , summary , vote_average , large_cover_image , background_image_original , release_date);
//                        List_data.add(jsonUtils);
//                    }
//                } catch (final JSONException e) {
//                    Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                }
//            }else {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get json from server. Check LogCat for possible errors!",
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            set_view();
//
//        }
//
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
//            case R.id.top_rated:
//                url = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
//                load_task();
//                break;
//            case R.id.popular:
//                url = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
//                load_task();
//                break;
            case R.id.settings:
                Intent SettingIntent = new Intent(this , SettingsActivity.class);
                startActivity(SettingIntent);
                break;
        }
        return true;
    }
}
