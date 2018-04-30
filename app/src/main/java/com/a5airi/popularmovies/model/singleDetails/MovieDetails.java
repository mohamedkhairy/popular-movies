
package com.a5airi.popularmovies.model.singleDetails;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieDetails {

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("vote_average")
    @Expose
    private String voteAverage;


    public String getBackdropPath() {
        return "http://image.tmdb.org/t/p/w780" +backdropPath;
    }

    public String getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return "http://image.tmdb.org/t/p/w780" +posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getVoteAverage()
    {

        return voteAverage.substring(0 , 3);
    }


}
