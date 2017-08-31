package com.assignment.candidate.rbcwealthmanagement.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by edmundrojas on 2017-08-30.
 */

public class Reviews {

    @SerializedName("reviews")
    private ArrayList<Review> reviews;

    public ArrayList<Review> getReviews() {
        return reviews;
    }
}
