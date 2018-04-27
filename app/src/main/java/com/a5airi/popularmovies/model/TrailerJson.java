
package com.a5airi.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerJson {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TrailerResult> results = null;


    public List<TrailerResult> getTrailerResults() {
        return results;
    }


}
