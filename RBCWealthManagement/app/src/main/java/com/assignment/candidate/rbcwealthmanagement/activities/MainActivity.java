package com.assignment.candidate.rbcwealthmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.assignment.candidate.rbcwealthmanagement.R;
import com.assignment.candidate.rbcwealthmanagement.adapters.RestaurantsAdapter;
import com.assignment.candidate.rbcwealthmanagement.interfaces.RestaurantListCallback;
import com.assignment.candidate.rbcwealthmanagement.interfaces.ServiceCallback;
import com.assignment.candidate.rbcwealthmanagement.managers.YelpApiManager;
import com.assignment.candidate.rbcwealthmanagement.models.Business;
import com.assignment.candidate.rbcwealthmanagement.models.ErrorResponse;
import com.assignment.candidate.rbcwealthmanagement.models.RestaurantsResult;
import com.assignment.candidate.rbcwealthmanagement.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private static final String LATITUDE_TORONTO_CITY_HALL = "43.653908";
    private static final String LONGITUDE_TORONTO_CITY_HALL = "-79.384293";

    private static final int NUM_ROWS_PHONE = 2;
    private static final int NUM_ROWS_TABLET = 4;
    private static final int RESPONSE_LIMIT = 10;

    private YelpApiManager yelpApiManager;
    private ProgressBar pb;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RestaurantsAdapter adapter;
    private RadioGroup radioGroup;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    ArrayList<Business> businesses = new ArrayList<Business>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        yelpApiManager = new YelpApiManager();

        setupViews();

        loadRestaurantsList();
    }

    private void setupViews() {

        pb = (ProgressBar) findViewById(R.id.progressbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        setupToolbar();
        setupRadioGroup();
        setupNavigationDrawer();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupNavigationDrawer() {

        drawer = (DrawerLayout) findViewById(R.id.dlDrawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_favourites) {
                    Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
                    startActivity(intent);
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void setupRadioGroup() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                if (i == R.id.radioButtonAsc) {
                    sortListAsc();
                } else {
                    sortListDesc();
                }

            }
        });
    }

    private void loadRestaurantsList() {
        yelpApiManager.getRestaurants(LATITUDE_TORONTO_CITY_HALL, LONGITUDE_TORONTO_CITY_HALL, RESPONSE_LIMIT, new ServiceCallback<RestaurantsResult>() {
            @Override
            public void onSuccess(RestaurantsResult successResponse) {

                Log.d("yelpApiManager", "" + successResponse);
                businesses = successResponse.getBusinesses();
                sortListAsc();
            }

            @Override
            public void onFailure(ErrorResponse errorResponse) {

            }

            @Override
            public void onAuthenticationRequired() {

            }
        });
    }

    private void setupRecyclerView(ArrayList<Business> businesses) {

        setRandomColors();
        adapter = new RestaurantsAdapter(this, businesses, new RestaurantListCallback() {
            @Override
            public void onListItemClick(Business business, Integer position, View view) {

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(getString(R.string.EXTRA_ID), business.getId());
                intent.putExtra(getString(R.string.EXTRA_NAME), business.getName());
                intent.putExtra(getString(R.string.EXTRA_LOCATION), Utils.getInstance().getLocationString(business.getLocation().getDisplayAddress()));
                intent.putExtra(getString(R.string.EXTRA_IMAGE_URL), business.getImage_url());
                intent.putExtra(getString(R.string.EXTRA_RANDOM_COLOR), business.getRandomColor());

                Pair<View, String> p1 = Pair.create(view, getString(R.string.TRANSITION_RESTAURANT_ROW));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, p1);
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

    private int getNumColumns() {

        if (Utils.getInstance().isTablet(this)) {
            return NUM_ROWS_TABLET;
        } else {
            return NUM_ROWS_PHONE;
        }
    }

    private void setRandomColors() {

        for (Business b : businesses) {
            b.setRandomColor(Utils.getInstance().getRandomColor(this));
        }

    }

    private void sortListAsc() {
        if (businesses.size() > 0) {
            Collections.sort(businesses, AscComparator);
            setupRecyclerView(businesses);
        }
    }

    private void sortListDesc() {
        if (businesses.size() > 0) {
            Collections.sort(businesses, DescComparator);
            setupRecyclerView(businesses);
        }
    }

    Comparator AscComparator = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            Business p1 = (Business) o1;
            Business p2 = (Business) o2;
            return p1.getName().compareToIgnoreCase(p2.getName());
        }
    };

    Comparator DescComparator = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            Business p1 = (Business) o1;
            Business p2 = (Business) o2;
            return p2.getName().compareToIgnoreCase(p1.getName());
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dlDrawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
