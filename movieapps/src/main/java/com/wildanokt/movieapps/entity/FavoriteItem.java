package com.wildanokt.movieapps.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.wildanokt.movieapps.db.DatabaseContract;

import static com.wildanokt.movieapps.db.DatabaseContract.getCollumnInt;
import static com.wildanokt.movieapps.db.DatabaseContract.getCollumnString;

public class FavoriteItem implements Parcelable {

    private int id;
    private String title, overview, imgPath;

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
    public String getImgPath() {
        return imgPath;
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

    public FavoriteItem(Cursor cursor) {
        this.id = getCollumnInt(cursor, DatabaseContract.FavoriteCollums._ID);
        this.title = getCollumnString(cursor, DatabaseContract.FavoriteCollums.TITLE);
        this.overview = getCollumnString(cursor, DatabaseContract.FavoriteCollums.OVERVIEW);
        this.imgPath = getCollumnString(cursor, DatabaseContract.FavoriteCollums.IMAGE_PATH);
    }

    protected FavoriteItem(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        imgPath = in.readString();
    }

    public static final Creator<FavoriteItem> CREATOR = new Creator<FavoriteItem>() {
        @Override
        public FavoriteItem createFromParcel(Parcel in) {
            return new FavoriteItem(in);
        }

        @Override
        public FavoriteItem[] newArray(int size) {
            return new FavoriteItem[size];
        }
    };
}
