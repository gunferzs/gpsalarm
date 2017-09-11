package ru.gunferzs.gpsalarm;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import java.io.IOException;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GunFerzs on 07.09.2017.
 */

public class AlarmActivity extends AppCompatActivity {

    public final static String KEY_PLACE = "place";

    @BindView(R.id.btnOK)
    Button btnOK;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @BindView(R.id.tvArrivedOnPlace)
    TextView tvArrivedOnPlace;

    @SuppressWarnings({"CanBeFinal", "unused"})
    @BindString(R.string.to_the_place)
    String to_the_place;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String place = intent.getExtras().getString(KEY_PLACE, "");
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tvArrivedOnPlace.setText(to_the_place + " " + place);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(this, alert);
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.setLooping(true);

                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("UnusedParameters")
    @OnClick(R.id.btnOK)
    public void onClickOK(View view) {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.stop();
    }
}
