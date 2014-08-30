package com.doorstop.liz.arrivalnotifier;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.common.api.*;
import com.google.android.gms.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liz on 19/08/14.
 */
public class GeofenceServices {

    public interface GeofenceServicesResultListener {
        public void done (String geofenceRefId, GeofenceServicesException e);
    }

    private GoogleApiClient mGoogleApiClient;
    private GooglePlayServicer googlePlayServicer;
    private Context context;

    public GeofenceServices(Context context) {
        this.context = context;
        googlePlayServicer = new GooglePlayServicer();
    }

    public void addGeofence(final Geofence geofenceToAdd, final PendingIntent pendingIntent, final GeofenceServicesResultListener geofenceServicesResultListener) {
        boolean connected = connect(new GoogleApiClient.ConnectionCallbacks() {
                                        @Override
                                        public void onConnected(Bundle bundle) {
                                            Log.d("GeofenceServices", "Connected");
                                            List<Geofence> listOfGeofences = new ArrayList<Geofence>();
                                            listOfGeofences.add(geofenceToAdd);
                                            LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, listOfGeofences, pendingIntent);
                                        }

                                        @Override
                                        public void onConnectionSuspended(int i) {
                                            Log.d("GeofenceServices", "ConnectionSuspended");
                                        }
                                    });
//            @Override
//            public void onConnected(Bundle bundle) {
//                Log.d("GeofenceServices", "Connected...adding geofence now");
//                List<Geofence> listOfGeofences = new ArrayList<Geofence>();
//                listOfGeofences.add(geofenceToAdd);
//                locationClient.addGeofences(listOfGeofences, pendingIntent, new LocationClient.OnAddGeofencesResultListener() {
//                    @Override
//                    public void onAddGeofencesResult(int i, String[] strings) {
//                        String geofenceRedId = "";
//                        GeofenceServicesException exception = null;
//                        if (LocationStatusCodes.SUCCESS == i) {
//                            geofenceRedId = strings[0];
//                        } else {
//                            exception = new GeofenceServicesException("Cannot add Geofence");
//                        }
//                        locationClient.disconnect();
//                        locationClient = null;
//
//                        geofenceServicesResultListener.done(geofenceRedId, exception);
//                    }
//                });
//            }
//
//            @Override
//            public void onDisconnected() {
//                Log.d("GeofenceServices", "Disconnected");
//                locationClient = null;
//            }
//        });

        Log.d("GeofenceServices", "isConnected=" + connected);
        if (!connected) {
            geofenceServicesResultListener.done("", new GeofenceServicesException("Cannot connect to Play Services"));
        }
    }

    /**
     * Returns true if can attempt to connect
     * @param connectionCallbacks
     * @return
     */
    private boolean connect(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        if (null == mGoogleApiClient) {

            boolean playServiceAvailable = googlePlayServicer.areGooglePlayServicesAvailable(context);

            if (playServiceAvailable) {

                Log.d("GeofenceServices", "Build API Client");
                mGoogleApiClient = new GoogleApiClient.Builder(context)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(connectionCallbacks)
                        .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {

                            @Override
                            public void onConnectionFailed(ConnectionResult connectionResult) {
                                Log.d("GeofenceServices", "Connection failed");
                            }
                        }).build();
                mGoogleApiClient.connect();
                return true;
            } else {
                //play services not installed
                return false;
            }
        } else {
            //location client is currently in use
            return false;
        }
    }


}
