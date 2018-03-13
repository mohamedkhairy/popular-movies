package com.a5airi.popularmovies;

import com.a5airi.popularmovies.model.JsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by khairy on 3/12/2018.
 */

public class Listed_data implements Serializable {
    private List<JsonUtils> data_json = new ArrayList<>();

    public Listed_data(List<JsonUtils> data_json) {
        this.data_json = data_json;

    }

    public List<JsonUtils> getData_json() {
        return data_json;
    }
}
