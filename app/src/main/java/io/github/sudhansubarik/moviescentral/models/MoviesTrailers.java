package io.github.sudhansubarik.moviescentral.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MoviesTrailers implements Parcelable {

    @SuppressWarnings("unused")
    public static final Creator<MoviesTrailers> CREATOR = new Creator<MoviesTrailers>() {
        @Override
        public MoviesTrailers createFromParcel(Parcel in) {
            return new MoviesTrailers(in);
        }

        @Override
        public MoviesTrailers[] newArray(int size) {
            return new MoviesTrailers[size];
        }
    };
    @SerializedName("id")
    private String id;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("site")
    private String site;
    @SerializedName("type")
    private String type;

    public MoviesTrailers() {
    }

    public MoviesTrailers(String id, String key, String name, String site, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
    }

    protected MoviesTrailers(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        type = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(type);
    }

}
