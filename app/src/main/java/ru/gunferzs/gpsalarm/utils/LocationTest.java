package ru.gunferzs.gpsalarm.utils;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by GunFerzs on 06.09.2017.
 */

public class LocationTest {
    private static Location getLocation(double lat, double lng){
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);
        return location;
    }

    public static ArrayList<Location> generateList(){
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(getLocation(48.673079, 44.454854));
        locations.add(getLocation(48.673107, 44.453499));
        locations.add(getLocation(48.672923, 44.452716));
        locations.add(getLocation(48.672356, 44.451847));
        locations.add(getLocation(48.672065, 44.450892));
        locations.add(getLocation(48.671640, 44.450098));
        locations.add(getLocation(48.671881, 44.449283));
        locations.add(getLocation(48.672271, 44.448210));
        locations.add(getLocation(48.672625, 44.447405));
        locations.add(getLocation(48.672986, 44.446579));
        locations.add(getLocation(48.673255, 44.445667));
        locations.add(getLocation(48.673496, 44.445141));
        locations.add(getLocation(48.673808, 44.444358));
        locations.add(getLocation(48.673744, 44.443682));





        return locations;
    }

}
