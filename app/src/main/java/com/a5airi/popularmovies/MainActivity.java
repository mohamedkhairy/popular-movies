package com.a5airi.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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

import com.a5airi.popularmovies.model.FirstJson;
import com.a5airi.popularmovies.model.JsonUtils;
import com.a5airi.popularmovies.moviesDB.MoviesContract;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements viewAdapter.movie_onclickHandler ,
        SharedPreferences.OnSharedPreferenceChangeListener {


    private static Retrofit retrofit;
    private String TAG ="MainActivity";
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    viewAdapter adapter;
    Listed_data listed_data ;
    String API_KEY ;
    String url = "https://api.themoviedb.org/3/movie/";
    FirstJson firstJson;
    String sort;
    FetchMoviesAsync moviesAsync = new FetchMoviesAsync();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Toast.makeText(this , " No Internet Connection !!" , Toast.LENGTH_LONG).show();
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
            if (moviesAsync.getStatus().toString().equals("FINISHED")){
                new FetchMoviesAsync().execute();
                set_view(true);
            }else if(moviesAsync.getStatus().toString().equals("PENDING")){
                new FetchMoviesAsync().execute();
                set_view(true);
            }else if(moviesAsync.getStatus().toString().equals("RUNNING")){
                set_view(true);
            }else {
                moviesAsync.execute();
                set_view(true);
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
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


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100){
//            if (resultCode == 200){
//                int position = data.getIntExtra(Position_result , -1);
//                recyclerView.removeViewAt(position);
//                adapter.notifyItemRemoved(position);
//                adapter.notifyItemChanged(position);
//                recyclerView.setAdapter(adapter);
//            }
//        }
//    }

    @Override
    public void movie_handler(int position , String MovieId) {
        String favoriteSort =  PreferenceManager.getDefaultSharedPreferences(this).
                            getString("chosenSort" , "Top Rated");
        Intent intent = new Intent(this, DetailsActivity.class);


        if (favoriteSort.equals("Favorites")){

            intent.putExtra(DetailsActivity.ID_EXTRA, MovieId);
            startActivity(intent);
//            intent.putExtra(DetailsActivity.Position_EXTRA, position);
//            startActivityForResult(intent , 100);


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
                if (isNetworkConnected()) {
                    getRetrofitmainPage("top_rated");
                }else {
                    noConnection();
                }
                break;
            case "Popular" :
                if (isNetworkConnected()) {
                    getRetrofitmainPage("popular");
                }else {
                    noConnection();
                }
                break;
            case  "Favorites" :
                if (moviesAsync.getStatus().toString().equals("FINISHED")){
                    new FetchMoviesAsync().execute();
                    set_view(true);
                }else if(moviesAsync.getStatus().toString().equals("PENDING")){
                    new FetchMoviesAsync().execute();
                    set_view(true);
                }else if(moviesAsync.getStatus().toString().equals("RUNNING")){
                    set_view(true);
                }else {
                    moviesAsync.execute();
                    set_view(true);
                }
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



        @Override
        protected Cursor doInBackground(String... params) {
            Cursor cursor ;
            try {
                 cursor =  getContentResolver().query(MoviesContract.MoviesDataBase.CONTENT_URI,
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
    }
}
