package com.doorstop.liz.arrivalnotifier;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liz on 28/08/14.
 */
public class NotificationFragment extends Fragment{

    public static NotificationFragment getInstance() {
        NotificationFragment newFragment = new NotificationFragment();

        Bundle args = new Bundle();
        newFragment.setArguments(args);
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        return rootView;
    }
}
