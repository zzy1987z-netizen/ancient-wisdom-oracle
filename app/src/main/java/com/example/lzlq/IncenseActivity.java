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

public class IncenseActivity extends BaseActivity {

    private static final int INCENSE_JUMP_DELAY = 7000; // 7秒

    private VideoView videoView;
    private View coverImage;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable nextActivityRunnable = this::goToNextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incense);
        // 燃香界面不需要全局UI，所以不调用setupGlobalUI()

        videoView = findViewById(R.id.vv_incense);
        coverImage = findViewById(R.id.iv_incense_cover);
        View btnSkip = findViewById(R.id.btn_skip_incense_styled);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.incense_static;
        videoView.setVideoURI(Uri.parse(videoPath));

        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            videoView.start();

            coverImage.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    coverImage.setVisibility(View.GONE);
                }
            });
        });

        btnSkip.setOnClickListener(v -> {
            triggerButtonFeedback();
            goToNextActivity();
        });

        handler.postDelayed(nextActivityRunnable, INCENSE_JUMP_DELAY);
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
