package com.a5airi.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by khairy on 3/10/2018.
 */

public class JsonUtils implements Serializable {


    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private String voteAverage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;



    public JsonUtils(String title, String overview, String voteAverage, String posterPath, String backdropPath , String releaseDate , String id) {
        this.title = title;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.id = id;


    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return overview;
    }

    public String getvote_average() {
        return voteAverage;
    }

    public String getCover_image() {
        return "http://image.tmdb.org/t/p/w780" + posterPath;
    }

    public String getIntro_image() {
        return "http://image.tmdb.org/t/p/w780" +  backdropPath;
    }

    public String getrelease_date() {
        return releaseDate;
    }

    public String getId() {
        return id;
    }

}
