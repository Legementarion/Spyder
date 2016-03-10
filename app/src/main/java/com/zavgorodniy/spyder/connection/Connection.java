package com.zavgorodniy.spyder.connection;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ksayker on 10.03.16.
 */
public class Connection {

    private String stringUrl = "http://195.140.162.44:8080/spyder/records";

    private JSONObject buildJSON(String imei, String latitude, String longitude) {
        JSONObject json = new JSONObject();
        try {
            json.put("phone", imei);
            json.put("latitude", latitude);
            json.put("longitude", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void sendData(String phone, String latitude, String longitude) {
        JSONObject json = buildJSON(phone, latitude, longitude);
        URL url;
        HttpURLConnection connection;
        DataOutputStream out = null;
        try {
            url = new URL(stringUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            out = new DataOutputStream(connection.getOutputStream());
            out.write(json.toString().getBytes());
            out.flush();
            Log.d("sended data: ", json.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
