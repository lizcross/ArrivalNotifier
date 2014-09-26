package com.doorstop.liz.arrivalnotifier;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.doorstop.liz.arrivalnotifier.dbServices.RepositoryActions;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import java.util.List;


public class ArrivalNotifierActivity extends FragmentActivity implements MessageEntryFragment.MessageEntryListener, MessagesListFragment.MessageListListener {

    private RepositoryActions<GeofenceSms> mGeofenceSmsRepository;
    private RepositoryActions<GeofenceModel> mGeofenceModelRepository;
    private static final String ALERT_DIALOG_TAG = "Alert_Dialog";
    private static final String DELETE_ALERT_DIALOG_TAG = "Delete_Alert_Dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGeofenceSmsRepository =
                ((ArrivalNotifierApplication) getApplication()).getGeofenceSmsRepository();
        mGeofenceModelRepository =
                ((ArrivalNotifierApplication) getApplication()).getGeofenceModelRepository();

        setContentView(R.layout.activity_arrival_notifier);

        Log.d("ArrivalNotifierActivity", "entered onCreate method");

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, MessagesListFragment.getInstance(), MessagesListFragment.TAG)
                .commit();

        Intent intent = getIntent();
        boolean hasExtra = intent.hasExtra("GEOFENCE_NOTIFICATION");

        int transitionType =
                LocationClient.getGeofenceTransition(intent);
        Location arrivalLocation = LocationClient.getTriggeringLocation(intent);

            // Test that a valid transition was reported
        if (hasExtra || (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) ||
                    (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)) {
            displayAlertDialog("You have arrived at " + arrivalLocation);
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
    public Long persistGeofenceSms(GeofenceSms geofenceSms) {
        return mGeofenceSmsRepository.insertOrUpdate(geofenceSms);
    }

    @Override
    public void deleteGeofenceModel(long id) {
        mGeofenceModelRepository.deleteItemWithId(id);
    }

    @Override
    public void displayAlertDialog(String title) {
        SingleMessageAlertDialog alertDialog = SingleMessageAlertDialog.getInstance(title);
        alertDialog.show(getSupportFragmentManager(), ALERT_DIALOG_TAG);
    }

    @Override
    public Long persistGeofenceModel(GeofenceModel geofenceModel) {
        return mGeofenceModelRepository.insertOrUpdate(geofenceModel);
    }

    @Override
    public List<GeofenceSms> getMessages() {
        return mGeofenceSmsRepository.getAllItems();
    }

    @Override
    public void showDeleteDialog(String itemName, long id) {
        DeleteItemAlertDialog alertDialog = DeleteItemAlertDialog.getInstance(itemName, id);
        alertDialog.show(getSupportFragmentManager(), DELETE_ALERT_DIALOG_TAG);
    }

    public void dismissAlertDialog(String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment alertDialogFrag = getSupportFragmentManager().findFragmentByTag(tag);
        if (alertDialogFrag != null) {
            ft.remove(alertDialogFrag);
        }
//        ft.addToBackStack(null);
    }

    public void deleteGeofenceSmsItem(long id) {
        GeofenceSms geofenceSmsToDelete = mGeofenceSmsRepository.getItemForId(id);
        long geofenceModelIdToDelete = geofenceSmsToDelete.getGeofenceModelId();

        mGeofenceSmsRepository.deleteItemWithId(id);
        mGeofenceModelRepository.deleteItemWithId(geofenceModelIdToDelete);

        Intent updateMessagesIntent = new Intent(MessagesListFragment.UPDATE_MESSAGES_INTENT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(updateMessagesIntent);
    }

    public static class SingleMessageAlertDialog extends DialogFragment {

        private static final String TITLE_IDENTIFIER = "title";

        public static SingleMessageAlertDialog getInstance(String title) {
            SingleMessageAlertDialog frag = new SingleMessageAlertDialog();
            Bundle args = new Bundle();
            args.putString(TITLE_IDENTIFIER, title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString(TITLE_IDENTIFIER);

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ((ArrivalNotifierActivity) getActivity()).dismissAlertDialog(ALERT_DIALOG_TAG);

                        }
                    }).create();
            return alertDialog;
        }
    }

    public static class DeleteItemAlertDialog extends DialogFragment {

        private static final String ITEM_NAME_IDENTIFIER = "itemName";
        private static final String GEOFENCE_SMS_ITEM_ID_IDENTIFIER = "geofenceSmsItemIdIdentifier";

        public static DeleteItemAlertDialog getInstance(String itemName, long geofenceSmsItemId) {
            DeleteItemAlertDialog frag = new DeleteItemAlertDialog();
            Bundle args = new Bundle();
            args.putString(ITEM_NAME_IDENTIFIER, itemName);
            args.putLong(GEOFENCE_SMS_ITEM_ID_IDENTIFIER, geofenceSmsItemId);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            String itemName = arguments.getString(ITEM_NAME_IDENTIFIER);
            final long itemId = arguments.getLong(GEOFENCE_SMS_ITEM_ID_IDENTIFIER);

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Would you like to delete " + itemName)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ((ArrivalNotifierActivity) getActivity()).deleteGeofenceSmsItem(itemId);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((ArrivalNotifierActivity) getActivity()).dismissAlertDialog(DELETE_ALERT_DIALOG_TAG);
                        }
                    }).create();
            return alertDialog;
        }
    }
}
