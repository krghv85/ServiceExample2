package com.poswal.serviceecample;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Created by komal on 12-07-2017.
 */

public class MyService extends Service implements LocationListener {

    String imei;
    Location location;
    double latitude;
    double longitude;
    double time;
    String provide;

    String log;
    String lat;
    String tim;
    String prov;
    String addressText;
    String dateString;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        showToast("Service Created");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showToast("Service onStartCommand");
        locationNetwork();
        locationGps();


        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String s3 = telephonyManager.getDeviceId();

     /*   long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm:ss a");
        dateString = sdf.format(date);*/

//        addressText = getLocationAddress();

        log = longitude + " ";
        lat = latitude + "";
        prov = provide + "";
        imei = s3;
        showToast(" latitude= " + latitude + "\nlongitude=  " + longitude + "\ndateString=  " + dateString + "\nprovide=  " +
                provide + "\naddress= " + getLocationAddress()+"\n IMEI : "+imei);

        MainActivity.update(latitude,longitude,dateString,provide,getLocationAddress(),imei);


        return Service.START_STICKY;

    }

    public void locationGps() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        Log.d("GPS", "GPS");
        if (locationManager != null) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                time = location.getTime();
                provide = location.getProvider();


            }
        }

    }


    private void locationNetwork() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        Log.d("Network", "Network");

        if (locationManager != null) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                time = location.getTime();
                provide = location.getProvider();
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        showToast("Service onBind");

        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        showToast("Service onUnbind");

        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        showToast("Service onRebind");

        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        showToast("Service onTaskRemoved");

    }

    public String getLocationAddress() {


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        // Get the current location from the input parameter list
        // Create a list to contain the result address
        List<Address> addresses = null;
        try {
                /*
                 * Return 1 address.
				 */
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (Exception e) {

            return ("IO Exception trying to get address:" + e.getMessage());
        }        // If the reverse geocode returned an address


        if (addresses != null && addresses.size() > 0) {
            // Get the first address
            Address address = addresses.get(0);
                /*
                 * Format the first line of address (if available), city, and
				 * country name.
				 */

            String addressText = String.format(
                    "%s, %s,%s,%s",
                    // If there's a street address, add it
                    address.getMaxAddressLineIndex() > 0 ? address
                            .getAddressLine(0) : "",
                    address.getSubLocality(),

                    // Locality is usually a city
                    address.getLocality(),
                    // The country of the address
                    address.getCountryName());
            // Return the text
            return addressText;
        } else {
            return "No address found by the service: Note to the developers, If no address is found by google itself, there is nothing you can do about it. :(";
        }


    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
