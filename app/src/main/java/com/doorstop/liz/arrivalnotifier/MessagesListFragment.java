package com.doorstop.liz.arrivalnotifier;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

/**
 * Created by liz on 04/09/14.
 */
public class MessagesListFragment extends ListFragment {

    public static final String TAG = MessagesListFragment.class.getSimpleName();

    public interface MessageListListener {
        public List<GeofenceSms> getMessages();

    }

    private Context mContext;
    private MessageListListener mMessageListListener;
    private MessagesListAdapter mAdapter;

    public static MessagesListFragment getInstance() {
        MessagesListFragment newFragment = new MessagesListFragment();

        Bundle args = new Bundle();
        newFragment.setArguments(args);
        return newFragment;
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

    }
}
