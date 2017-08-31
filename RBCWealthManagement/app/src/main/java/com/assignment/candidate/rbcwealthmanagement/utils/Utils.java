package com.assignment.candidate.rbcwealthmanagement.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

import com.assignment.candidate.rbcwealthmanagement.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by edmundrojas on 2017-08-30.
 */

public class Utils {

    private static Utils instance = null;

    private Utils() {}

    public static synchronized Utils getInstance() {

        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public int getRandomColor(Context context){
        int[] array = context.getResources().getIntArray(R.array.randomColors);
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public String getLocationString(ArrayList<String> locationArray) {

        String location = "";
        for (String s : locationArray) {
            location = location + " " + s;
        }

        return location;
    }

    public boolean isTablet(Activity activity) {
        return (activity.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
