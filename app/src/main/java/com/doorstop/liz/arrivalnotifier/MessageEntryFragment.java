package com.doorstop.liz.arrivalnotifier;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.location.Geofence;

/**
 * Created by liz on 26/08/14.
 */
public class MessageEntryFragment extends Fragment {

    private GeofenceServices geofenceServices = null;
    private Context context;

    public static MessageEntryFragment getInstance() {
        MessageEntryFragment newFragment = new MessageEntryFragment();

        Bundle args = new Bundle();
        newFragment.setArguments(args);
        return newFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (null != activity) {
            geofenceServices = new GeofenceServices(activity);
            context = activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message_entry, container, false);

        Button addGeofenceButton = (Button) rootView.findViewById(R.id.addGeofenceButton);
        addGeofenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geofence sampleGeofence = new Geofence.Builder()
                        .setRequestId("home")
                        .setCircularRegion(50.112738, -122.954449, 100.0f)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                        .setLoiteringDelay(60000)
                        .setExpirationDuration(3600000).build();

                if (null != geofenceServices) {
                    geofenceServices.addGeofence(sampleGeofence, getNotificationPendingIntent(), new GeofenceServices.GeofenceServicesResultListener() {
                        @Override
                        public void done(String geofenceRefId, GeofenceServicesException e) {
                            //tbd
                        }
                    });
                }
            }
        });
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        geofenceServices = null;
    }

    /*
     * Create a PendingIntent that triggers an IntentService in your
     * app when a geofence transition occurs.
     */
    private PendingIntent getTransitionPendingIntent() {
        // Create an explicit Intent
        Intent intent = new Intent(context,
                GeofenceTransitionsIntentService.class);
        /*
         * Return the PendingIntent
         */
        return PendingIntent.getService(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getNotificationPendingIntent() {
        Intent intent = new Intent(context, ArrivalNotifierActivity.class);
        intent.putExtra("GEOFENCE_NOTIFICATION", "Arrived");

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
