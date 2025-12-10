package com.example.lzlq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class DrawFailActivity extends BaseActivity {

    private static final String STATE_POEM_TEXT = "STATE_POEM_TEXT";
    // 关键修复：使用最终确定的“人时”
    private static final int AUTO_JUMP_DELAY = 20000; // 20秒
    private static final long CHARACTER_DELAY = 400; // 每秒2.5字

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable nextActivityRunnable = this::goToNextActivity;
    private boolean hasInitialized = false;
    private String mPoemText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_fail);

        if (savedInstanceState != null) {
            mPoemText = savedInstanceState.getString(STATE_POEM_TEXT, null);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_POEM_TEXT, mPoemText);
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

        TypewriterTextView tvPoem = findViewById(R.id.tv_fail_poem);
        findViewById(R.id.btn_continue_from_fail).setOnClickListener(v -> {
            triggerButtonFeedback();
            goToNextActivity();
        });

        startPoemAnimation(tvPoem);
        handler.postDelayed(nextActivityRunnable, AUTO_JUMP_DELAY);
    }

    private void startPoemAnimation(TypewriterTextView tvPoem) {
        if (mPoemText == null) {
            mPoemText = FailPoemManager.getInstance().getRandomPoem();
        }
        
        if (mPoemText == null || mPoemText.isEmpty()) {
            mPoemText = "此中有真意，\n欲辨已忘言。";
        }
        
        // 关键修复：使用恒定的、充满禅意的打字机速度
        tvPoem.setCharacterDelay(CHARACTER_DELAY);
        tvPoem.animateText(mPoemText);
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
        super.onDestroy();
    }
}
