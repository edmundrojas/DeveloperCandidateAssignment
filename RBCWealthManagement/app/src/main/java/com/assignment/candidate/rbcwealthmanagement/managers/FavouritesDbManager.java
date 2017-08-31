package com.assignment.candidate.rbcwealthmanagement.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.assignment.candidate.rbcwealthmanagement.models.Business;
import com.assignment.candidate.rbcwealthmanagement.models.Location;

import java.util.ArrayList;

/**
 * Created by edmundrojas on 2017-08-30.
 */

public class FavouritesDbManager {

    private DbHelper favouritesHelper;
    private final Context context;
    private SQLiteDatabase favouritesDatabase;

    public static final String ID = "id";
    public static final String RESTAURANT_ID = "restaurant_id";
    public static final String NAME = "name";
    public static final String IMAGE_URL = "image_url";
    public static final String LOCATION = "location";
    public static final String RANDOM_COLOR = "random_color";

    private static final String DATABASE_NAME = "FavouritesDb";
    private static final String DATABASE_TABLE = "favouritesTable";
    private static final int DATABASE_VERSION = 1;

    public FavouritesDbManager(Context context) {
        this.context = context;
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + RESTAURANT_ID
                    + " TEXT NOT NULL, " + NAME
                    + " TEXT NOT NULL, " + IMAGE_URL
                    + " TEXT NOT NULL, " + LOCATION
                    + " TEXT NOT NULL, " + RANDOM_COLOR
                    + " INTEGER NOT NULL);"

            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

    }

    public ArrayList<Business> getBusinesses() {

        ArrayList<Business> businesses = new ArrayList<Business>();

        String[] columns = new String[] { RESTAURANT_ID, NAME, IMAGE_URL, LOCATION, RANDOM_COLOR };
        Cursor c = favouritesDatabase.query(DATABASE_TABLE, columns, null, null,
                null, null, NAME + " COLLATE NOCASE;");

        int rowName = c.getColumnIndex(NAME);
        int rowRestaurantId = c.getColumnIndex(RESTAURANT_ID);
        int rowImageUrl = c.getColumnIndex(IMAGE_URL);
        int rowLocation = c.getColumnIndex(LOCATION);
        int rowRandomColor = c.getColumnIndex(RANDOM_COLOR);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            Business business = new Business();
            business.setId(c.getString(rowRestaurantId));
            business.setName(c.getString(rowName));
            business.setImage_url(c.getString(rowImageUrl));
            business.setLocation(getLocation(c.getString(rowLocation)));
            business.setRandomColor(c.getInt(rowRandomColor));

            businesses.add(business);
        }

        c.close();

        return businesses;

    }

    private Location getLocation(String l){

        Location location = new Location();
        ArrayList<String> locationArray = new ArrayList<>();
        locationArray.add(l);
        location.setDisplay_address(locationArray);

        return location;
    }

    public Integer favouriteExists(String id) {

        Cursor mCount = favouritesDatabase
                .rawQuery("select * from " + DATABASE_TABLE + " where " + RESTAURANT_ID + "='" + id + "'", null);
        mCount.moveToFirst();
        Integer count = mCount.getCount();
        mCount.close();

        return count;
    }

    public long addFavourite(String restaurantId, String name,
                             String imageURL, String location, Integer randomColor) {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();

        cv.put(RESTAURANT_ID, restaurantId);
        cv.put(NAME, name);
        cv.put(IMAGE_URL, imageURL);
        cv.put(LOCATION, location);
        cv.put(RANDOM_COLOR, randomColor);

        return favouritesDatabase.insert(DATABASE_TABLE, null, cv);
    }

    public FavouritesDbManager open() throws SQLException {
        favouritesHelper = new DbHelper(context);
        favouritesDatabase = favouritesHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        favouritesHelper.close();
    }

}
