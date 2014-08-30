package com.doorstop.liz.arrivalnotifier;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by liz on 19/08/14.
 */
public class GooglePlayServicer {

    public static String LOG_NAME = GooglePlayServicer.class.getName();

    public GooglePlayServicer() {
        //Do nothing
    }

    public boolean areGooglePlayServicesAvailable(Context context) {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {

            Log.d(LOG_NAME, "Google Play Services connection available");

            return true;

            // Google Play services was not available for some reason
        } else {

            Log.d(LOG_NAME, "Google Play Services connection unavailable");

            return false;

        }
    }
}
