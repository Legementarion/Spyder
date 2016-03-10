package com.zavgorodniy.spyder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zavgorodniy.spyder.Locator.GPSTracker;

public class MainActivity extends Activity {

    Button showLocation;
    TextView tvCurrentLocation;
    TextView tvPreviousLocation;

    GPSTracker gps;

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

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    String coordinates = tvCurrentLocation.getText().toString();
                    tvPreviousLocation.setText(coordinates);
                    coordinates = "Lat: " + latitude + "\nLong: " + longitude;
                    tvCurrentLocation.setText(coordinates);

                    Toast.makeText(getApplicationContext(), "Your Location is - \n" + coordinates, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });
    }
}

