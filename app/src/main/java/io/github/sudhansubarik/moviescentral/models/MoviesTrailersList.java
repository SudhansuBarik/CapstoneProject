package io.github.sudhansubarik.moviescentral.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesTrailersList {

    @SerializedName("id")
    private int page;
    @SerializedName("results")
    private List<MoviesTrailers> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MoviesTrailers> getResults() {
        return results;
    }

    public void setResults(List<MoviesTrailers> results) {
        this.results = results;
    }
}
