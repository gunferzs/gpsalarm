package ru.gunferzs.gpsalarm.services;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.gunferzs.gpsalarm.AlarmActivity;
import ru.gunferzs.gpsalarm.BroadcastData;
import ru.gunferzs.gpsalarm.MainActivity;
import ru.gunferzs.gpsalarm.R;
import ru.gunferzs.gpsalarm.SpeedCalculator;
import ru.gunferzs.gpsalarm.db.DBHelper;
import ru.gunferzs.gpsalarm.db.GPSPoint;
import ru.gunferzs.gpsalarm.screens.map.MapController;
import ru.gunferzs.gpsalarm.utils.Constants;
import ru.gunferzs.gpsalarm.utils.LocationTest;

/**
 * Created by GunFerzs on 06.09.2017.
 */

public class GpsCheckerService extends Service {

    SpeedCalculator speedCalculator;

    public static GpsCheckerService service;

    public final static String SPEED_INFO = "speed_info";

    LocationManager locationManager;

    GPSPoint gpsPoint;

    @Override
    public void onCreate() {
        service = this;
        String textNotification = getString(R.string.textNotification);
        String titleNotification = getString(R.string.titleNotification);
        Toast.makeText(this,textNotification,Toast.LENGTH_LONG).show();
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(titleNotification)
                .setContentText(textNotification)
                .setSmallIcon(R.mipmap.ic_launcher);
        Notification notification;
        notification = builder.build();

        List<GPSPoint> set = DBHelper.getGPSPoints();
        gpsPoint = set.get(0);
        startForeground(777, notification);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        speedCalculator = new SpeedCalculator(gpsPoint.getLocation());


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                locationListener);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "My Tag");
        wl.acquire();

    }
    PowerManager.WakeLock wl;



    @Override
    public void onDestroy() {
        super.onDestroy();
        service = null;
        locationManager.removeUpdates(locationListener);
        wl.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("startcommand");

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    Location currentLocation;

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            speedCalculator.updateLocation(location);
            Intent intent = new Intent(MapController.BROADCAST_ACTION);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            currentLocation = location;
            BroadcastData broadcastData = new BroadcastData(location, speedCalculator.getCurrentSpeed(),speedCalculator.getDistance());
            if(broadcastData.getTime()<gpsPoint.getMinute()* Constants.SECONDS_OF_MINUTES){
                Intent alarmIntent = new Intent(getBaseContext(), AlarmActivity.class);
                alarmIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                alarmIntent.putExtra(AlarmActivity.KEY_PLACE,gpsPoint.getDescription());
                startActivity(alarmIntent);
            }
            intent.putExtra(SPEED_INFO, new BroadcastData(currentLocation, speedCalculator.getCurrentSpeed(),speedCalculator.getDistance()));
            sendBroadcast(intent);


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


}
