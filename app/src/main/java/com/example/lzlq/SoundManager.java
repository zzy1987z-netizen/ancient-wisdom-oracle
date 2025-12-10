package com.example.lzlq;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.concurrent.atomic.AtomicInteger;

public class SoundManager {

    private static SoundManager instance;

    private MediaPlayer bgMediaPlayer;
    private final SoundPool soundPool;
    private final int buttonClickSoundId;
    private final int shakeSoundId;
    private boolean isMuted = false;
    private final AtomicInteger activeActivities = new AtomicInteger(0);

    private SoundManager(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();

        buttonClickSoundId = soundPool.load(context, R.raw.button_click_bamboo, 1);
        shakeSoundId = soundPool.load(context, R.raw.shake_sound, 1);

        bgMediaPlayer = MediaPlayer.create(context, R.raw.bg_music_guqin);
        if (bgMediaPlayer != null) {
            bgMediaPlayer.setLooping(true);
        }
    }

    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context.getApplicationContext());
        }
        return instance;
    }

    public void playButtonClickSound() {
        if (!isMuted) {
            soundPool.play(buttonClickSoundId, 1, 1, 0, 0, 1);
        }
    }

    public void playShakeSound() {
        if (!isMuted) {
            soundPool.play(shakeSoundId, 1, 1, 0, 0, 1);
        }
    }

    public void startCountingActivity() {
        if (activeActivities.getAndIncrement() == 0 && bgMediaPlayer != null && !isMuted) {
            bgMediaPlayer.start();
        }
    }

    public void stopCountingActivity() {
        if (activeActivities.decrementAndGet() == 0 && bgMediaPlayer != null && bgMediaPlayer.isPlaying()) {
            bgMediaPlayer.pause();
        }
    }

    public void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            if (bgMediaPlayer != null && bgMediaPlayer.isPlaying()) {
                bgMediaPlayer.pause();
            }
        } else {
            if (activeActivities.get() > 0 && bgMediaPlayer != null) {
                bgMediaPlayer.start();
            }
        }
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void release() {
        if (bgMediaPlayer != null) {
            bgMediaPlayer.release();
            bgMediaPlayer = null;
        }
        soundPool.release();
        instance = null;
    }
}
