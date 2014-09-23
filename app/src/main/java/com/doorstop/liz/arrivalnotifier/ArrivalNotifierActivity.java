package com.doorstop.liz.arrivalnotifier;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.doorstop.liz.arrivalnotifier.dbServices.RepositoryActions;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import java.util.List;


public class ArrivalNotifierActivity extends FragmentActivity implements NotificationFragment.NotificationFragmentListener, MessageEntryFragment.MessageEntryListener, MessagesListFragment.MessageListListener {

    private RepositoryActions<GeofenceSms> mGeofenceSmsRepository;
    private RepositoryActions<GeofenceModel> mGeofenceModelRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGeofenceSmsRepository =
                ((ArrivalNotifierApplication) getApplication()).getGeofenceSmsRepository();
        mGeofenceModelRepository =
                ((ArrivalNotifierApplication) getApplication()).getGeofenceModelRepository();

        setContentView(R.layout.activity_arrival_notifier);

        Log.d("ArrivalNotifierActivity", "entered onCreate method");
        Intent intent = getIntent();
        boolean hasExtra = intent.hasExtra("GEOFENCE_NOTIFICATION");

        int transitionType =
                LocationClient.getGeofenceTransition(intent);

            // Test that a valid transition was reported
        if (hasExtra || (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) ||
                    (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, NotificationFragment.getInstance(), NotificationFragment.TAG)
                    .commit();
        } else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MessageEntryFragment.getInstance(), MessageEntryFragment.TAG)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.arrival_notifier, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_list) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MessagesListFragment.getInstance(), MessagesListFragment.TAG)
                    .commit();
        } else if (id == R.id.action_add) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MessageEntryFragment.getInstance(), MessageEntryFragment.TAG)
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDismiss() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .remove(manager.findFragmentByTag(NotificationFragment.TAG))
                .add(R.id.container, MessageEntryFragment.getInstance(), MessageEntryFragment.TAG)
                .commit();
    }

    @Override
    public Long persistGeofenceSms(GeofenceSms geofenceSms) {
        return mGeofenceSmsRepository.insertOrUpdate(geofenceSms);
    }

    @Override
    public Long persistGeofenceModel(GeofenceModel geofenceModel) {
        return mGeofenceModelRepository.insertOrUpdate(geofenceModel);
    }

    @Override
    public List<GeofenceSms> getMessages() {
        return mGeofenceSmsRepository.getAllItems();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_arrival_notifier_list, container, false);
            return rootView;
        }
    }
}
