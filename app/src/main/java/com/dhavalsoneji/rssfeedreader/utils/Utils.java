package com.dhavalsoneji.rssfeedreader.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    //2011-09-03T08:17:00.000-07:00//////////2011-09-03T08:17:00
    private static final String IP_DATE_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String OP_DATE_FORMAT_1 = "EEEE, dd MMMM yyyy";

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
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets an input stream.
     */
    public static InputStream downloadUrl(final URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setReadTimeout(AppConstants.NET_READ_TIMEOUT_MILLIS /* milliseconds */);
            conn.setConnectTimeout(AppConstants.NET_CONNECT_TIMEOUT_MILLIS /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
        return conn.getInputStream();
    }

    public static boolean isValidString(String str) {
        try {
            if (str != null && str.length() > 0 && !TextUtils.isEmpty(str)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    public static <T> Boolean isValidList(List<T> list) {
        try {
            if (list != null && !list.isEmpty() && list.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
        return false;
    }

    public static boolean validateUrl(String bloggerUrl) {
        try {
            URI uri = new URI(bloggerUrl);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public static boolean isValidBlogspotUrl(String url) {
        if (url.contains("blogspot")) {
            if (url.contains("feeds")) {
                if (url.contains("posts")) {
                    if (url.contains("default")) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String formatDate(String dateTime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(IP_DATE_FORMAT_1);
        SimpleDateFormat outputFormat = new SimpleDateFormat(OP_DATE_FORMAT_1);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            Applog.e(TAG, e.getMessage(), e);
        }
        return str;
    }
}
