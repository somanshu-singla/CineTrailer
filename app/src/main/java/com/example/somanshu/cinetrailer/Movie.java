package com.example.somanshu.cinetrailer;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by somanshu on 9/7/17.
 */

public class Movie implements Parcelable {

    String id;
    String original_title;
    String overview;
    String release_date;
    String poster_path;
    String vote_average;
    String trailer_path;
    String trailer_name;



    public Movie() {
    }

    private Movie(Parcel parcel) {
        this.id = parcel.readString();
        this.original_title = parcel.readString();
        this.poster_path = parcel.readString();
        this.overview = parcel.readString();
        this.vote_average = parcel.readString();
        this.release_date = parcel.readString();
        this.trailer_path = parcel.readString();
        this.trailer_name = parcel.readString();
    }
    public Movie(JSONObject jsonObject)
    {
        try
        {
            Log.e(" id ",jsonObject.getString("id"));
            id=jsonObject.getString("id");
            Log.e(" title ",jsonObject.getString("original_title"));
            original_title=jsonObject.getString("original_title");
            Log.e(" overview ",jsonObject.getString("overview"));
            overview=jsonObject.getString("overview");
            Log.e(" path ",jsonObject.getString("poster_path"));
            poster_path="http://image.tmdb.org/t/p/w500"+jsonObject.getString("poster_path");
            release_date=jsonObject.getString("release_date");
            Log.e("release",poster_path);
            vote_average=jsonObject.getString("vote_average");
            trailer_path="http://api.themoviedb.org/3/movie/"+id+"/videos"+"?api_key=e4d10cb7c3b4358ff07ad16d7f546c25";
            Log.e("Trailer ",trailer_path);
        }
        catch (Exception e)
        {
            Log.e("Error ","in object creation - file : Movie.java");
        }
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getTrailer_path() {
        return trailer_path;
    }

    public void setTrailer_path(String trailer_path) {
        this.trailer_path = trailer_path;
    }

    public String getTrailer_name() {
        return trailer_name;
    }

    public void setTrailer_name(String trailer_name) {
        this.trailer_name = trailer_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.original_title);
        dest.writeString(this.poster_path);
        dest.writeString(this.overview);
        dest.writeString(this.vote_average);
        dest.writeString(this.release_date);
        dest.writeString(this.trailer_path);
        dest.writeString(this.trailer_name);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };
}
