package com.assignment.candidate.rbcwealthmanagement.activities;

import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.assignment.candidate.rbcwealthmanagement.R;
import com.assignment.candidate.rbcwealthmanagement.adapters.RestaurantsAdapter;
import com.assignment.candidate.rbcwealthmanagement.interfaces.RestaurantListCallback;
import com.assignment.candidate.rbcwealthmanagement.managers.FavouritesDbManager;
import com.assignment.candidate.rbcwealthmanagement.models.Business;
import com.assignment.candidate.rbcwealthmanagement.utils.Utils;

import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<Business> businesses = new ArrayList<Business>();
    private RecyclerView.LayoutManager layoutManager;
    private RestaurantsAdapter adapter;
    private ProgressBar pb;
    private static FavouritesDbManager fdb;

    private static final int NUM_ROWS_PHONE = 2;
    private static final int NUM_ROWS_TABLET = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        fdb = new FavouritesDbManager(this);
        setupViews();
        new LoadFavouritesFromDb().execute();
    }

    private void setupViews() {
        pb = (ProgressBar) findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void setupRecyclerView(ArrayList<Business> businesses) {

        adapter = new RestaurantsAdapter(this, businesses, new RestaurantListCallback() {
            @Override
            public void onListItemClick(Business business, Integer position, View view) {

                Intent intent = new Intent(FavouritesActivity.this, DetailActivity.class);
                intent.putExtra(getString(R.string.EXTRA_ID), business.getId());
                intent.putExtra(getString(R.string.EXTRA_NAME), business.getName());
                intent.putExtra(getString(R.string.EXTRA_LOCATION), Utils.getInstance().getLocationString(business.getLocation().getDisplayAddress()));
                intent.putExtra(getString(R.string.EXTRA_IMAGE_URL), business.getImage_url());
                intent.putExtra(getString(R.string.EXTRA_RANDOM_COLOR), business.getRandomColor());

                Pair<View, String> p1 = Pair.create(view, getString(R.string.TRANSITION_RESTAURANT_ROW));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(FavouritesActivity.this, p1);
                startActivity(intent, options.toBundle());

            }
        });
        layoutManager = new GridLayoutManager(this, getNumColumns());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        pb.setVisibility(View.GONE);
    }

    private class LoadFavouritesFromDb extends AsyncTask<Void, Void, ArrayList<Business>> {

        @Override
        protected ArrayList<Business> doInBackground(Void... voids) {

            ArrayList<Business> businesses = null;
            try {
                fdb.open();
                businesses = fdb.getBusinesses();
                fdb.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return businesses;
        }

        @Override
        protected void onPostExecute(ArrayList<Business> businesses) {
            super.onPostExecute(businesses);

            if (businesses != null) {
                setupRecyclerView(businesses);
            }
        }
    }

    private int getNumColumns() {

        if (Utils.getInstance().isTablet(this)) {
            return NUM_ROWS_TABLET;
        } else {
            return NUM_ROWS_PHONE;
        }
    }

}
