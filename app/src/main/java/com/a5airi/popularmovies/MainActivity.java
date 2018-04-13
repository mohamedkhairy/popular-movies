package com.a5airi.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.a5airi.popularmovies.Adapter.viewAdapter;
import com.a5airi.popularmovies.httpHandler.HttpHandler;
import com.a5airi.popularmovies.model.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements viewAdapter.movie_onclickHandler , SharedPreferences.OnSharedPreferenceChangeListener {


    private String TAG ="MainActivity";

    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    viewAdapter adapter;
    List<JsonUtils> List_data = new ArrayList<>();
    Listed_data listed_data ;
    String API_KEY = "af0c4d656b90649d188f51b053cd24b4";
    String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSharedPreferences();
        load_task();
        Log.d(TAG , "step 1111111111111");
    }

    private void setupSharedPreferences(){
       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        Log.d(TAG , "step 222222222222");
    }


    public void load_task(){
        List_data.clear();
        new GetMovies().execute();
        listed_data = new Listed_data(List_data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        Log.d(TAG , "step 6666666666666");
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
         JsonUtils jsonUtils =  listed_data.getData_json().get(position);
        Intent intent = new Intent(this , DetailsActivity.class);
        intent.putExtra(DetailsActivity.MOVIE_EXTRA ,  jsonUtils);
        startActivity(intent);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG , "step 333333333333");
       String value =  sharedPreferences.getString(key , getString(R.string.TopRated));
        Log.d(TAG , "step 4444444444444");
       switch(value){
            case "Top Rated" :
                Toast.makeText(MainActivity.this , "Top Rated set" , Toast.LENGTH_LONG).show();
                break;
            case "Popular" :
                Toast.makeText(MainActivity.this , "Popular set" , Toast.LENGTH_LONG).show();
                break;
            case  "Favorites" :
                Toast.makeText(MainActivity.this , "Favorites set" , Toast.LENGTH_LONG).show();
                break;
        }
        Log.d(TAG , "step 5555555555");
    }

    private class GetMovies extends AsyncTask<Void , Void , Void>{



        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();
            String json_str = httpHandler.makeServiceCall(url);
            JsonUtils jsonUtils;

            if (json_str != null){
                try {
                    JSONObject main_jsonobject = new JSONObject(json_str);
                    JSONArray json_movies = main_jsonobject.getJSONArray("results");

                    for(int i = 0 ; i < json_movies.length() ; i++ ){
                        JSONObject movie = json_movies.getJSONObject(i);
                        String title = movie.getString("title");
                        String summary = movie.getString("overview");
                        String vote_average =  movie.getString("vote_average");
                        String background_image_original ="http://image.tmdb.org/t/p/w780"+ movie.getString("backdrop_path");
                        String large_cover_image ="http://image.tmdb.org/t/p/w780"+ movie.getString("poster_path");
                        String release_date = movie.getString("release_date");

                        jsonUtils = new JsonUtils(title  , summary , vote_average , large_cover_image , background_image_original , release_date);
                        List_data.add(jsonUtils);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            set_view();

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

        switch (item.getItemId()){
            case R.id.top_rated:
                url = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
                load_task();
                break;
            case R.id.popular:
                url = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
                load_task();
                break;
            case R.id.settings:
                Intent SettingIntent = new Intent(this , SettingsActivity.class);
                startActivity(SettingIntent);
                break;
        }
        return true;
    }
}
