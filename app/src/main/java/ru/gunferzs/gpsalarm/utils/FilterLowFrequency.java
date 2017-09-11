package ru.gunferzs.gpsalarm.utils;

/**
 * Created by GunFerzs on 06.09.2017.
 */

public class FilterLowFrequency {

    private final static double EPSILON = 0.0001;

    @SuppressWarnings("FieldCanBeLocal")
    private double oldValue = 0;
    private double newValue = 0;
    private double k = 0.8;

    @SuppressWarnings("unused")
    public void setK(double k) {
        this.k = k;
    }

    public double filteringNewValue(double newValue){
        newValue = EPSILON<newValue ? newValue : 0;
        oldValue = this.newValue;
        if(this.newValue!=0) {
            this.newValue = oldValue * (1 - k) + k * newValue;

        }else{
            this.newValue = newValue;
        }
        return this.newValue;
    }
}
