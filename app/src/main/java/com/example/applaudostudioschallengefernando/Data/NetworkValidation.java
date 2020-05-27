package com.example.applaudostudioschallengefernando.Data;

import android.content.Context;
import android.net.ConnectivityManager;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class NetworkValidation {

    public boolean isNetworkConnected(Context cContext) {
        ConnectivityManager cm = (ConnectivityManager) cContext.getSystemService(cContext.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean isInternetAvailable() {
        try {
            String hostname = new URL("https://www.google.com").getHost();
            InetAddress address = InetAddress.getByName(hostname);
            return !address.equals("");
        } catch (UnknownHostException | MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
