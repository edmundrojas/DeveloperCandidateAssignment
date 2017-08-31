package com.assignment.candidate.rbcwealthmanagement.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by edmundrojas on 2017-08-30.
 */

public class Location {

    @SerializedName("display_address")
    private ArrayList<String> display_address;

    public ArrayList<String> getDisplayAddress() {
        return display_address;
    }

    public void setDisplay_address(ArrayList<String> display_address) {
        this.display_address = display_address;
    }
}
