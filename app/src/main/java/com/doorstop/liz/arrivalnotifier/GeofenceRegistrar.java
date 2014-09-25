package com.doorstop.liz.arrivalnotifier;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.location.Geofence;

import java.util.List;

/**
 * Register all the Geofence objects with the appropriate PendingIntent
 * Created by liz on 24/09/14.
 */
public class GeofenceRegistrar extends AsyncTask<GeofenceSms, Void, Void> {

    public static final String TAG = GeofenceRegistrar.class.getSimpleName();

    private static final long DURATION = 3600000L;

    private Context mContext;
    private GeofenceServices mGeofenceServices = null;

    public GeofenceRegistrar(Context context) {
        super();
        mContext = context;
        mGeofenceServices = new GeofenceServices(mContext);
    }

    @Override
    protected Void doInBackground(GeofenceSms... geofenceSmses) {
        int size = geofenceSmses.length;
        for (int i = 0; i < size; i++) {
            GeofenceSms geofenceSms = geofenceSmses[i];

            try {
                Geofence geofence = createGeofenceObject(geofenceSms.getGeofenceModel());
                PendingIntent pendingIntent = createPendingIntent(geofenceSms);
                registerGeofence(geofence, pendingIntent);
            } catch (IllegalArgumentException e) {
                Log.d(TAG, "Could not process geofence " + e.getMessage());
            }

        }
        return null;
    }

    private Geofence createGeofenceObject(GeofenceModel geofenceModel) throws IllegalArgumentException{
        return new Geofence.Builder()
                    .setRequestId(geofenceModel.getId() + "")
                    .setCircularRegion(geofenceModel.getLatitude(), geofenceModel.getLongitude(), geofenceModel.getRadius())
                    .setTransitionTypes(geofenceModel.getTransitionType())
                    .setExpirationDuration(DURATION).build();
    }

    private PendingIntent createPendingIntent(GeofenceSms geofenceSms) {
        Intent intent = new Intent(mContext, SmsService.class);
        intent.putExtra(SmsService.PHONE_NUMBER_KEY, geofenceSms.getPhoneNumber());
        intent.putExtra(SmsService.SMS_MESSAGE_KEY, geofenceSms.getMessage());

        return PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void registerGeofence(Geofence geofence, PendingIntent pendingIntent) {
        Log.d(TAG, "Register a geofence in the async task");
        //Register Geofence with Location services
        mGeofenceServices.addGeofence(geofence, pendingIntent, new GeofenceServices.GeofenceServicesResultListener() {
            @Override
            public void done(String geofenceRefId, GeofenceServicesException e) {
                //tbd
                if (null != e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        });
    }

}
