package com.assignment.candidate.rbcwealthmanagement.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.assignment.candidate.rbcwealthmanagement.R;
import com.assignment.candidate.rbcwealthmanagement.interfaces.RestaurantListCallback;
import com.assignment.candidate.rbcwealthmanagement.models.Business;
import com.assignment.candidate.rbcwealthmanagement.utils.Utils;

import java.util.ArrayList;

/**
 * Created by edmundrojas on 2017-08-28.
 */

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Business> restaurantList;
    private RestaurantListCallback restaurantListCallback;

    public RestaurantsAdapter(Context context, ArrayList<Business> restaurantList, RestaurantListCallback restaurantListCallback) {
        this.context = context;
        this.restaurantList = restaurantList;
        this.restaurantListCallback = restaurantListCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_grid_restaurant, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.tvName.setText(restaurantList.get(position).getName());
        holder.tvLocation.setText(Utils.getInstance().getLocationString(restaurantList.get(position).getLocation().getDisplayAddress()));

        GradientDrawable bgShape = (GradientDrawable) holder.rlRoundedBg.getBackground();
        bgShape.setColor(restaurantList.get(position).getRandomColor());

    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName;
        private TextView tvLocation;
        private RelativeLayout rlRoundedBg;

        public ViewHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tvRestaurantName);
            tvLocation = (TextView) view.findViewById(R.id.tvLocation);
            rlRoundedBg = (RelativeLayout) view.findViewById(R.id.rlRoundedBackground);

            view.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {

            final int adapterPosition = getAdapterPosition();
            restaurantListCallback.onListItemClick(restaurantList.get(adapterPosition), adapterPosition, view);

        }
    }

}
