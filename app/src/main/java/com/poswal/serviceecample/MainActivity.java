package com.poswal.serviceecample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.renderscript.Double2;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView= (TextView) findViewById(R.id.textview);

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlertSettings();
        }
        int seconds = 1*60;
        Intent intent = new Intent(this, MyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime(),(seconds * 200)	,pendingIntent);
//        Toast.makeText(this, "Alarm set in " + seconds + " seconds", Toast.LENGTH_LONG).show();



        showToast("call Service");
    }

    public void showAlertSettings() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public  void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


        public static void update(Double lat, Double lng, String date, String provider, String address, String imei)
        {
            textView.setText(" latitude= " + lat + "\nlongitude=  " + lng + "\ndateString=  " + date + "\nprovide=  " +
                    provider + "\naddress= " +address+"\n IMEI : "+imei);
        }

}
