package com.assignment.candidate.rbcwealthmanagement.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by edmundrojas on 2017-08-30.
 */

public class Review {

    @SerializedName("rating")
    private String rating;
    @SerializedName("text")
    private String text;

    public String getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }
}
