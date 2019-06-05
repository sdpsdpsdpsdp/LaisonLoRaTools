package com.laisontech.laisonloratools.location.event;

import android.location.Location;

/**
 * Created by SDP
 * on 2019/3/29
 * Des：
 */
public class LocationEvent {
    private Location location;

    public LocationEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
