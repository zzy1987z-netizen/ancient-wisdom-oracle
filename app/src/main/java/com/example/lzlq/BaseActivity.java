package com.example.lzlq;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected SoundManager soundManager;
    protected VibrationManager vibrationManager;
    private View barButtons;
    private boolean isBarExpanded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundManager = SoundManager.getInstance(getApplicationContext());
        vibrationManager = VibrationManager.getInstance();
        vibrationManager.init(getApplicationContext());
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base_final);
        ViewGroup contentContainer = findViewById(R.id.content_container);
        LayoutInflater.from(this).inflate(layoutResID, contentContainer, true);
        setupGlobalUI();
    }

    public void setupGlobalUI() {
        View retractableBarContainer = findViewById(R.id.retractable_bar_container);
        if (retractableBarContainer == null) return;

        barButtons = findViewById(R.id.ll_bar_buttons);
        View barHandle = findViewById(R.id.iv_bar_handle);

        barHandle.setOnClickListener(v -> toggleRetractableBar());
        setupBarButtons();
    }

    private void toggleRetractableBar() {
        if (isBarExpanded) {
            barButtons.animate().alpha(0f).setDuration(200);
        } else {
            barButtons.animate().alpha(1f).setDuration(200);
        }
        isBarExpanded = !isBarExpanded;
    }

    private void setupBarButtons() {
        TextView btnMute = findViewById(R.id.btn_global_mute);
        TextView btnFavorites = findViewById(R.id.btn_global_favorites);
        TextView btnHome = findViewById(R.id.btn_global_home);
        TextView btnDonate = findViewById(R.id.btn_global_donate_placeholder);

        updateMuteButtonState(btnMute);

        btnMute.setOnClickListener(v -> {
            triggerButtonFeedback();
            soundManager.toggleMute();
            updateMuteButtonState((TextView) v);
        });

        btnFavorites.setOnClickListener(v -> {
            triggerButtonFeedback();
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        });

        btnHome.setOnClickListener(v -> {
            triggerButtonFeedback();
            Intent intent = new Intent(this, GuideActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        btnDonate.setOnClickListener(v -> {
            triggerButtonFeedback();
        });
    }

    protected void triggerButtonFeedback() {
        vibrationManager.vibrate();
        soundManager.playButtonClickSound();
    }

    private void updateMuteButtonState(TextView muteButton) {
        if (muteButton == null) return;
        if (soundManager.isMuted()) {
            muteButton.setAlpha(0.5f);
        } else {
            muteButton.setAlpha(1.0f);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundManager.startCountingActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundManager.stopCountingActivity();
    }
}
