package com.assignment.candidate.rbcwealthmanagement.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by edmundrojas on 2017-08-29.
 */

public class Business {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("location")
    private Location location;
    private int randomColor;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }

    public Location getLocation() {
        return location;
    }

    public void setRandomColor(int randomColor) {
        this.randomColor = randomColor;
    }

    public int getRandomColor() {
        return randomColor;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
