package ru.gunferzs.gpsalarm;

import android.location.Location;

import ru.gunferzs.gpsalarm.utils.FilterLowFrequency;

/**
 * Created by GunFerzs on 06.09.2017.
 */

class LocationInfo {
    private double lat;
    private double lng;
    @SuppressWarnings("CanBeFinal")
    private FilterLowFrequency latFilter;
    @SuppressWarnings("CanBeFinal")
    private FilterLowFrequency lngFilter;
    private long timestamp;

    public LocationInfo() {
        latFilter = new FilterLowFrequency();
        lngFilter = new FilterLowFrequency();
    }

    public void setNewLocation(Location location){
        timestamp = System.currentTimeMillis();
        lat = latFilter.filteringNewValue(location.getLatitude());
        lng = lngFilter.filteringNewValue(location.getLongitude());
    }

    public void setNewLocation(LocationInfo newLocation){
        this.lat = newLocation.getLat();
        this.lng = newLocation.getLng();
        this.timestamp = newLocation.getTimestamp();
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
