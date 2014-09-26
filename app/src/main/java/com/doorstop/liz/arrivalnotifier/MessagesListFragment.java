package com.doorstop.liz.arrivalnotifier;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;

/**
 * Created by liz on 04/09/14.
 */
public class MessagesListFragment extends ListFragment {

    public static final String TAG = MessagesListFragment.class.getSimpleName();

    public interface MessageListListener {
        public List<GeofenceSms> getMessages();
        public void showDeleteDialog(String itemName, long id);

    }

    public static final String UPDATE_MESSAGES_INTENT = "com.doorstop.liz.arrivalnotifier.UpdateMessagesIntent";

    private Context mContext;
    private MessageListListener mMessageListListener;
    private MessagesListAdapter mAdapter;


    private BroadcastReceiver mUpdateMessagesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //TBD implement with loader.
            mAdapter.clear();
            mAdapter.addAll(mMessageListListener.getMessages());
            mAdapter.notifyDataSetChanged();
        }
    };

    public static MessagesListFragment getInstance() {
        MessagesListFragment newFragment = new MessagesListFragment();

        Bundle args = new Bundle();
        newFragment.setArguments(args);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;

        try {
            mMessageListListener = (MessageListListener) activity;
        } catch (ClassCastException e) {
            mMessageListListener = null;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (null != mMessageListListener) {
            mAdapter = new MessagesListAdapter(mContext, R.layout.messages_list_item, mMessageListListener.getMessages());

            setListAdapter(mAdapter);
        }

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != mMessageListListener && null != mAdapter) {
                    GeofenceSms geofenceSms = mAdapter.getItem(position);
                    mMessageListListener.showDeleteDialog("TBD", geofenceSms.getId());
                }
                return true;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(mContext).registerReceiver(mUpdateMessagesReceiver,
                new IntentFilter(UPDATE_MESSAGES_INTENT));
    }

    @Override
    public void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mUpdateMessagesReceiver);
    }
}
