package ru.gunferzs.gpsalarm.screens.map;

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.ButterKnife;
import ru.gunferzs.gpsalarm.R;
import ru.gunferzs.gpsalarm.db.DBHelper;
import ru.gunferzs.gpsalarm.db.GPSPoint;

/**
 * Created by GunFerzs on 07.09.2017.
 */

public class MapPresenter extends MvpBasePresenter<MapView> {

    private Location chooserLocation;

    @SuppressWarnings("unused")
    @BindString(R.string.error_fill_field)
    private String ERROR_FILL_FILED;

    @Override
    public void attachView(MapView view) {
        super.attachView(view);
    }

    public void chooseLocation(LatLng latLng){
        chooserLocation = new Location("");
        chooserLocation.setLatitude(latLng.latitude);
        chooserLocation.setLongitude(latLng.longitude);
        getView().setMarker(latLng);
    }

    public void trackPosition(String description, String minute){
        if(chooserLocation!=null&&!description.isEmpty()&&!minute.isEmpty()){
            List<GPSPoint> gpsPoints = new ArrayList<>();
            chooserLocation.setLatitude(48.673612);
            chooserLocation.setLongitude(44.443837);
            gpsPoints.add(new GPSPoint(chooserLocation, description, Double.parseDouble(minute)));
            DBHelper.setGPSPoints(gpsPoints);
            getView().startService();
        }else{
            getView().error(ERROR_FILL_FILED);
        }
    }

    public MapPresenter(Activity activity) {
        ButterKnife.bind(this,activity);
    }
}
