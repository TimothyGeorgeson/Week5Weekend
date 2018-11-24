package com.example.consultants.week5weekend.utils;

import com.example.consultants.week5weekend.model.data.LocationResponse;

public interface LatLngCallback {

    void onSuccess(LocationResponse locationResponse);

    void onFailure(String error);
}
