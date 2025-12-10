package com.example.lzlq;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.VideoView;

public class PreIncenseActivity extends BaseActivity {

    private static final String STATE_INCENSE_STARTED = "STATE_INCENSE_STARTED";
    private static final int INCENSE_DURATION = 5000; // 5秒

    private VideoView videoView;
    private View coverLayout, btnPray, btnSkip;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable nextActivityRunnable = this::goToNextActivity;
    private boolean hasInitialized = false;
    private boolean mIncenseProcessStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_incense);

        if (savedInstanceState != null) {
            mIncenseProcessStarted = savedInstanceState.getBoolean(STATE_INCENSE_STARTED, false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_INCENSE_STARTED, mIncenseProcessStarted);
    }

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
        videoView = findViewById(R.id.vv_incense);
        coverLayout = findViewById(R.id.rl_pre_incense_cover);
        btnPray = findViewById(R.id.btn_pray_styled);
        btnSkip = findViewById(R.id.btn_skip_styled);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.incense_static;
        videoView.setVideoURI(Uri.parse(videoPath));

        if (mIncenseProcessStarted) {
            // 如果仪式已开始，则直接显示动画，不再显示“启”按钮
            startIncenseProcess();
        } else {
            btnPray.setOnClickListener(v -> {
                triggerButtonFeedback();
                startIncenseProcess();
            });
        }

        btnSkip.setOnClickListener(v -> {
            triggerButtonFeedback();
            goToNextActivity();
        });
    }

    private void startIncenseProcess() {
        mIncenseProcessStarted = true;
        btnPray.setVisibility(View.GONE);

        videoView.setOnPreparedListener(mp -> mp.setLooping(true));
        videoView.start();

        coverLayout.animate().alpha(0f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                coverLayout.setVisibility(View.GONE);
            }
        });

        btnSkip.setVisibility(View.VISIBLE);
        btnSkip.animate().alpha(1f).setDuration(1500);

        handler.postDelayed(nextActivityRunnable, INCENSE_DURATION);
    }

    private void goToNextActivity() {
        handler.removeCallbacks(nextActivityRunnable);
        if (isFinishing()) return;
        startActivity(new Intent(this, ShakeActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(nextActivityRunnable);
        if (videoView != null) videoView.stopPlayback();
        super.onDestroy();
    }
}
