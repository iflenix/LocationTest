package com.example.home.locationtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

/**
 * Created by HOME on 10.07.2015.
 */
public class LocationUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Location location = (Location)intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);

        Toast.makeText(context,String.valueOf(location.getLatitude()) + ":" +
                String.valueOf(location.getLongitude()),Toast.LENGTH_LONG).show();

    }
}
