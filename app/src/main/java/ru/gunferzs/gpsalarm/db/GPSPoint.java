package ru.gunferzs.gpsalarm.db;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by GunFerzs on 07.09.2017.
 */

public class GPSPoint implements Serializable {
    @SuppressWarnings("CanBeFinal")
    private Location location;
    @SuppressWarnings("CanBeFinal")
    private String description;
    @SuppressWarnings("CanBeFinal")
    private double minute;

    public GPSPoint(Location location, String description, double minute) {
        this.location = location;
        this.description = description;
        this.minute = minute;
    }

    public Location getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public double getMinute() {
        return minute;
    }
}
