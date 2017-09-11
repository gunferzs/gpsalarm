package ru.gunferzs.gpsalarm.screens.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import ru.gunferzs.gpsalarm.BroadcastData;
import ru.gunferzs.gpsalarm.R;
import ru.gunferzs.gpsalarm.screens.base.MvpBaseController;
import ru.gunferzs.gpsalarm.services.GpsCheckerService;

/**
 * Created by GunFerzs on 07.09.2017.
 */

public class MapController extends MvpBaseController<MapView, MapPresenter> implements MapView, OnMapReadyCallback {


    @SuppressWarnings({"CanBeFinal", "unused"})
    @BindView(R.id.etDescription)
    EditText etDescription;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @BindView(R.id.etMinute)
    EditText etMinute;

    @SuppressWarnings({"CanBeFinal", "unused"})
    @BindView(R.id.speed)
    TextView speed;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @BindView(R.id.distance)
    TextView distance;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @BindView(R.id.time)
    TextView time;

    @SuppressWarnings({"CanBeFinal", "unused"})
    @BindView(R.id.rlInfoAboutArrive)
    private
    RelativeLayout rlInfoAboutArrive;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @BindView(R.id.rlDescription)
    RelativeLayout rlDescription;

    @SuppressWarnings({"unused", "CanBeFinal"})
    @BindString(R.string.meter_in_seconds)
    String meter_in_seconds;
    @SuppressWarnings({"unused", "CanBeFinal"})
    @BindString(R.string.meters)
    String meters;
    @SuppressWarnings({"unused", "CanBeFinal"})
    @BindString(R.string.minutes)
    String minutes;


    @SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
    MapFragment mapFragment;

    @BindView(R.id.btnStartService)
    Button btnStartService;
    @BindView(R.id.btnChange)
    Button btnChange;


    public final static String BROADCAST_ACTION = "ru.gunferzs.gpsalarm.gps_notify";
    private GoogleMap mMap;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.screen_map, container, false);
    }

    @Override
    protected void viewBound(View view) {
        mapFragment = (MapFragment) getActivitys().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        getActivitys().registerReceiver(broadcastReceiver, intentFilter);
        if(GpsCheckerService.service!=null){
            rlInfoAboutArrive.setVisibility(View.VISIBLE);
            rlDescription.setVisibility(View.GONE);
        }else{
            rlInfoAboutArrive.setVisibility(View.GONE);
            rlDescription.setVisibility(View.VISIBLE);
        }
        setInfo("0","0",0);
    }

    @NonNull
    @Override
    public MapPresenter createPresenter() {
        return new MapPresenter(getActivitys());
    }


    @SuppressWarnings("UnusedParameters")
    @OnClick(R.id.btnStartService)
    public void onClickStartService(View view){
        getPresenter().trackPosition(etDescription.getText().toString(), etMinute.getText().toString());
    }

    @SuppressWarnings("UnusedParameters")
    @OnClick(R.id.btnChange)
    public void onClickChangeLocation(View view){
        rlInfoAboutArrive.setVisibility(View.GONE);
        rlDescription.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("CanBeFinal")
    private GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            getPresenter().chooseLocation(latLng);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver!=null) {
            getActivitys().unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivitys(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivitys(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationManager locationManager = (LocationManager) getActivitys().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        mMap.setOnMapClickListener(onMapClickListener);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(12)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @SuppressWarnings("CanBeFinal")
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(speed!=null&&distance!=null){
                BroadcastData broadcastData = (BroadcastData) intent.getSerializableExtra(GpsCheckerService.SPEED_INFO);
                setInfo(broadcastData.getSpeed(),broadcastData.getDistance(),broadcastData.getTime());
            }
        }
    };

    @SuppressLint("SetTextI18n")
    private void setInfo(String speed, String distance, double time){
        this.speed.setText(speed+meter_in_seconds);
        this.distance.setText(distance+meters);
        this.time.setText(time/60+minutes);
    }

    @Override
    public void setMarker(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    @Override
    public void error(String text) {
        Toast.makeText(getActivitys(),text,Toast.LENGTH_LONG).show();
    }

    private Activity getActivitys(){
        return getActivity();
    }

    @Override
    public void startService() {
        /*if(GpsCheckerService.service!=null){
            GpsCheckerService.service.stopSelf();
        }*/
        Intent intent = new Intent(GpsCheckerService.ACTION_RECEIVER_STOP_SERVICE);
        getActivitys().sendBroadcast(intent);
        getActivitys().startService(new Intent(getActivitys(), GpsCheckerService.class));
        rlInfoAboutArrive.setVisibility(View.VISIBLE);
        rlDescription.setVisibility(View.GONE);

    }
}
