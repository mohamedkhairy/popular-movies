package com.a5airi.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
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
import android.view.View;
import android.widget.Toast;

import com.a5airi.popularmovies.Adapter.viewAdapter;
import com.a5airi.popularmovies.httpHandler.ApiFetcher;
import com.a5airi.popularmovies.httpHandler.HttpHandler;
import com.a5airi.popularmovies.model.FirstJson;
import com.a5airi.popularmovies.model.JsonUtils;
import com.a5airi.popularmovies.moviesDB.MoviesContract;
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
        SharedPreferences.OnSharedPreferenceChangeListener {


    private String TAG ="MainActivity";
    private static final int MAIN_LOADER = 11;

    private static final int ID_LOADER = 33;
    private static final String BUNDLE_KEY = "id";
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
    String MovieID;
    FetchMoviesAsync moviesAsync = new FetchMoviesAsync();
RecyclerView.ViewHolder holder = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportLoaderManager().initLoader(MAIN_LOADER, null, this);


        if (isNetworkConnected()){
            setupSharedPreferences();
            getRetrofitmainPage(sort);
        }else{
            noConnection();
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void noConnection(){
        Toast.makeText(this , "Your Favorites \n No Internet Connection !!" , Toast.LENGTH_LONG).show();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("chosenSort" , "Favorites").apply();
        setupSharedPreferences();
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
                set_view(false);
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
            moviesAsync.execute();
            set_view(true);
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Loader<Cursor> loader = getSupportLoaderManager().getLoader(MAIN_LOADER);
//        if (loader == null){
//            getSupportLoaderManager().initLoader(MAIN_LOADER, null, this);
//        }else {
//            getSupportLoaderManager().restartLoader(MAIN_LOADER , null , this);
//        }
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        moviesAsync.execute();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public  void set_view (Boolean favorite){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(MainActivity.this , 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new viewAdapter(MainActivity.this , this , listed_data , favorite);
        recyclerView.setAdapter(adapter);




    }



    @Override
    public void movie_handler(int position) {
        String favoriteSort =  PreferenceManager.getDefaultSharedPreferences(this).
                            getString("chosenSort" , "Top Rated");
        Intent intent = new Intent(this, DetailsActivity.class);


        if (favoriteSort.equals("Favorites")){

//                Bundle bundle = new Bundle();
//                bundle.putInt(BUNDLE_KEY, position);
//                Loader<Cursor> loader = getSupportLoaderManager().getLoader(MAIN_LOADER);
//                if (loader == null) {
//                    getSupportLoaderManager().initLoader(ID_LOADER, bundle, this);
//                } else {
//                    getSupportLoaderManager().restartLoader(ID_LOADER, bundle, this);
//                }
//                moviesAsync.execute();
                Cursor cursor =moviesAsync.getCursor();
                if (cursor == null){Log.d("xxx" , "000000000000000");}
                cursor.move(position);
                int Index = cursor.getColumnIndex(MoviesContract.MoviesDataBase.COLUMN_MOVIE_ID);
                MovieID = cursor.getString(Index);
                intent.putExtra(DetailsActivity.ID_EXTRA, MovieID);
                startActivity(intent);


        }else {
            JsonUtils jsonUtils = listed_data.getData_json().get(position);
            intent.putExtra(DetailsActivity.MOVIE_EXTRA, jsonUtils);
            startActivity(intent);
        }

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
//                Loader<Cursor> loader = getSupportLoaderManager().getLoader(MAIN_LOADER);
//                if (loader == null){
//                    getSupportLoaderManager().initLoader(MAIN_LOADER, null, this);
//                }else {
//                    getSupportLoaderManager().restartLoader(MAIN_LOADER , null , this);
//                }
                moviesAsync.execute();
                set_view(true);
                break;
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                Intent SettingIntent = new Intent(this , SettingsActivity.class);
                startActivity(SettingIntent);
        return true;
    }


    public class FetchMoviesAsync extends AsyncTask<String,Void,Cursor> {
//        private Context con;
        private String id;



        private Cursor cursor;



        @Override
        protected Cursor doInBackground(String... params) {

            try {
                Cursor cursor =  getContentResolver().query(MoviesContract.MoviesDataBase.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

                return cursor;

            } catch (Exception e) {
                Log.e(TAG, "Failed to asynchronously load data.");
                e.printStackTrace();
                return null;
            }
        }
        @Override
        public void onPostExecute(Cursor countries){
            adapter.swapCursor(countries);
        }

        public String getId() {
            return id;
        }
        public Cursor getCursor() {
            return cursor;
        }

    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
//        return new AsyncTaskLoader<Cursor>(this) {
//            @Override
//            protected void onStartLoading() {
//
//               forceLoad();
//            }
//
//            @Override
//            public Cursor loadInBackground() {
//
//
//                try {
//                    Cursor cursor =  getContentResolver().query(MoviesContract.MoviesDataBase.CONTENT_URI,
//                            null,
//                            null,
//                            null,
//                            null);
//                    if (args != null){
//                        int position = args.getInt(BUNDLE_KEY);
//                        getdatafrom(cursor , position);
//                    }
//                    return cursor;
//
//                } catch (Exception e) {
//                    Log.e(TAG, "Failed to asynchronously load data.");
//                    e.printStackTrace();
//                    return null;
//                }
//
//            }
//        };
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//
//        adapter.swapCursor(data);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//
//    }
//
    public void getdatafrom(Cursor cursor , int position){
        cursor.move(position);
        int Index = cursor.getColumnIndex(MoviesContract.MoviesDataBase.COLUMN_MOVIE_ID);
        MovieID = cursor.getString(Index);
    }
}
