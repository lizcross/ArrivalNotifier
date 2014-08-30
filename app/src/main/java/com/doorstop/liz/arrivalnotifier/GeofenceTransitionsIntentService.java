package com.doorstop.liz.arrivalnotifier;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import java.util.List;

/**
 * Created by liz on 26/08/14.
 */
public class GeofenceTransitionsIntentService extends IntentService {

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //*****Copied from Google Geofence tutorial*****


        // First check for errors
        if (LocationClient.hasError(intent)) {
            // Get the error code with a static method
            int errorCode = LocationClient.getErrorCode(intent);
            // Log the error
            Log.e("GeofenceTransitionsIntentService",
                    "Location Services error: " +
                            Integer.toString(errorCode)
            );
            /*
             * You can also send the error code to an Activity or
             * Fragment with a broadcast Intent
             */
        /*
         * If there's no error, get the transition type and the IDs
         * of the geofence or geofences that triggered the transition
         */
        } else {
            // Get the type of transition (entry or exit)
            int transitionType =
                    LocationClient.getGeofenceTransition(intent);
            // Test that a valid transition was reported
            if ((transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) ||
                            (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)) {
                List<Geofence> triggerList =
                        LocationClient.getTriggeringGeofences(intent);

                String[] triggerIds = new String[triggerList.size()];

                for (int i = 0; i < triggerIds.length; i++) {
                    // Store the Id of each geofence
                    triggerIds[i] = triggerList.get(i).getRequestId();

                    Log.d("GeofenceTransitionsIntentService", triggerList.toString() + " was triggered");
                }
                /*
                 * At this point, you can store the IDs for further use
                 * display them, or display the details associated with
                 * them.
                 */
            } else {
                Log.e("GeofenceTransitionsIntentService",
                        "Geofence transition error: " +
                                Integer.toString(transitionType));
            }
            // An invalid transition was reported
        }
    }
}
