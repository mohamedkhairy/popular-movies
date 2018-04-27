package com.a5airi.popularmovies.httpHandler;

import com.a5airi.popularmovies.model.FirstJson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiFetcher {

    @GET("{sort}")
    Call<FirstJson> getmMovies(@Path("sort") String sort ,  @Query("api_key") String api_key);


}
