package com.zavgorodniy.spyder.connection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator for ip address and port.
 *
 * @author ksayker
 * @version 0.1
 * @date 10.03.2016
 */
public class Validator {
    /** regular expression for ip.*/
    private String regExpForIp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    /** regular expression for port.*/
    private String regExpForPort = "^0*(?:6553[0-5]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{1,3}|[0-9])$";

    /**
     * validate ip.
     *
     * @param ip string for validating.
     * @return true if ip complies else return false.
     */
    public boolean validateIp(String ip){
        Pattern pattern = Pattern.compile(regExpForIp);
        Matcher matcher = pattern.matcher(ip);
        System.out.println(ip + "-" + matcher.matches());
        return matcher.matches();
    }

    /**
     * validate ip.
     *
     * @param port string for validating.
     * @return true if port complies else return false.
     */
    public boolean validatePort(String port){
        Pattern pattern = Pattern.compile(regExpForPort);
        Matcher matcher = pattern.matcher(port);
        return matcher.matches();
    }

}
