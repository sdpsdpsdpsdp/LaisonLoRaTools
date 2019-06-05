package com.laisontech.laisonloratools.asynctask;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.laisontech.laisonloratools.callback.LoadDataFromDBListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by SDP
 * on 2019/5/24
 * Des：
 */
public class LocationGeocoderTask extends BaseTask<LatLng, Void, List<Address>> {
    private Geocoder mGeocoder;

    public LocationGeocoderTask(Context context, LoadDataFromDBListener<List<Address>> listener) {
        super(listener);
        mGeocoder = new Geocoder(context);
    }

    @Override
    protected List<Address> doInBackground(LatLng... latLngs) {
        if (latLngs == null || latLngs.length != 1) return null;
        LatLng latLng = latLngs[0];
        if (latLng == null) return null;
        List<Address> addresses = null;
        try {
            addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }
}
