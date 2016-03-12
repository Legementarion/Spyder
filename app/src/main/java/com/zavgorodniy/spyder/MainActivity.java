package com.zavgorodniy.spyder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zavgorodniy.spyder.Locator.GPSTracker;
import com.zavgorodniy.spyder.connection.Connection;

public class MainActivity extends Activity {

    Button showLocation;
    Button changeServer;
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
        changeServer = (Button) findViewById(R.id.bt_change_srv);
        tvCurrentLocation = (TextView) findViewById(R.id.tv_coordinates);
        tvPreviousLocation = (TextView) findViewById(R.id.tv_prev_coordinates);
        telNumber = "";
        conn = new Connection();
        gps = GPSTracker.getInstance(this);
        // show location button click event
        changeServer.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                LayoutInflater inflater = getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.server_dialog, null);
                builder.setView(dialogView)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText valueIp = (EditText) dialogView.findViewById(R.id.server_ip);
                                EditText valuePort = (EditText) dialogView.findViewById(R.id.server_port);
                                if(conn.setUrl(valueIp.getText().toString(),valuePort.getText().toString()) !=1){
                                    Toast.makeText(getApplicationContext(), R.string.Wrong_Ip_Port, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        showLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                conn = new Connection();
                gps.getLocation();

                if (telNumber.equals("")) {
                    phoneNumber();
                } else {
                    init();
                }

            }
        });
    }

    private void init() {
        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());

            String coordinates = tvCurrentLocation.getText().toString();
            tvPreviousLocation.setText(coordinates);
            coordinates = "Lat: " + latitude + "\nLong: " + longitude;
            tvCurrentLocation.setText(coordinates);

            srt = latitude + ", " + longitude + ", " + telNumber;
            Toast.makeText(getApplicationContext(), "Your Location is - \n" + coordinates +
                    "\n" + telNumber, Toast.LENGTH_LONG).show();
            gps.stopUsingGPS();
            if (!telNumber.equals("")) {
                Thread thread = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                conn.sendData(telNumber, latitude, longitude);
                            }
                        }
                );
                thread.start();

            } else {
                Toast.makeText(getApplicationContext(), R.string.sim_card_error, Toast.LENGTH_LONG).show();
                phoneNumberInput();
            }
        } else {
            // can't get location
            // GPS or Network is not enabled message
            Toast.makeText(getApplicationContext(), R.string.gps_connect_error, Toast.LENGTH_LONG).show();
        }
    }

    public boolean phoneNumber() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            telNumber = tMgr.getLine1Number();
            return true;
        } catch (SecurityException ex) {
            phoneNumberInput();
            return true;
        }
    }

    public void phoneNumberInput() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.edit_dialog, null);
        builder.setView(dialogView)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText valueView = (EditText) dialogView.findViewById(R.id.editText);
                        if (valueView == null) Log.d("AA", "NULL");
                        else {
                            String value = valueView.getText().toString();
                            if (value.length() < 10 || value.length() > 13) {
                                phoneNumberInput();
                            } else {
                                telNumber = value;
                                init();
                            }
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}