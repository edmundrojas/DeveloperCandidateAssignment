package com.assignment.candidate.rbcwealthmanagement.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by edmundrojas on 2017-08-29.
 */

public class RestaurantsResult<T> {

    @SerializedName("total")
    private Long total;

    @SerializedName("businesses")
    private ArrayList<Business> businesses;

    public Long getTotal() {
        return total;
    }

    public ArrayList<Business> getBusinesses() {
        return businesses;
    }

}
