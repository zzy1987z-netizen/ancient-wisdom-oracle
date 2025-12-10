package com.example.lzlq;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class VibrationManager {

    private static VibrationManager instance;
    private Vibrator vibrator;

    private VibrationManager() {}

    public static synchronized VibrationManager getInstance() {
        if (instance == null) {
            instance = new VibrationManager();
        }
        return instance;
    }

    public void init(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrate() {
        if (vibrator != null && vibrator.hasVibrator()) {
            // 为了兼容新旧安卓版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(50); // 震动50毫秒
            }
        }
    }
}
