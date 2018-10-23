package io.github.sudhansubarik.moviescentral.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favorites")
public class DbMovies {

    @NonNull
    @PrimaryKey
    private int movieId;
    private String movieName;
    private String originalTitle;
    private String tagline;
    private String overview;
    private boolean adult;
    private String posterPath;
    private String backdropPath;
    private String releaseDate;
    private boolean video;
    private double voteAverage;
    private int voteCount;

    public DbMovies() {
    }

    public DbMovies(int id, String title, String originalTitle, String tagline, String overview,
                    boolean adult, String posterPath, String backdropPath, String releaseDate, boolean video,
                    double voteAverage, int voteCount) {
        this.movieId = id;
        this.movieName = title;
        this.originalTitle = originalTitle;
        this.tagline = tagline;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
