package io.github.sudhansubarik.moviescentral.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    @SerializedName("title")
    private String title;
    @SerializedName("id")
    private int id;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("tagline")
    private String tagline;
    @SerializedName("overview")
    private String overview;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("video")
    private boolean video;
    @SerializedName("vote_average")
    private Double voteAverage;
    @SerializedName("vote_count")
    private int voteCount;

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Movie(
            int id,
            String title,
            String originalTitle,
            String tagline,
            String overview,
            boolean adult,
            String posterPath,
            String backdropPath,
            String releaseDate,
            boolean video,
            double voteAverage,
            int voteCount) {
        this.id = id;
        this.title = title;
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

    @Override
    public int describeContents() {
        return 0;
    }

    protected Movie(Parcel in) {

        title = in.readString();
        id = in.readInt();
        originalTitle = in.readString();
        tagline = in.readString();
        overview = in.readString();
        adult = in.readByte() != 0x00;
        posterPath = in.readString();
        backdropPath = in.readString();
        releaseDate = in.readString();
        video = in.readByte() != 0x00;
        voteAverage = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(tagline);
        dest.writeString(overview);
        dest.writeByte((byte) (adult ? 0x01 : 0x00));
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(releaseDate);
        dest.writeByte((byte) (video ? 0x01 : 0x00));
        if (voteAverage == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(voteAverage);
        }
        dest.writeInt(voteCount);
    }
}