package com.assignment.candidate.rbcwealthmanagement.interfaces;

import android.view.View;

import com.assignment.candidate.rbcwealthmanagement.models.Business;

/**
 * Created by edmundrojas on 2017-08-28.
 */

public interface RestaurantListCallback {
    void onListItemClick(Business business, Integer position, View view);
}
