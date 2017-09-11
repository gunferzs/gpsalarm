package ru.gunferzs.gpsalarm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.gunferzs.gpsalarm.screens.map.MapController;
import ru.gunferzs.gpsalarm.services.GpsCheckerService;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.tvGPS)
    TextView tvGPS;
    @BindView(R.id.speed)
    TextView speed;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.time)
    TextView time;

    @SuppressWarnings({"CanBeFinal", "unused"})
    @BindView(R.id.btnStart)
    private
    Button btnStart;

    @SuppressWarnings({"CanBeFinal", "unused"})
    @BindView(R.id.container)
    private
    ViewGroup container;

    @SuppressWarnings("FieldCanBeLocal")
    private Router router;

    //PowerManager.WakeLock fullWakeLock, partialWakeLock;

    private static final int GPS_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestGPSPermission();
        router = Conductor.attachRouter(this,container,savedInstanceState);
        if(!router.hasRootController()){
            router.setRoot(RouterTransaction.with(new MapController()));
        }
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, GpsCheckerService.class));
                //requestGPSPermission();
                finish();
            }
        });
        System.out.println(System.currentTimeMillis()+" create");
    }


    private void requestGPSPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    GPS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GPS_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestGPSPermission();
                } else {
                    requestGPSPermission();
                }
                break;
        }
    }
}
