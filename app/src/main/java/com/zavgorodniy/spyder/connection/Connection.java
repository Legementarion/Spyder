package com.zavgorodniy.spyder.connection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Connection to server for sending phone number and coordinate.
 *
 * @author ksayker
 * @version 0.1
 * @date 10.03.2016
 */
public class Connection {

    /** success state.*/
    public static final int SUCCESS = 1;
    /** error state.*/
    public static final int ERROR = 2;

    /** ip and port validator.*/
    private Validator validator;

    /** http head.*/
    private String stringHttp;
    /** current ip address.*/
    private String stringIp;
    /** current port.*/
    private String stringPort;
    /** url path from request*/
    private String stringUrlPath;

    /**
     * Create new connection.
     */
    public Connection() {
        validator = new Validator();
        stringHttp = "http://";
        stringUrlPath = "/spyder/records";
    }

    /**
     * Packs data in to json.
     *
     * @param imei imei data.
     * @param latitude coordinate latitude.
     * @param longitude coordinate longitude.
     * @return json object with data.
     */
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

    /**
     * Set ip address and port fo the server.
     * @param ip ip address of the server.
     * @param port port of the server.
     * @return  return SUCCESS when ip and port is validated else return ERROR.
     */
    public int setUrl(String ip, String port) {
        int result = ERROR;
        if (validator.validateIp(ip) && validator.validatePort(port)) {
            stringIp = ip;
            stringPort = port;
            result = SUCCESS;
        }

        return result;
    }

    /**
     * Send data to current server.
     * @param phone phone number.
     * @param latitude coordinate latitude.
     * @param longitude coordinate longitude.
     * @return  return SUCCESS when data successful send else return ERROR.
     */
    public int sendData(String phone, String latitude, String longitude) {
        int result = ERROR;
        if (stringIp != null && stringPort != null){
            JSONObject json = buildJSON(phone, latitude, longitude);
            URL url;
            HttpURLConnection connection;
            DataOutputStream out;
            try {
                url = new URL(stringHttp + stringIp + ":" + stringPort + stringUrlPath);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                out = new DataOutputStream(connection.getOutputStream());
                out.write(json.toString().getBytes());
                out.flush();
                out.close();
                result = SUCCESS;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
