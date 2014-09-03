package com.doorstop.liz.arrivalnotifier;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by liz on 28/08/14.
 */
public class NotificationFragment extends Fragment{

    public static final String TAG = NotificationFragment.class.getSimpleName();

    public interface NotificationFragmentListener {
        public void onDismiss();
    }

    private Button mDismissButton;
    private NotificationFragmentListener mNotificationFragmentListener;


    public static NotificationFragment getInstance() {
        NotificationFragment newFragment = new NotificationFragment();

        Bundle args = new Bundle();
        newFragment.setArguments(args);
        return newFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mNotificationFragmentListener = (NotificationFragmentListener) activity;
        } catch (ClassCastException e) {
            Log.d(TAG, "Activity does not implement listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        mDismissButton = (Button) rootView.findViewById(R.id.dismissButton);
        mDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mNotificationFragmentListener) {
                    mNotificationFragmentListener.onDismiss();
                }

            }
        });
        return rootView;
    }
}
