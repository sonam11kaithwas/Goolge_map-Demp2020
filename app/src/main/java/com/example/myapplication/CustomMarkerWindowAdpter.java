package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Sonam-11 on 2/6/20.
 */
public class CustomMarkerWindowAdpter implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public CustomMarkerWindowAdpter(Context ctx) {
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindow, null);
        TextView txt = view.findViewById(R.id.txt);
        CustomWindowModel customWindowModel = (CustomWindowModel) marker.getTag();

        txt.setText(customWindowModel.getLat() + " " + customWindowModel.getLng() + "\n" +
                customWindowModel.getAdr());
        //+ " " + customWindowModel.getCity
        //                () + "\n" + customWindowModel.getCountry() + " " + customWindowModel.getState()
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
