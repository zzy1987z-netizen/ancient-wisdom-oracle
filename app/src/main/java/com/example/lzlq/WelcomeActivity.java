package com.example.lzlq;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private boolean hasInitialized = false;

    private SoundManager soundManager;
    private VibrationManager vibrationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 先询问“守门人”，如果流程已完成，则立刻自我了断
        if (MyApplication.isWelcomeFlowCompleted) {
            goToNextActivity();
            return;
        }

        // 为这个独立的模块独立供能
        soundManager = SoundManager.getInstance(getApplicationContext());
        vibrationManager = VibrationManager.getInstance();
        vibrationManager.init(getApplicationContext());

        setContentView(R.layout.activity_welcome);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !hasInitialized) {
            hasInitialized = true;
            // 首次获得焦点时，初始化内容
            initializeAndSetupContent();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 确保 soundManager 已初始化，并且欢迎流程还没走完
        if (soundManager != null && !MyApplication.isWelcomeFlowCompleted) {
            soundManager.startCountingActivity();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 确保 soundManager 已初始化
        if (soundManager != null) {
            soundManager.stopCountingActivity();
        }
    }

    private void initializeAndSetupContent() {
        TextView tvContent = findViewById(R.id.tv_welcome_content);
        String baseText = getString(R.string.user_agreement_full_text);
        String warningText = "\n\n---\n\n<b>弘扬传统文化，请勿封建迷信。</b>";
        tvContent.setText(Html.fromHtml(baseText + warningText, Html.FROM_HTML_MODE_LEGACY));
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());

        final CheckBox cbAgree = findViewById(R.id.cb_agree);
        final Button btnEnter = findViewById(R.id.btn_enter);

        btnEnter.setEnabled(false);

        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnEnter.setEnabled(isChecked);
        });

        btnEnter.setOnClickListener(v -> {
            if (v.isEnabled()) {
                triggerButtonFeedback();
                // 通知“守门人”，大门已锁上！
                MyApplication.isWelcomeFlowCompleted = true;
                goToNextActivity();
            }
        });
    }

    private void goToNextActivity() {
        // 如果欢迎流程已经完成，这里的跳转目标应该是主界面，而非再次引导
        // 但为了保持逻辑的纯粹性，我们暂时仍然跳转到GuideActivity
        // 最终的优化，应该是直接跳转到一个“主页”
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    private void triggerButtonFeedback() {
        if(soundManager == null || vibrationManager == null) return;
        vibrationManager.vibrate();
        soundManager.playButtonClickSound();
    }
}
