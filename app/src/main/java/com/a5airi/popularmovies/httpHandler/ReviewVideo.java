package com.a5airi.popularmovies.httpHandler;

import com.a5airi.popularmovies.model.ReviewJson;
import com.a5airi.popularmovies.model.TrailerJson;
import com.a5airi.popularmovies.model.singleDetails.MovieDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewVideo {
    @GET("{id}/reviews")
    Call<ReviewJson> getReviewData(@Path("id") String id, @Query("api_key") String api_key);

    @GET("{id}/videos")
    Call<TrailerJson> getTrailerData(@Path("id") String id, @Query("api_key") String api_key);

    @GET("{id}")
    Call<MovieDetails> getDetails(@Path("id") String id, @Query("api_key") String api_key);

}
