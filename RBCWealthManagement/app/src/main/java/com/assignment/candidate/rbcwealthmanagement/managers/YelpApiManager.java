package com.assignment.candidate.rbcwealthmanagement.managers;

import android.util.Log;

import com.assignment.candidate.rbcwealthmanagement.interfaces.ServiceCallback;
import com.assignment.candidate.rbcwealthmanagement.models.ErrorResponse;
import com.assignment.candidate.rbcwealthmanagement.models.RestaurantsResult;
import com.assignment.candidate.rbcwealthmanagement.models.Reviews;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by edmundrojas on 2017-08-29.
 */

public class YelpApiManager {

    private static final String BASE_URL = "https://api.yelp.com/v3/";
    private static final String ACCESS_TOKEN = "1N3mkwuKgWR-4CYGxUi4OVxsSKqfmxjUqm1BXmA81h353nDGrke1eWRpsHfW9nkGF4ZFgXSwUNXQM5LF9e5ppoTy1e3ht0MSoees4JuEPFRZD7fziQ0GqF8DD7KkWXYx";

    public YelpApiManager() {}

    private interface YelpApiInterface {

        @GET("businesses/search")
        Call<RestaurantsResult> getRestaurantList(
                @Query("latitude") String latitude,
                @Query("longitude") String longitude,
                @Query("limit") Integer limit);

        @GET("businesses/{id}/reviews")
        Call<Reviews> getReviews(
                @Path("id") String id);
    }

    private Retrofit createClient() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public void getRestaurants(String latitude, String longitude, Integer limit, final ServiceCallback<RestaurantsResult> callback) {
        HashMap<String, Object> payload = new HashMap<String, Object>();
        payload.put("latitude", latitude);
        payload.put("longitude", longitude);
        payload.put("limit", limit);

        YelpApiInterface yelpApiInterface = createClient().create(YelpApiInterface.class);
        Call<RestaurantsResult> call = yelpApiInterface.getRestaurantList(latitude, longitude, limit);

        performRestaurantCall(call, callback);
    }

    public void getReviews(String id, final ServiceCallback<Reviews> callback){
        HashMap<String, Object> payload = new HashMap<String, Object>();
        payload.put("id", id);

        YelpApiInterface yelpApiInterface = createClient().create(YelpApiInterface.class);
        Call<Reviews> call = yelpApiInterface.getReviews(id);

        performReviewsCall(call, callback);
    }

    private void performRestaurantCall(final Call call, final ServiceCallback serviceCallback) {
        call.enqueue(new Callback<RestaurantsResult>() {
            @Override
            public void onResponse(Call call, Response response) {
                String responseCode = String.valueOf(response.code());

                Log.d("YelpApiManager", "Call finished " + call.request().url().toString() + ", STATUS: " + responseCode);

                if (response.code() == 200) {
                    RestaurantsResult serverResponse = (RestaurantsResult) response.body();

                    if (serviceCallback != null) {
                        serviceCallback.onSuccess(serverResponse);
                    }

                }
            }

            @Override
            public void onFailure(Call<RestaurantsResult> response, Throwable t) {
                Log.d("YelpApiManager", "Call Failed " + call.request().url().toString() + ", exception: " + t.getMessage());

                String errorMessage = "";
                if (t != null) {
                    errorMessage = t.getMessage();
                }

                if (serviceCallback != null) {
                    ErrorResponse defaultError = new ErrorResponse();
                    defaultError.setStatus(500);
                    defaultError.setMessage(errorMessage);
                    serviceCallback.onFailure(defaultError);
                }
            }
        });
    }

    private void performReviewsCall(final Call call, final ServiceCallback serviceCallback) {
        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call call, Response response) {
                String responseCode = String.valueOf(response.code());

                Log.d("YelpApiManager", "Call finished " + call.request().url().toString() + ", STATUS: " + responseCode);

                if (response.code() == 200) {
                    Reviews serverResponse = (Reviews) response.body();

                    if (serviceCallback != null) {
                        serviceCallback.onSuccess(serverResponse);
                    }

                }
            }

            @Override
            public void onFailure(Call<Reviews> response, Throwable t) {
                Log.d("YelpApiManager", "Call Failed " + call.request().url().toString() + ", exception: " + t.getMessage());

                String errorMessage = "";
                if (t != null) {
                    errorMessage = t.getMessage();
                }

                if (serviceCallback != null) {
                    ErrorResponse defaultError = new ErrorResponse();
                    defaultError.setStatus(500);
                    defaultError.setMessage(errorMessage);
                    serviceCallback.onFailure(defaultError);
                }
            }
        });
    }

}
