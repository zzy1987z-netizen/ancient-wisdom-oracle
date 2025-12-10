package com.example.lzlq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.Random;

public class DaodejingActivity extends BaseActivity {

    private static final String STATE_CHAPTER_TEXT = "STATE_CHAPTER_TEXT";
    // 关键修复：使用最终确定的“人时”
    private static final int AUTO_JUMP_DELAY = 20000; // 20秒
    private static final long CHARACTER_DELAY = 400; // 每秒2.5字

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable nextActivityRunnable = this::goToNextActivity;
    private boolean hasInitialized = false;
    private String mChapterText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daodejing);

        if (savedInstanceState != null) {
            mChapterText = savedInstanceState.getString(STATE_CHAPTER_TEXT, null);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_CHAPTER_TEXT, mChapterText);
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

        TypewriterTextView tvChapter = findViewById(R.id.tv_daodejing_chapter);
        findViewById(R.id.btn_skip_daodejing_styled).setOnClickListener(v -> {
            triggerButtonFeedback();
            goToNextActivity();
        });

        startChapterAnimation(tvChapter);
        handler.postDelayed(nextActivityRunnable, AUTO_JUMP_DELAY);
    }

    private void startChapterAnimation(TypewriterTextView tvChapter) {
        if (mChapterText == null) {
            mChapterText = DaodejingManager.getInstance().getRandomChapter();
        }

        if (mChapterText == null || mChapterText.isEmpty()) {
            mChapterText = "道可道，非常道。";
        }
        
        // 关键修复：使用恒定的、充满禅意的打字机速度
        tvChapter.setCharacterDelay(CHARACTER_DELAY);
        tvChapter.animateText(mChapterText);
    }

    // 关键修复：此处的跳转，是跳转到“出签页”，而非再次摇签
    private void goToNextActivity() {
        handler.removeCallbacks(nextActivityRunnable);
        if (isFinishing()) return;

        int signNumber = new Random().nextInt(100) + 1;
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("SIGN_NUMBER", signNumber);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(nextActivityRunnable);
        super.onDestroy();
    }
}
