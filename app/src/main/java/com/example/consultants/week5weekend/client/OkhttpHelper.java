package com.example.consultants.week5weekend.client;

import android.util.Log;

import com.example.consultants.week5weekend.model.data.LocationResponse;
import com.example.consultants.week5weekend.utils.LatLngCallback;
import com.example.consultants.week5weekend.utils.NetworkHelper;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpHelper {
    private static final String TAG = OkhttpHelper.class.getSimpleName() + "_TAG";

    OkHttpClient client;
    private Request request;

    public OkhttpHelper(String location) {

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(NetworkHelper.BASE_URL)
                .addPathSegment("maps")
                .addPathSegment("api")
                .addPathSegment("geocode")
                .addPathSegment("json")
                .addQueryParameter("address", location)
                .addQueryParameter("key", NetworkHelper.API_KEY)
                .build();

        Log.d(TAG, "OkhttpHelper: " + url.toString());

        client = new OkHttpClient.Builder()
                .build();

        request = new Request.Builder()
                .url(url)
                .build();

    }

    public void enqueue(LatLngCallback latLngCall) {

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latLngCall.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Gson gson = new Gson();
                LocationResponse locationResponse = gson.fromJson(response.body().string(), LocationResponse.class);

                Log.d(TAG, "onResponse: "+ locationResponse.getResults().get(0).getGeometry().getLocation().getLat());

                latLngCall.onSuccess(locationResponse);
            }
        });


    }
}
