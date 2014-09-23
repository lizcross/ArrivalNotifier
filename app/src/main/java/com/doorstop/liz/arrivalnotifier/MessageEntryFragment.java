package com.doorstop.liz.arrivalnotifier;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.Geofence;

import java.util.Random;

/**
 * Created by liz on 26/08/14.
 */
public class MessageEntryFragment extends Fragment {

    public interface MessageEntryListener {
        public Long persistGeofenceModel(GeofenceModel geofenceModel);
        public Long persistGeofenceSms(GeofenceSms geofenceSms);
        public void deleteGeofenceModel(long id);
    }

    public static final String TAG = MessageEntryFragment.class.getSimpleName();

    private MessageEntryListener messageEntryListener = null;
    private GeofenceServices mGeofenceServices = null;
    private Context mContext;
    private Button mAddGeofenceButton;
    private EditText mLatitudeEditText;
    private EditText mLongitudeEditText;
    private EditText mPhoneNumberEditText;
    private EditText mMessageEditText;

    private Random mRandom = new Random();

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
            mGeofenceServices = new GeofenceServices(activity);
            mContext = activity;
        }

        try {
            messageEntryListener = (MessageEntryListener) activity;
        } catch (ClassCastException e) {
            messageEntryListener = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message_entry, container, false);

        mAddGeofenceButton = (Button) rootView.findViewById(R.id.addGeofenceButton);
        mAddGeofenceButton.setOnClickListener(createAddGeofenceButtonListener());

        mLatitudeEditText = (EditText) rootView.findViewById(R.id.latitude);
        mLongitudeEditText = (EditText) rootView.findViewById(R.id.longitude);
        mPhoneNumberEditText = (EditText) rootView.findViewById(R.id.phoneNumber);
        mMessageEditText = (EditText) rootView.findViewById(R.id.message);

        return rootView;
    }

    private View.OnClickListener createAddGeofenceButtonListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Persist the geofence and message in the database
                double latitudeValue = Double.parseDouble(mLatitudeEditText.getText().toString());
                double longitudeValue = Double.parseDouble(mLongitudeEditText.getText().toString());
                float radius = 20.0f;
                long duration = 3600000L;
                int transitionType = Geofence.GEOFENCE_TRANSITION_ENTER;

                //Neeed to add to DB first to get the unique id generated then we can
                //create the Geofence object using the builder and see if the values are correct
                //if not correct then need to remove the geofence object from the db

                boolean geofenceAdded = true;

                if (null != messageEntryListener && null != mGeofenceServices) {
                    GeofenceModel geofenceModel = new GeofenceModel(null, latitudeValue, longitudeValue, radius, duration, transitionType);
                    Long geofenceModelId = messageEntryListener.persistGeofenceModel(geofenceModel);
                    Geofence locationServicesGeofence = null;

                    try {
                        locationServicesGeofence = new Geofence.Builder()
                                .setRequestId(geofenceModelId + "")
                                .setCircularRegion(latitudeValue, longitudeValue, radius)
                                .setTransitionTypes(transitionType)
                                .setExpirationDuration(duration).build();
                    } catch (IllegalArgumentException e) {
                        //delete geofenceModel from Db
                        messageEntryListener.deleteGeofenceModel(geofenceModelId);
                        geofenceAdded = false;

                    }

                    if (geofenceAdded) {
                        //Add sms to db
                        GeofenceSms geofenceSms = new GeofenceSms(null,
                                mPhoneNumberEditText.getText().toString(),
                                mMessageEditText.getText().toString(),
                                geofenceModelId);
                        messageEntryListener.persistGeofenceSms(geofenceSms);

                        //Register Geofence with Location services
                        mGeofenceServices.addGeofence(locationServicesGeofence, getNotificationPendingIntent(), new GeofenceServices.GeofenceServicesResultListener() {
                            @Override
                            public void done(String geofenceRefId, GeofenceServicesException e) {
                                //tbd
                                Log.d(TAG, e.getMessage());
                            }
                    });

                    }
                } else {
                    geofenceAdded = false;
                }

                if (geofenceAdded) {
                    clearTextFields();
                } else {
                    //show an error dialog
                }

            }
        };
    }

    private void clearTextFields() {
        mLatitudeEditText.setText("");
        mLongitudeEditText.setText("");
        mPhoneNumberEditText.setText("");
        mMessageEditText.setText("");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mGeofenceServices = null;
    }

    /*
     * Create a PendingIntent that triggers an IntentService in your
     * app when a geofence transition occurs.
     */
    private PendingIntent getTransitionPendingIntent() {
        // Create an explicit Intent
        Intent intent = new Intent(mContext,
                GeofenceTransitionsIntentService.class);
        /*
         * Return the PendingIntent
         */
        return PendingIntent.getService(
                mContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getNotificationPendingIntent() {
        Intent intent = new Intent(mContext, ArrivalNotifierActivity.class);
        intent.putExtra("GEOFENCE_NOTIFICATION", "Arrived");

        return PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
