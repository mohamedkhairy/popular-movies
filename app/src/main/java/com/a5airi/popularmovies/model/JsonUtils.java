package com.a5airi.popularmovies.model;

import java.io.Serializable;

/**
 * Created by khairy on 3/10/2018.
 */

public class JsonUtils implements Serializable {

    private String title;
    private String summary;
    private String vote_average;
    private String cover_image;
    private String intro_image;
    private String release_date;




    public JsonUtils(String title, String summary, String vote_average, String cover_image, String intro_image , String release_date) {
        this.title = title;
        this.summary = summary;
        this.vote_average = vote_average;
        this.cover_image = cover_image;
        this.intro_image = intro_image;
        this.release_date = release_date;

    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getvote_average() {
        return vote_average;
    }

    public String getCover_image() {
        return cover_image;
    }

    public String getIntro_image() {
        return intro_image;
    }

    public String getrelease_date() {
        return release_date;
    }
}
