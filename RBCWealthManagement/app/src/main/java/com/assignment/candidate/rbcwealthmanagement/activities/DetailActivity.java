package com.assignment.candidate.rbcwealthmanagement.activities;

import android.Manifest;
import android.animation.Animator;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.assignment.candidate.rbcwealthmanagement.R;
import com.assignment.candidate.rbcwealthmanagement.adapters.ReviewsAdapter;
import com.assignment.candidate.rbcwealthmanagement.interfaces.ServiceCallback;
import com.assignment.candidate.rbcwealthmanagement.managers.FavouritesDbManager;
import com.assignment.candidate.rbcwealthmanagement.managers.YelpApiManager;
import com.assignment.candidate.rbcwealthmanagement.models.ErrorResponse;
import com.assignment.candidate.rbcwealthmanagement.models.Review;
import com.assignment.candidate.rbcwealthmanagement.models.Reviews;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity {

    private String id, name, location, imageUrl;
    private int randomColor;
    private View transitionView;
    private LinearLayout llDetailsContainer;
    private RelativeLayout rlRestaurantInfo;
    private ImageView ivBannerImage;
    private boolean transitionIn = true;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ReviewsAdapter adapter;
    private ArrayList<Review> reviews;
    private TextView tvLocation;
    private ProgressBar pb;
    private ImageView ivLocationIcon;
    private FloatingActionButton fab;
    private static FavouritesDbManager fdb;

    private static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 1;

    private YelpApiManager yelpApiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        yelpApiManager = new YelpApiManager();
        fdb = new FavouritesDbManager(this);

        getExtras();
        setupViews();

        loadReviewsList();
        setupEnterAnimation();
    }

    private void loadReviewsList() {

        yelpApiManager.getReviews(id, new ServiceCallback<Reviews>() {
            @Override
            public void onSuccess(Reviews successResponse) {

                Log.d("yelpApiManager", "" + successResponse);
                reviews = successResponse.getReviews();
                setupRecyclerView();
            }

            @Override
            public void onFailure(ErrorResponse errorResponse) {

            }

            @Override
            public void onAuthenticationRequired() {

            }
        });

    }

    private void setupViews() {

        getSupportActionBar().setTitle(name);

        pb = (ProgressBar) findViewById(R.id.progressbar);

        ivLocationIcon = (ImageView) findViewById(R.id.ivLocationIcon);

        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvLocation.setText(location);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        llDetailsContainer = (LinearLayout) findViewById(R.id.llDetailsContainer);
        llDetailsContainer.setBackgroundColor(randomColor);

        rlRestaurantInfo = (RelativeLayout) findViewById(R.id.rlRestaurantInfo);

        ivBannerImage = (ImageView) findViewById(R.id.ivBannerImage);
        new BasicLoadImageFromUrl().execute(imageUrl);

        transitionView = (View) findViewById(R.id.vwTransition);
        GradientDrawable bgShape = (GradientDrawable) transitionView.getBackground();
        bgShape.setColor(randomColor);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkExternalStoragePermissions();
            }
        });
    }

    private void getExtras() {
        id = getIntent().getExtras().getString(getString(R.string.EXTRA_ID));
        name = getIntent().getExtras().getString(getString(R.string.EXTRA_NAME));
        location = getIntent().getExtras().getString(getString(R.string.EXTRA_LOCATION));
        imageUrl = getIntent().getExtras().getString(getString(R.string.EXTRA_IMAGE_URL));
        randomColor = getIntent().getExtras().getInt(getString(R.string.EXTRA_RANDOM_COLOR));
    }

    private void setupRecyclerView() {

        adapter = new ReviewsAdapter(this, reviews);
        layoutManager = new LinearLayoutManager(DetailActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        pb.setVisibility(View.GONE);
    }

    private class BasicLoadImageFromUrl extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            URL url = null;
            Bitmap bmp = null;
            try {
                url = new URL(strings[0]);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ivBannerImage.setImageBitmap(bitmap);

        }
    }

    private void setupEnterAnimation() {

        getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                Log.d("setupEnterAnimation()", "onTransitionStart");

                if (transitionIn == false) {
                    llDetailsContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                Log.d("setupEnterAnimation()", "onTransitionEnd");

                if (transitionIn == true) {

                    llDetailsContainer.setVisibility(View.VISIBLE);
                    animateTransitionViewsIn();
                    transitionIn = false;
                }
            }

            @Override
            public void onTransitionCancel(Transition transition) {
                Log.d("setupEnterAnimation()", "onTransitionCancel");
            }

            @Override
            public void onTransitionPause(Transition transition) {
                Log.d("setupEnterAnimation()", "onTransitionPause");
            }

            @Override
            public void onTransitionResume(Transition transition) {
                Log.d("setupEnterAnimation()", "onTransitionResume");
            }
        });

    }

    private void animateTransitionViewsIn() {

        int centerX = (transitionView.getLeft() + transitionView.getRight()) / 2;
        int centerY = (llDetailsContainer.getTop() + llDetailsContainer.getBottom()) / 2;

        int startRadius = 0;
        int endRadius = Math.max(llDetailsContainer.getWidth(), llDetailsContainer.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(llDetailsContainer, centerX, centerY, startRadius, endRadius);
        circularReveal.setDuration(300);
        circularReveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                ivBannerImage.setVisibility(View.VISIBLE);
                rlRestaurantInfo.setVisibility(View.VISIBLE);
                ivLocationIcon.setVisibility(View.VISIBLE);
                tvLocation.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        circularReveal.setInterpolator(new AccelerateDecelerateInterpolator());

        circularReveal.start();
    }

    private void animateTransitionViewsOut() {

        int centerX = (transitionView.getLeft() + transitionView.getRight()) / 2;
        int centerY = (llDetailsContainer.getTop() + llDetailsContainer.getBottom()) / 2;

        int startRadius = Math.max(llDetailsContainer.getWidth(), llDetailsContainer.getHeight());
        int endRadius = 0;

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(llDetailsContainer, centerX, centerY, startRadius, endRadius);
        circularReveal.setDuration(300);
        circularReveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                llDetailsContainer.setVisibility(View.INVISIBLE);
                supportFinishAfterTransition();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        circularReveal.setInterpolator(new AccelerateDecelerateInterpolator());

        circularReveal.start();

    }

    private void checkExternalStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_EXTERNAL_STORAGE);
        } else {
            new AddFavouriteToDb().execute();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new AddFavouriteToDb().execute();
                }
                return;
            }
        }
    }

    private void showFavouriteAddedSnackbar() {
        Snackbar.make(fab, name + " added to favourites", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private class AddFavouriteToDb extends AsyncTask<Void, Void, Long> {

        @Override
        protected Long doInBackground(Void... voids) {

            Long success = null;
            try {
                fdb.open();
                success = fdb.addFavourite(id, name, imageUrl, location, randomColor);
                fdb.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return success;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);

            showFavouriteAddedSnackbar();
        }
    }

    @Override
    public void onBackPressed() {
        animateTransitionViewsOut();
    }

}
