package ru.gunferzs.gpsalarm;

import android.location.Location;

import ru.gunferzs.gpsalarm.utils.FilterLowFrequency;

import static android.location.Location.distanceBetween;

/**
 * Created by GunFerzs on 06.09.2017.
 */

public class SpeedCalculator {

    private double currentSpeed;
    private double distance;
    @SuppressWarnings("CanBeFinal")
    private FilterLowFrequency distanceFilter = new FilterLowFrequency();
    @SuppressWarnings("unused")
    FilterLowFrequency speedFilter = new FilterLowFrequency();

    @SuppressWarnings("CanBeFinal")
    private LocationInfo oldLocation;
    @SuppressWarnings("CanBeFinal")
    private LocationInfo newLocation;
    @SuppressWarnings("CanBeFinal")
    private Location target;

    public SpeedCalculator(Location target) {
        this.target = target;
        oldLocation = new LocationInfo();
        newLocation = new LocationInfo();
    }

    public void updateLocation(Location location){
        oldLocation.setNewLocation(newLocation);
        newLocation.setNewLocation(location);
        float[] result = new float[1];
        float[] resultTemp = new float[1];
        distanceBetween(oldLocation.getLat(),oldLocation.getLng(),newLocation.getLat(),newLocation.getLng(),resultTemp);
        //distanceBetween(newLocation.getLat(),newLocation.getLng(),48.673612, 44.443837,result);
        distanceBetween(newLocation.getLat(),newLocation.getLng(),target.getLatitude(), target.getLongitude(),result);
        distance = distanceFilter.filteringNewValue(result[0]);
        System.out.println((resultTemp[0]/((newLocation.getTimestamp()-oldLocation.getTimestamp())/1000.0))+" speed");
        System.out.println(resultTemp[0]);
        System.out.println(newLocation.getTimestamp());
        System.out.println(oldLocation.getTimestamp());
        currentSpeed = (resultTemp[0]/((newLocation.getTimestamp()-oldLocation.getTimestamp())/1000.0));
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public double getDistance() {
        return distance;
    }
}
