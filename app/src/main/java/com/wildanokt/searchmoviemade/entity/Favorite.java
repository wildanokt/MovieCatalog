package com.wildanokt.searchmoviemade.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static android.provider.BaseColumns._ID;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.IMAGE_PATH;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.OVERVIEW;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.getCollumnInt;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.getCollumnString;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.TITLE;

public class Favorite implements Parcelable {

    private int id;
    private String title;
    private String overview;
    private String imgPath;

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
    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public String getImgPath() {
        return imgPath;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.imgPath);
    }

    public Favorite() {
    }

    //new
    public Favorite(Cursor cursor){
        this.id = getCollumnInt(cursor, _ID);
        this.title = getCollumnString(cursor, TITLE);
        this.overview = getCollumnString(cursor, OVERVIEW);
        this.imgPath = getCollumnString(cursor, IMAGE_PATH);
    }

    protected Favorite(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.imgPath = in.readString();
    }

    public static final Parcelable.Creator<Favorite> CREATOR = new Parcelable.Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };
}
