package ru.gunferzs.gpsalarm.db;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import ru.gunferzs.gpsalarm.App;

/**
 * Created by GunFerzs on 07.09.2017.
 */

public class DBHelper {

    private final static String KEY_SET_GPS_POINTS = "key_gps_points";

    private static void init(){
        if(!Hawk.isBuilt()) {
            Hawk.init(App.getContext()).build();
        }
    }

    public static void setGPSPoints(List<GPSPoint> gpsPoints){
        init();
        Hawk.put(KEY_SET_GPS_POINTS,gpsPoints);
    }

    public static List<GPSPoint> getGPSPoints(){
        init();
        return Hawk.get(KEY_SET_GPS_POINTS, new ArrayList<GPSPoint>());
    }

    public static void clear(){
        init();
        Hawk.deleteAll();
    }

}
