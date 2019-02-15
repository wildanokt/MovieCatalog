package com.wildanokt.searchmoviemade.model;

import org.json.JSONObject;

public class MovieItems {
    private String title;
    private String overview;
    private String posterPath;
    private String releaseDate;

    //----------------------------------------------------------
    //constructor

    public MovieItems(JSONObject object) {
        try {
            String title = object.getString("title");
            String overview = object.getString("overview");
            String posterPath = object.getString("poster_path");
            String releaseDate = object.getString("release_date");

            this.title = title;
            this.overview = overview;
            this.posterPath = posterPath;
            this.releaseDate = releaseDate;

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //----------------------------------------------------------
    //Getter Setter vars

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getOverview() {
        return overview;
    }
    public String getPosterPath() {
        return posterPath;
    }
    public String getReleaseDate() {
        return releaseDate;
    }

    //----------------------------------------------------------

}
