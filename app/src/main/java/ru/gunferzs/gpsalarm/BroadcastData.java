package ru.gunferzs.gpsalarm;

import android.annotation.SuppressLint;
import android.location.Location;

import java.io.Serializable;

/**
 * Created by GunFerzs on 06.09.2017.
 */

public class BroadcastData implements Serializable {
    @SuppressWarnings("CanBeFinal")
    private String speed;
    @SuppressWarnings("CanBeFinal")
    private String distance;
    @SuppressWarnings("CanBeFinal")
    private String location;
    @SuppressWarnings("CanBeFinal")
    private double time;

    public BroadcastData(Location location, double speed, double distance) {
        this.speed = modificator(speed);
        this.distance = modificator(distance);
        this.location = location.getLatitude()+" "+location.getLongitude();
        time = distance/speed;
    }

    @SuppressLint("DefaultLocale")
    private String modificator(double value){
        return String.format("%.3f",value);
    }

    public String getSpeed() {
        return speed;
    }

    public String getDistance() {
        return distance;
    }

    @SuppressWarnings("unused")
    public String getLocation() {
        return location;
    }

    public double getTime() {
        return time;
    }
}
