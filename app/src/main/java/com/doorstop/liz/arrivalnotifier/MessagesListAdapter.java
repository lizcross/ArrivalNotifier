package com.doorstop.liz.arrivalnotifier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by liz on 22/09/14.
 */
public class MessagesListAdapter extends ArrayAdapter<GeofenceSms> {

    private Context mContext;
    private int mRowLayout;

    public MessagesListAdapter(Context context, int resource, List<GeofenceSms> objects) {
        super(context, resource, objects);

        mContext = context;
        mRowLayout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(mRowLayout, parent, false);

        TextView latitudeTV = (TextView) rowView.findViewById(R.id.listItemLatitude);
        TextView longitudeTV = (TextView) rowView.findViewById(R.id.listItemLongitude);
        TextView phoneNumberTV = (TextView) rowView.findViewById(R.id.listItemPhoneNumber);
        TextView messageTV = (TextView) rowView.findViewById(R.id.listItemMessage);

        GeofenceSms geofenceSms = getItem(position);
        GeofenceModel geofenceModel = geofenceSms.getGeofenceModel();

        latitudeTV.setText(geofenceModel.getLatitude().toString());
        longitudeTV.setText(geofenceModel.getLongitude().toString());
        phoneNumberTV.setText(geofenceSms.getPhoneNumber());
        messageTV.setText(geofenceSms.getMessage());

        return rowView;
    }
}
