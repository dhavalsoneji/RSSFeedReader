package com.dhavalsoneji.rssfeedreader.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dhaval Soneji on 30/11/16.
 */

public class Utils {
    /**
     * Check Internet Available or Not
     */
    public static boolean checkInternetConnection(final Context context) {
        final ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets an input stream.
     */
    public static InputStream downloadUrl(final URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(AppConstants.NET_READ_TIMEOUT_MILLIS /* milliseconds */);
        conn.setConnectTimeout(AppConstants.NET_CONNECT_TIMEOUT_MILLIS /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
