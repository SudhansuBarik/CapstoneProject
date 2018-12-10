package io.github.sudhansubarik.moviescentral.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MoviesReviews implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MoviesReviews> CREATOR = new Parcelable.Creator<MoviesReviews>() {
        @Override
        public MoviesReviews createFromParcel(Parcel in) {
            return new MoviesReviews(in);
        }

        @Override
        public MoviesReviews[] newArray(int size) {
            return new MoviesReviews[size];
        }
    };
    @SerializedName("id")
    private String id;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("url")
    private String url;

    public MoviesReviews() {
    }

    public MoviesReviews(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    private MoviesReviews(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

}
