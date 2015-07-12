package com.example.home.locationtest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationActivity extends Activity {
    LocationManager locationManager;
    TextView longtitudeText, latitudeText;
    double curLongtitude, curLatitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        longtitudeText = (TextView) findViewById(R.id.tvLongtitude);
        latitudeText = (TextView) findViewById(R.id.tvLatitude);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        Intent intent = new Intent(this, LocationUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        locationManager.removeUpdates(pendingIntent);
        super.onPause();
    }

    private LocationListener myLocListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            curLongtitude = location.getLongitude();
            longtitudeText.setText(String.valueOf(curLongtitude));
            curLatitude = location.getLatitude();
            latitudeText.setText(String.valueOf(curLatitude));

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void onButtonGetLocation(View view) {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocListener);
        Intent intent = new Intent(this, LocationUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                pendingIntent);

        String addressStr = "No address found";

        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gc.getFromLocation(curLatitude, curLongtitude, 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
               sb.append(address.getLocality()).append(", ").append(address.getAddressLine(2)).append(", ");
                sb.append(address.getPostalCode()).append(", ");
                sb.append(address.getCountryName()).append(address.getCountryCode());
            }
            addressStr = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView addressView = (TextView) findViewById(R.id.addressTextView);
        addressView.setText("Address: " + addressStr);

        Intent mapIntent = new Intent(this,MapsViewActivity.class);
        intent.putExtra("LOGTITUDE_EXTRA",curLongtitude);
        intent.putExtra("LATITUDE_EXTRA",curLatitude);

        startActivity(mapIntent);

    }


}
