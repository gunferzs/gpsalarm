package ru.gunferzs.gpsalarm.screens.map;


import com.google.android.gms.maps.model.LatLng;
import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by GunFerzs on 07.09.2017.
 */

interface MapView extends MvpView {
    void setMarker(LatLng latLng);
    void error(String text);
    void startService();
}
