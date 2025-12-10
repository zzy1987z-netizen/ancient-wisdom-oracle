package com.example.lzlq;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import java.util.Random;

public class ShakeActivity extends BaseActivity implements SensorEventListener {

    private static final String STATE_IS_SHAKING = "STATE_IS_SHAKING";
    private static final float SHAKE_THRESHOLD = 12.0f;

    private boolean isShaking = false;
    private boolean hasInitialized = false;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private VideoView vvShakeVideo;
    private View coverLayout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        if (savedInstanceState != null) {
            isShaking = savedInstanceState.getBoolean(STATE_IS_SHAKING, false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_IS_SHAKING, isShaking);
    }

    // 关键恢复：使用“延迟加载”来确保丝滑体验
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !hasInitialized) {
            hasInitialized = true;
            initializeAndSetupContent();
        }
    }

    private void initializeAndSetupContent() {
        setupGlobalUI();

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        vvShakeVideo = findViewById(R.id.vv_shake_video);
        coverLayout = findViewById(R.id.rl_shake_cover);
        findViewById(R.id.btn_shake_styled).setOnClickListener(v -> startShakingProcess());

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.shake_video;
        vvShakeVideo.setVideoURI(Uri.parse(videoPath));

        vvShakeVideo.setOnCompletionListener(mp -> handleResult());

        if (isShaking) {
            startShakingProcess();
        }
    }

    private void startShakingProcess() {
        if (isShaking && hasInitialized) return; 
        isShaking = true;
        sensorManager.unregisterListener(this);
        
        soundManager.playShakeSound();

        // 终极修复：在这里遥控“心诚则灵”水印的出现
        findViewById(R.id.tv_watermark_cover).setVisibility(View.VISIBLE);
        
        vvShakeVideo.setVisibility(View.VISIBLE);
        vvShakeVideo.start();

        coverLayout.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                coverLayout.setVisibility(View.GONE);
            }
        });
    }

    private void handleResult() {
        if (isFinishing()) return;
        int result = new Random().nextInt(2);
        if (result == 1) handleSuccess();
        else handleFailure();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double acceleration = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;
            if (acceleration > SHAKE_THRESHOLD) {
                startShakingProcess();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasInitialized && !isShaking) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
    
    private void handleSuccess() {
        sharedPreferences.edit().putInt("failedAttempts", 0).apply();
        
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("SIGN_NUMBER", new Random().nextInt(100) + 1);
        startActivity(intent);
        finish(); 
    }

    private void handleFailure() {
        int failedAttempts = sharedPreferences.getInt("failedAttempts", 0) + 1;
        sharedPreferences.edit().putInt("failedAttempts", failedAttempts).apply();
        
        Intent intent;
        if (failedAttempts >= 3) {
            sharedPreferences.edit().putInt("failedAttempts", 0).apply();
            intent = new Intent(this, DaodejingActivity.class);
        } else {
            intent = new Intent(this, DrawFailActivity.class);
        }
        startActivity(intent);
        finish(); 
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    protected void onDestroy() {
        if (vvShakeVideo != null) vvShakeVideo.stopPlayback();
        super.onDestroy();
    }
}
