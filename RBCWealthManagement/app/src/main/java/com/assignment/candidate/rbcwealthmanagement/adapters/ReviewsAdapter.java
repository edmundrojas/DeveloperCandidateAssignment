package com.assignment.candidate.rbcwealthmanagement.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.assignment.candidate.rbcwealthmanagement.R;
import com.assignment.candidate.rbcwealthmanagement.interfaces.RestaurantListCallback;
import com.assignment.candidate.rbcwealthmanagement.models.Review;

import java.util.ArrayList;

/**
 * Created by edmundrojas on 2017-08-28.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Review> reviewList;
    private RestaurantListCallback restaurantListCallback;

    public ReviewsAdapter(Context context, ArrayList<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_review, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.tvReview.setText(reviewList.get(position).getText());
        holder.tvReviewStars.setText(reviewList.get(position).getRating());

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvReviewStars;
        private TextView tvReview;

        public ViewHolder(View view) {
            super(view);

            tvReviewStars = (TextView) view.findViewById(R.id.tvReviewStars);
            tvReview = (TextView) view.findViewById(R.id.tvReview);

            view.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {

            final int adapterPosition = getAdapterPosition();

        }
    }

}
